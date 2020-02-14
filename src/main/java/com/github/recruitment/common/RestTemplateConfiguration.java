package com.github.recruitment.common;

import com.github.recruitment.common.proportiest.ExternalServiceCredentials;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate createOKCustomRestTemplate( ClientHttpRequestFactory clientHttpRequestFactory, RestTemplateBuilder restTemplateBuilder ) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setRequestFactory( clientHttpRequestFactory );
        return restTemplate;
    }

    @Bean
    @ConfigurationProperties ( "github.credentials" )
    public ExternalServiceCredentials getExternalServiceCredentials() {
        return new ExternalServiceCredentials();
    }

    @Bean
    public RestTemplateBuilder getRestTemplateBuilder( ExternalServiceCredentials externalServiceCredentials ) {
        return new RestTemplateBuilder().basicAuthentication( externalServiceCredentials.getUsername(),externalServiceCredentials.getPassword() );
    }
}
