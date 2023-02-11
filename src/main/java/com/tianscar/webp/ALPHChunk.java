package com.tianscar.webp;

public class ALPHChunk extends RawDataChunk {

    static final byte[] ALPH = new byte[] { 'A', 'L', 'P', 'H' };

    public static final int PREPROCESSING_MASK = 3 << 4;
    public static final int FILTERING_METHOD_MASK = 3 << 2;
    public static final int COMPRESSION_MASK = 3 << 0;

    public static final int FLAG_NO_PREPROCESSING = 0 << 4;
    public static final int FLAG_LEVEL_REDUCTION = 1 << 4;
    public static final int FLAG_NO_FILTER = 0 << 2;
    public static final int FLAG_HORIZONTAL_FILTER = 1 << 2;
    public static final int FLAG_VERTICAL_FILTER = 2 << 2;
    public static final int FLAG_GRADIENT_FILTER = 3 << 2;
    public static final int FLAG_NO_COMPRESSION = 0 << 0;
    public static final int FLAG_LOSSLESS_COMPRESSION = 1 << 0;

    private final int rsvPFC;
    public ALPHChunk(byte[] data) {
        super(ALPH, data);
        rsvPFC = data[0];
    }

    public int getFlags() {
        return rsvPFC;
    }

    public int getPreprocessingFlag() {
        return rsvPFC & PREPROCESSING_MASK;
    }

    public int getFilteringMethodFlag() {
        return rsvPFC & FILTERING_METHOD_MASK;
    }

    public int getCompressionFlag() {
        return rsvPFC & COMPRESSION_MASK;
    }

}
