package vn.cloud.cardservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gmaps")
public record SecretsConfig(String apiURL) {
}
