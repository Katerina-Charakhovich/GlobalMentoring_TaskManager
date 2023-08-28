package com.example.taskmanager.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableMongoRepositories(basePackages = "com.example")
public class MongoDbConfig extends AbstractMongoClientConfiguration {

    private String databaseName = "task_manager";
    private String url = "mongodb://localhost:27019/task_manager";

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(url))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
        return super.mongoTemplate(databaseFactory, converter);
    }

    @Override
    public MongoDatabaseFactory mongoDbFactory() {
        return super.mongoDbFactory();
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
