package network;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public abstract class AbstractUdpSocket {
    // Flags
    protected final AtomicBoolean connected = new AtomicBoolean(false);
    protected final AtomicBoolean error = new AtomicBoolean(false);
    // Error
    protected volatile String errorString = "";
    // Message pool
    protected final ConcurrentLinkedQueue<byte[]> iQueue = new ConcurrentLinkedQueue<>();
    protected final LinkedBlockingQueue<byte[]> oQueue = new LinkedBlockingQueue<>();
    // Flow statistic
    protected long up = 0, down = 0, upt = 0, downt = 0; 

    // Check connection
    public boolean isConnected() {
        return connected.get();
    }
    // Check error
    public boolean isError() {
        return error.get();
    }
    // Get error string
    public String getError() {
        return errorString;
    }
    // Check message
    public boolean hasMessage() {
        return !iQueue.isEmpty();
    }
    // Get message
    public byte[] get() {
        return iQueue.poll();
    }
    // Send message
    public void send(byte[] b) {
        oQueue.add(b);
    }
    // Set error
    public void setError(String obj) {
        error.set(true);
        errorString = obj;
    }
    // Get upload
    public long getUpload() {
        return up;
    }
    // Get download
    public long getDownload() {
        return down;
    }
    // Reset upload
    public void resetUpload() {
        up = upt;
        upt = 0;
    }
    // Reset download
    public void resetDownload() {
        down = downt;
        downt = 0;
    }
    // Debug
    public void byte2Hex(byte[] b) {
        for (byte i : b) {
            int tmp = i;
            System.out.print(Integer.toHexString(tmp & 0xFF) + ' ');
        }
        System.out.print('\n');
    }
}
