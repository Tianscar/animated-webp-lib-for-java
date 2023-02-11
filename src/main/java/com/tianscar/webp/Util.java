package com.tianscar.webp;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Objects;

final class Util {

    private Util() {
        throw new UnsupportedOperationException();
    }

    public static boolean arrayEquals(byte[] a, byte[] b) {
        if (a == null || b == null) return false;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i ++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    public static final long UINT32_MAX = 4294967295L; // 2 ^ 32 - 1
    public static final int  UINT24_MAX = 16777215;    // 2 ^ 24 - 1
    public static final int  UINT16_MAX = 65535;       // 2 ^ 16 - 1;
    public static final BigInteger BI_UINT32_MAX = new BigInteger(String.valueOf(UINT32_MAX));

    public static int checkUInt16(int arg) {
        return checkArgRange(arg, 0, UINT16_MAX, "uint16");
    }

    public static int checkUInt24(int arg) {
        return checkArgRange(arg, 0, UINT24_MAX, "uint24");
    }

    public static long checkUInt32(long arg) {
        return checkArgRange(arg, 0, UINT32_MAX, "uint32");
    }

    public static BigInteger checkUInt32(BigInteger arg) {
        return checkArgRange(arg, BigInteger.ZERO, BI_UINT32_MAX, "uint32");
    }

    private static long checkArgRange(long arg, long min, long max, String msg) {
        if (arg < min) throw new IllegalArgumentException(msg + " < " + min);
        else if (arg > max) throw new IllegalArgumentException(msg + " > " + max);
        return arg;
    }

    private static int checkArgRange(int arg, int min, int max, String msg) {
        if (arg < min) throw new IllegalArgumentException(msg + " < " + min);
        else if (arg > max) throw new IllegalArgumentException(msg + " > " + max);
        return arg;
    }

    private static BigInteger checkArgRange(BigInteger arg, BigInteger min, BigInteger max, String msg) {
        Objects.requireNonNull(arg);
        Objects.requireNonNull(min);
        Objects.requireNonNull(max);
        Objects.requireNonNull(msg);
        if (arg.compareTo(min) < 0) throw new IllegalArgumentException(msg + " < " + min);
        else if (arg.compareTo(max) > 0) throw new IllegalArgumentException(msg + " > " + max);
        return arg;
    }

    public static void writePadding(OutputStream out, int payload) throws IOException {
        for (int i = 0; i < payload; i ++) {
            out.write(0);
        }
    }

    public static void writePadding(OutputStream out, long payload) throws IOException {
        for (long i = 0; i < payload; i ++) {
            out.write(0);
        }
    }

    public static int readInt8(InputStream in) throws IOException {
        byte[] buf = new byte[1];
        if (readNBytes(in, buf, 0, 1) != 1) throw new EOFException();
        return (((0 & 0xFF) << 24) |
                ((0 & 0xFF) << 16) |
                ((0 & 0xFF) <<  8) |
                ((buf[0] & 0xFF) <<  0));
    }

    public static int readUInt8(InputStream in) throws IOException {
        return readInt8(in);
    }

    public static void writeInt8(OutputStream out, int val) throws IOException {
        out.write((byte) val);
    }

    public static void writeUInt8(OutputStream out, int val) throws IOException {
        writeInt8(out, val);
    }

    public static int readInt16(InputStream in) throws IOException {
        byte[] buf = new byte[2];
        if (readNBytes(in, buf, 0, 2) != 2) throw new EOFException();
        return (((0 & 0xFF) << 24) |
                ((0 & 0xFF) << 16) |
                ((buf[1] & 0xFF) <<  8) |
                ((buf[0] & 0xFF) <<  0));
    }

    public static int readUInt16(InputStream in) throws IOException {
        return readInt16(in);
    }

    public static void writeInt16(OutputStream out, int val) throws IOException {
        byte[] buf = new byte[2];
        buf[0] = (byte) (val >>> 0);
        buf[1] = (byte) (val >>> 8);
        out.write(buf, 0, 2);
    }

    public static void writeUInt16(OutputStream out, int val) throws IOException {
        writeInt16(out, val);
    }

    public static int readInt24(InputStream in) throws IOException {
        byte[] buf = new byte[3];
        if (readNBytes(in, buf, 0, 3) != 3) throw new EOFException();
        return (((0 & 0xFF) << 24) |
                        ((buf[2] & 0xFF) << 16) |
                        ((buf[1] & 0xFF) <<  8) |
                        ((buf[0] & 0xFF) <<  0));
    }

    public static int readUInt24(InputStream in) throws IOException {
        return readInt24(in);
    }

    public static void writeInt24(OutputStream out, int val) throws IOException {
        byte[] buf = new byte[3];
        buf[0] = (byte) (val >>> 0);
        buf[1] = (byte) (val >>> 8);
        buf[2] = (byte) (val >>> 16);
        out.write(buf, 0, 3);
    }

    public static void writeUInt24(OutputStream out, int val) throws IOException {
        writeInt24(out, val);
    }

    public static int readInt32(InputStream in) throws IOException {
        byte[] buf = new byte[4];
        if (readNBytes(in, buf, 0, 4) != 4) throw new EOFException();
        return toInt32(buf);
    }

    public static int toInt32(byte[] buf) {
        return (((buf[3] & 0xFF) << 24) |
                ((buf[2] & 0xFF) << 16) |
                ((buf[1] & 0xFF) <<  8) |
                ((buf[0] & 0xFF) <<  0));
    }

    public static long toUInt32(int val) {
        return ((long) val) & 0xFFFFFFFFL;
    }

    public static long toUInt32(byte[] buf) {
        return toUInt32(toInt32(buf));
    }

    public static long readUInt32(InputStream in) throws IOException {
        return toUInt32(readInt32(in));
    }

    public static void writeInt32(OutputStream out, long val) throws IOException {
        byte[] buf = new byte[4];
        buf[0] = (byte) (val >>>  0);
        buf[1] = (byte) (val >>>  8);
        buf[2] = (byte) (val >>> 16);
        buf[3] = (byte) (val >>> 24);
        out.write(buf, 0, 4);
    }

    public static void writeUInt32(OutputStream out, long val) throws IOException {
        writeInt32(out, val);
    }

    public static int read1Based(InputStream in) throws IOException {
        return readUInt24(in) + 1;
    }

    public static void write1Based(OutputStream out, int i) throws IOException {
        checkUInt24(i);
        writeUInt24(out, i - 1);
    }

    public static byte[] readFourCC(InputStream in) throws IOException {
        byte[] buf = new byte[4];
        if (readNBytes(in, buf, 0, 4) != 4) throw new EOFException();
        return buf;
    }

    public static void readFourCC(InputStream in, byte[] buf) throws IOException {
        if (Objects.requireNonNull(buf).length != 4) throw new IllegalArgumentException("buf length must be 4");
        if (readNBytes(in, buf, 0, 4) != 4) throw new EOFException();
    }

    public static void writeFourCC(OutputStream out, byte[] fourCC) throws IOException {
        out.write(fourCC);
    }

    public static void writeFourCC(OutputStream out, byte c1, byte c2, byte c3, byte c4) throws IOException {
        out.write(c1);
        out.write(c2);
        out.write(c3);
        out.write(c4);
    }

    public static byte[] readNBytes(InputStream in, int len) throws IOException {
        byte[] buf = new byte[len];
        if (readNBytes(in, buf, 0, len) != len) throw new EOFException();
        return buf;
    }

    public static int readNBytes(InputStream in, byte[] b, int off, int len) throws IOException {
        Objects.requireNonNull(in);
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0)
                break;
            n += count;
        }
        return n;
    }

    public static boolean isOdd(long num) {
        return (num & 1) == 1;
    }

    public static boolean isOdd(int num) {
        return (num & 1) == 1;
    }

    public static void skip1Byte(InputStream in) throws IOException {
        skipNBytes(in, 1);
    }

    public static void skipNBytes(InputStream in, long n) throws IOException {
        Objects.requireNonNull(in);
        while (n > 0) {
            long ns = in.skip(n);
            if (ns > 0 && ns <= n) {
                n -= ns;
            }
            else if (ns == 0) {
                if (in.read() == -1) {
                    throw new EOFException();
                }
                n --;
            }
            else throw new IOException("Unable to skip exactly");
        }
    }

}
