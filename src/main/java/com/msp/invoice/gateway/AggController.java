package com.msp.invoice.gateway;

import cc.blynk.clickhouse.BalancedClickhouseDataSource;
import cc.blynk.clickhouse.ClickHouseConnection;
import cc.blynk.clickhouse.ClickHouseStatement;
import cc.blynk.clickhouse.util.ClickHouseRowBinaryInputStream;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.OutputStream;
import java.sql.ResultSet;

@RestController
@RequestMapping("/api/agg")
@Secured({"ROLE_READ"})
public class AggController {
    private final BalancedClickhouseDataSource chDataSource;

    public AggController(BalancedClickhouseDataSource chDataSource) {
        this.chDataSource = chDataSource;
    }

    @GetMapping(value = "/patches", produces = MediaType.TEXT_PLAIN_VALUE)
    public StreamingResponseBody aggInstalledPatches() {
        return out -> useResultSet(
                "SELECT count(*) " +
                        "FROM patch_history " +
                        "WHERE PatchState = 1",
                rs -> {
                    rs.next();
                    out.write(rs.getBytes(1));
                });
    }

    @GetMapping("/agents")
    public StreamingResponseBody aggInvoiceData() {
        return out -> readInto(
                "SELECT if(position(OSInfo, 'Server Standard') > 0, 'Servers', 'Workstations') AS Kind, " +
                        "   count(*) as Count " +
                        "FROM agents " +
                        "GROUP BY Kind " +
                        "FORMAT JSON",
                out);
    }

    private void readInto(String sql, OutputStream destination) {
        useStatement(stmt -> {
            try (ClickHouseRowBinaryInputStream binary = stmt.executeQueryClickhouseRowBinaryStream(sql);
                 ChInputStream dataStream = new ChInputStream(binary)) {
                dataStream.transferTo(destination);
            }
        });
    }

    private void useResultSet(String sql, ThrowableConsumer<ResultSet> consumer) {
        useStatement(stmt -> {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                consumer.accept(rs);
            }
        });
    }

    private void useStatement(ThrowableConsumer<ClickHouseStatement> consumer) {
        try (ClickHouseConnection conn = chDataSource.getConnection();
             ClickHouseStatement stmt = conn.createStatement()) {
            consumer.accept(stmt);
        } catch (Throwable err) {
            throw new RestClientException(err.getMessage());
        }
    }

    @FunctionalInterface
    private interface ThrowableConsumer<T> {
        void accept(T t) throws Throwable;
    }
}
