package network;

import auxiliary.*;
import com.*;
import entity.*;
import scene.*;

import java.io.*;

public class SvrClt {
    static public AbstractUdpSocket socket;             // Now socket
    static public boolean next = true;                  // Next frame flag
    static public long frame = 0;                       // Frame number
    static public long lstUD = 0;                       // Last update time
    static public final Vec2 rmp = Vec2.getInstance();  // Remote mouse position
    static public boolean rmd = false;                  // Remote mouse down

    static public void init() {
        socket = (Shared.isSvr ? Shared.server : Shared.client);
        next = true;
        frame = 0;
        lstUD = System.currentTimeMillis();
        rmp.reset();
        rmd = false;
    }
    static public void syncRandom() {
        if (Shared.enableNetwork) {
            if (Shared.isSvr) {
                Shared.server.send(Pack.getRSD());
                while (Shared.server.connected.get())
                    if (Shared.server.hasMessage() && Shared.server.get()[0] == Pack.ACK)
                        break;
            }
            else {
                while (Shared.client.connected.get())
                    if (Shared.client.hasMessage()) {
                        byte[] buf = Shared.client.get();
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
                        Shared.client.send(Pack.getACK());
                        break;
                    }
            }
        }
    }
    static public boolean checkConnection() {
        if (Shared.enableNetwork && !socket.isConnected()) {
            Shared.netDiscon = true;
            SceneManager.transfer("Menu");
            return false;
        }
        return true;
    }
    static public void updateFlow() {
        if (Shared.enableNetwork && System.currentTimeMillis() - lstUD > 1000) {
            socket.resetUpload();
            socket.resetDownload();
            lstUD = System.currentTimeMillis();
        }
    }
    static public void syncGame() {
        if (Shared.enableNetwork)
            while (socket.hasMessage()) {
                byte[] buf = socket.get();
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(buf));
                try {
                    byte op = in.readByte();
                    switch (op) {
                        case Pack.STT:
                            Shared.gameSet = true;
                            return;
                        case Pack.NXF:
                            next = true;
                            return;
                        case Pack.IMP: {
                            double px = in.readDouble();
                            double py = in.readDouble();
                            (Shared.isSvr ? EntityPool.p2 : EntityPool.p1)
                                .addImpulse(Vec2.getInstance(px, py));
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
                                    Vec2.getInstance(px, py), 
                                    Vec2.getInstance(vx, vy),
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
                            EntityPool.loot.add(new HPMedic(Vec2.getInstance(px, py)));
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
        if (Shared.enableNetwork) {
            next = false;
            if (!Shared.isSvr)
                socket.send(Pack.getACK());
        }
    }
}
