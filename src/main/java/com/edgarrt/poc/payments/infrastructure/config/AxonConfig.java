package com.edgarrt.poc.payments.infrastructure.config;

import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.jdbc.PersistenceExceptionResolver;
import org.axonframework.common.jdbc.UnitOfWorkAwareConnectionProviderWrapper;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;

@Configuration
public class AxonConfig {
    @Bean
    public EventStorageEngine eventStorageEngine(DataSource dataSource, Serializer serializer, PersistenceExceptionResolver persistenceExceptionResolver) {
        ConnectionProvider cp = () -> DataSourceUtils.getConnection(dataSource);
        return JdbcEventStorageEngine.builder()
                .connectionProvider(new UnitOfWorkAwareConnectionProviderWrapper(cp))
                .serializer(serializer)
                .persistenceExceptionResolver(persistenceExceptionResolver)
                .build();
    }
}
