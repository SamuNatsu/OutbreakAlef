package network;

import network.rudp.*;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class UdpClient extends AbstractUdpSocket {
    // Socket
    private ReliableSocket remote;

    // Reset
    public void reset() {
        try {
            remote.close();
        }
        catch (Exception e) {}
        remote = null;
        connected.set(false);
        error.set(false);
        iQueue.clear();
        oQueue.clear();
        up = down = upt = downt = 0;
    }
    // Connect
    public void connect(String ip, int port) {
        reset();
        new Thread(()-> {
            try {
                remote = new ReliableSocket();
                remote.connect(new InetSocketAddress(ip, port), 5000);
                connected.set(true);
                new Thread(()-> {
                    try {
                        DataInputStream in = new DataInputStream(remote.getInputStream());
                        byte len;
                        byte[] buf;
                        while (connected.get()) {
                            len = in.readByte();
                            buf = new byte[len];
                            in.readFully(buf);
                            downt += len + 1;
                            iQueue.add(buf);
                        }
                    }
                    catch (Exception e) {
                        connected.set(false);
                        try {
                            remote.close();
                        }
                        catch (Exception _e) {}
                    }
                }, "UpdClient-Receiver").start();
                new Thread(()-> {
                    try {
                        DataOutputStream out = new DataOutputStream(remote.getOutputStream());
                        byte[] buf;
                        while (connected.get()) {
                            buf = oQueue.poll(50, TimeUnit.MILLISECONDS);
                            if (buf == null)
                                continue;
                            out.writeByte(buf.length);
                            out.write(buf);
                            out.flush();
                            upt += buf.length + 1;
                        }
                    }
                    catch (Exception e) {
                        connected.set(false);
                        try {
                            remote.close();
                        }
                        catch (Exception _e) {}
                    }
                }, "UpdClient-Sender").start();
            }
            catch (Exception e) {
                if (e.getMessage() == null)
                    setError("Connecting time out");
                else 
                    setError(e.getMessage());
            }
        }, "UdpClient-Connect").start();
    }
    // Get remote address
    public String getRemoteAddress() {
        return connected.get() ? remote.getInetAddress().getHostAddress() + ':' + remote.getPort() : "<No connection>";
    }
}
