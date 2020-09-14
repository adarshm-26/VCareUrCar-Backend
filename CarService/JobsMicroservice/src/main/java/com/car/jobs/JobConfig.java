package com.car.jobs;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.car.jobs")

public class JobConfig {
    @Bean
    public MongoClient mongo(){
        ConnectionString connectionString=new ConnectionString("mongodb://localhost:27017/CarService");
        MongoClientSettings mongoClientSettings=MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(mongoClientSettings);
    }
    @Bean
    public MongoTemplate mongoTemplate() throws Exception{
        return new MongoTemplate(mongo(),"CarService");
    }
}
