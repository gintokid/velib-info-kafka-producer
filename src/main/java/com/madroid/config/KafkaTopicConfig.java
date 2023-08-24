package com.madroid.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic velibDataStationInfoTopic() {
        return TopicBuilder.name("velibdata-station-info")
                .build();
    }

    @Bean
    public NewTopic velibDataStationStatusTopic() {
        return TopicBuilder.name("velibdata-station-status")
                .build();
    }
}
