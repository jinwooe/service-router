package com.skcc.servicerouter.utils;


import org.junit.Assert;
import org.junit.Test;

public class BytesUtilTest {
    @Test
    public void test() {
        byte[] bytes = BytesUtil.intToBytes(1);
        Assert.assertEquals(1, BytesUtil.bytesToInt(bytes));

        bytes = BytesUtil.intToBytes(256);
        Assert.assertEquals(256, BytesUtil.bytesToInt(bytes));

        bytes = BytesUtil.intToBytes(-1);
        Assert.assertEquals(-1, BytesUtil.bytesToInt(bytes));

        bytes = new byte[]{0,0,0,1};
        Assert.assertEquals(1, BytesUtil.bytesToInt(bytes));

    }
}
