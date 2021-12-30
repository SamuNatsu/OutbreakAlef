package network;

import auxiliary.*;
import com.*;
import entity.*;
import scene.*;

import java.io.*;

public class SvrClt {
    // Socket
    static public final UdpServer server = new UdpServer();
    static public final UdpClient client = new UdpClient();
    static public AbstractUdpSocket socket;
    // Frame control
    static public boolean next = true;
    static public long frame = 0;
    // Flow statistic
    static private long lstUD = 0;
    // Remote state
    static public final Vec2 rmp = new Vec2();
    static public boolean rmd = false;

    // Initialize
    static public void init() {
        socket = (Shared.isSvr ? server : client);
        next = true;
        frame = 0;
        lstUD = System.currentTimeMillis();
        rmp.reset();
        rmd = false;
    }
    static public void syncRandom() {
        if (Shared.isMultiplayer) {
            if (Shared.isSvr) {
                server.send(Pack.getRSD());
                while (server.connected.get())
                    if (server.hasMessage() && server.get()[0] == Pack.ACK)
                        break;
            }
            else {
                while (client.connected.get())
                    if (client.hasMessage()) {
                        byte[] buf = client.get();
                        if (buf[0] != Pack.RSD)
                            continue;
                        DataInputStream in = new DataInputStream(new ByteArrayInputStream(buf));
                        try {
                            in.readByte();
                            Utils.setSeed(in.readLong());
                        }
                        catch (Exception e) {
                            continue;
                        }
                        client.send(Pack.getACK());
                        break;
                    }
            }
        }
    }
    static public boolean checkConnection() {
        if (Shared.isMultiplayer && !socket.isConnected()) {
            Shared.isNetIrrupt = true;
            SceneManager.transfer("Menu");
            return false;
        }
        return true;
    }
    static public void updateFlow() {
        if (Shared.isMultiplayer && System.currentTimeMillis() - lstUD > 1000) {
            socket.resetUpload();
            socket.resetDownload();
            lstUD = System.currentTimeMillis();
        }
    }
    static public void syncGame() {
        if (Shared.isMultiplayer)
            while (socket.hasMessage()) {
                byte[] buf = socket.get();
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(buf));
                try {
                    byte op = in.readByte();
                    switch (op) {
                        case Pack.STT:
                            Shared.isGameOver = true;
                            return;
                        case Pack.NXF:
                            next = true;
                            return;
                        case Pack.IMP: {
                            double px = in.readDouble();
                            double py = in.readDouble();
                            (Shared.isSvr ? EntityPool.p2 : EntityPool.p1)
                                .addImpulse(new Vec2(px, py));
                            break;
                        }
                        case Pack.GMB:
                            for (int i = 0; i < 10; ++i)
                                EntityPool.generateMob();
                            break;
                        case Pack.GBL: {
                            double px = in.readDouble();
                            double py = in.readDouble();
                            double vx = in.readDouble();
                            double vy = in.readDouble();
                            int dmg = in.readInt();
                            double rng = in.readDouble();
                            EntityPool.bullet.add(
                                new Bullet(
                                    new Vec2(px, py), 
                                    new Vec2(vx, vy),
                                    dmg, rng,
                                    (Shared.isSvr ? (byte)1 : (byte)0)));
                            break;
                        }
                        case Pack.ACK:
                            next = true;
                            socket.send(Pack.getNXF());
                            return;
                        case Pack.ASC: {
                            int sc = in.readShort();
                            (Shared.isSvr ? EntityPool.p2 : EntityPool.p1)
                                .addScoreN(sc);
                            break;
                        }
                        case Pack.CUR: {
                            int hp = in.readByte();
                            (Shared.isSvr ? EntityPool.p2 : EntityPool.p1)
                                .cure(hp);
                            break;
                        }
                        case Pack.GHM: {
                            double px = in.readDouble();
                            double py = in.readDouble();
                            EntityPool.loot.add(new Medicine(new Vec2(px, py)));
                            break;
                        }
                        case Pack.SWG: {
                            byte b = in.readByte();
                            (Shared.isSvr ? EntityPool.p2 : EntityPool.p1)
                                .switchGunN(b);
                            break;
                        }
                        case Pack.MPS: {
                            rmp.x = in.readShort();
                            rmp.y = in.readShort();
                            rmd = (in.readByte() == 1 ? true : false);
                            break;
                        }
                        default:
                            break;
                    }
                }
                catch (Exception e) {}
            }
    }
    static public void syncPack() {
        if (Shared.isMultiplayer) {
            next = false;
            if (!Shared.isSvr)
                socket.send(Pack.getACK());
        }
    }
}
