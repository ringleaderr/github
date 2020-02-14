package com.github.recruitment.config;

import com.github.recruitment.config.proportiest.ConnectionHttpClientFeature;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientGithubConfig {

    @Bean
    @ConfigurationProperties ( "github.http-client-config" )
    ConnectionHttpClientFeature getConnectionHttpClientFeature() {
        return new ConnectionHttpClientFeature();
    }

    @Bean
    public ClientHttpRequestFactory createOKCommonsRequestFactory( OkHttpClientFactory okHttpRequestFactory ) {
        OkHttpClient client = okHttpRequestFactory.createBuilder( false ).build();
        return new OkHttp3ClientHttpRequestFactory( client );
    }

    @Bean
    public ConnectionPool createConnectionPool( ConnectionHttpClientFeature feature ) {
        return new ConnectionPool(
                feature.getMaxIdleConnections(),
                feature.getKeepAliveDuration(),
                TimeUnit.SECONDS );
    }
}
