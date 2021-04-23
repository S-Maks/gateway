package com.msp.invoice.gateway;

import cc.blynk.clickhouse.BalancedClickhouseDataSource;
import cc.blynk.clickhouse.settings.ClickHouseProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClickhouseConfig {

    @Bean
    @ConfigurationProperties(prefix = "db.clickhouse")
    public ClickHouseProperties clickHouseProperties() {
        return new ClickHouseProperties();
    }

    @Bean
    public BalancedClickhouseDataSource chDataSource(
            @Value("${db.clickhouse.url}") String url,
            ClickHouseProperties properties
    ) {
        return new BalancedClickhouseDataSource(url, properties);
    }
}
