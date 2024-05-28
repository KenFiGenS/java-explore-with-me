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
import ru.practicum.statsDto.StatsDtoWithHitsCount;

import java.time.LocalDateTime;
import java.util.List;

public class StatsClient {
    private static final String API_PREFIX = "/stats";
    private RestTemplate restTemplate;

    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    @SneakyThrows
    public List<StatsDtoWithHitsCount> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String url;
        if (uris.isEmpty() && unique == null) {
            url = "?start=" + start + "&end=" + end;
        } else if (!uris.isEmpty() && unique == null) {
            url = "?start=" + start + "&end=" + end + "&uris=" + uris;
        } else if (!uris.isEmpty()) {
            url = "?start=" + start + "&end=" + end + "&unique=" + unique;
        } else {
            url = "?start=" + start + "&end=" + end + "&uris=" + uris + "&unique=" + unique;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsDtoCreate> entityReq = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(url, HttpMethod.GET, entityReq, List.class).getBody();
        } catch (HttpStatusCodeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
