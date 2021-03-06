package network;

import auxiliary.*;
import entity.*;

import java.io.*;

import com.Camera;
import com.Mouse;

public class Pack {
    // Static flags
    static public final byte STT = 0;   // Start game
    static public final byte RSD = 1;   // Random seed
    static public final byte NXF = 2;   // Next frame
    static public final byte IMP = 3;   // Impulse
    static public final byte GMB = 4;   // Generate mob
    static public final byte GBL = 5;   // Generate bullet
    static public final byte ACK = 6;   // Acknowledged
    static public final byte ASC = 7;   // Add score
    static public final byte CUR = 8;   // Cure player
    static public final byte GHM = 9;  // Generate HP medic
    static public final byte SWG = 10;  // Switch gun
    static public final byte MPS = 11;  // Mouse position & state

    // Pack maker
    static public byte[] getSST() {
        byte[] tmp = {STT};
        return tmp;
    }
    static public byte[] getRSD() {
        ByteArrayOutputStream b = new ByteArrayOutputStream(9);
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeByte(RSD);
            out.writeLong(Utils.getSeed());
            out.flush();
            return b.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static public byte[] getNXF() {
        byte[] tmp = {NXF};
        return tmp;
    }
    static public byte[] getIMP(Vec2 obj) {
        ByteArrayOutputStream b = new ByteArrayOutputStream(17);
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeByte(IMP);
            out.writeDouble(obj.x);
            out.writeDouble(obj.y);
            out.flush();
            return b.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static public byte[] getGMB() {
        byte[] tmp = {GMB};
        return tmp;
    }
    static public byte[] getGBL(Vec2 pos, Vec2 v, int dmg, double rng) {
        ByteArrayOutputStream b = new ByteArrayOutputStream(45);
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeByte(GBL);
            out.writeDouble(pos.x);
            out.writeDouble(pos.y);
            out.writeDouble(v.x);
            out.writeDouble(v.y);
            out.writeInt(dmg);
            out.writeDouble(rng);
            out.flush();
            return b.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static public byte[] getACK() {
        byte[] tmp = {ACK};
        return tmp;
    }
    static public byte[] getASC(int sc) {
        ByteArrayOutputStream b = new ByteArrayOutputStream(3);
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeByte(ASC);
            out.writeShort((short)sc);
            out.flush();
            return b.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static public byte[] getCUR(int hp) {
        ByteArrayOutputStream b = new ByteArrayOutputStream(2);
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeByte(CUR);
            out.writeByte((byte)hp);
            out.flush();
            return b.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static public byte[] getGHM(Vec2 pos) {
        ByteArrayOutputStream b = new ByteArrayOutputStream(17);
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeByte(GHM);
            out.writeDouble(pos.x);
            out.writeDouble(pos.y);
            out.flush();
            return b.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static public byte[] getSWG(Player.Gun tp) {
        ByteArrayOutputStream b = new ByteArrayOutputStream(2);
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeByte(SWG);
            switch (tp) {
                case AR:
                    out.writeByte(0);
                    break;
                case SG:
                    out.writeByte(1);
                    break;
                case SMG:
                    out.writeByte(2);
            }
            out.flush();
            return b.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static public byte[] getMPS() {
        ByteArrayOutputStream b = new ByteArrayOutputStream(6);
        DataOutputStream out = new DataOutputStream(b);
        try {
            Vec2 tmp = Camera.trans2GPos(Mouse.position);
            out.writeByte(MPS);
            out.writeShort((short)tmp.x);
            out.writeShort((short)tmp.y);
            out.writeByte(Mouse.down ? 1 : 0);
            out.flush();
            return b.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
