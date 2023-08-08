package com.example.jariBean.config.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Lazy
@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends AbstractElasticSearchConfig{

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
