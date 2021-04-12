package com.msp.invoice.gateway;

import cc.blynk.clickhouse.util.ClickHouseRowBinaryInputStream;

import java.io.IOException;
import java.io.InputStream;

public class ChInputStream extends InputStream {
    private final ClickHouseRowBinaryInputStream source;

    public ChInputStream(ClickHouseRowBinaryInputStream source) {
        this.source = source;
    }

    @Override
    public int read() throws IOException {
        return source.readByte();
    }

    @Override
    public void close() throws IOException {
        source.close();
    }
}
