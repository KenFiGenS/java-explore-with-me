package ru.practicum;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statsDto.StatsDtoCreate;

import java.util.List;


public class HitClient {
    private static final String API_PREFIX = "/hit";
    private RestTemplate restTemplate;

    public HitClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    @SneakyThrows
    public StatsDtoCreate createHit(StatsDtoCreate statsDtoCreate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsDtoCreate> entityReq = new HttpEntity<>(statsDtoCreate, headers);
        try {
            return restTemplate.exchange("", HttpMethod.POST, entityReq, StatsDtoCreate.class).getBody();
        } catch (HttpStatusCodeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
