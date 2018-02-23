package com.skcc.servicerouter.utils;

import java.nio.ByteBuffer;

public class BytesUtil {
    public static byte[] intToBytes(int i) {
        ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / 8);
        bb.putInt(i);
        return bb.array();
    }

    public static int bytesToInt(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / 8);
        bb.put(bytes);
        bb.flip();
        return bb.getInt();
    }
}
