package network;

import network.rudp.*;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class UdpServer extends AbstractUdpSocket {
    // Socket
    private ReliableServerSocket listener;
    private Socket remote;

    // Get local port
    public int getLocalPort() {
        return listener.getLocalPort();
    }
    // Reset
    public void reset() {
        try {
            listener.close();
        }
        catch (Exception e) {}
        listener = null;
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
    // Start server
    public void start() {
        reset();
        int retry = 0;
        while (true)
            try {
                listener = new ReliableServerSocket(0);
                break;
            }
            catch (Exception e) {
                ++retry;
                if (retry > 20) {
                    setError("Cannot allocate port");
                    return;
                }
            }
        new Thread(()-> {
            try {
                remote = listener.accept();
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
                }, "UpdServer-Receiver").start();
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
                }, "UpdServer-Sender").start();
            }
            catch (Exception e) {
                setError(e.getMessage());
            }
        }, "UdpServer-Start").start();
    }
    // Get remote address
    public String getRemoteAddress() {
        return connected.get() ? remote.getInetAddress().getHostAddress() + ':' + remote.getPort() : "<No connection>";
    }
}
