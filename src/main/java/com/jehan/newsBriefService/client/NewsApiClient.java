package com.jehan.newsBriefService.client;

import com.jehan.newsBriefService.dto.NewsApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class NewsApiClient {

    @Value("${news.api.api-key}")
    private String newsApiKey;

    @Value("${news.api.base-url}")
    private String baseUrl;

    @Value("${news.api.default-country}")
    private String defaultCountry;

    @Value("${news.api.top-headlines-endpoint}")
    private String topHeadlinesEndpoint;

    public NewsApiResponse getTopHeadLines() {
        final RestTemplate restTemplate = new RestTemplate();

        final String url = getTopHeadLinesUrl();

        final NewsApiResponse newsApiResponse = restTemplate.getForObject(url, NewsApiResponse.class);

        log.info("Fetched top headlines: {}", newsApiResponse);

        return newsApiResponse;
    }

    private String getTopHeadLinesUrl() {
     final String baseUrl = this.baseUrl + this.topHeadlinesEndpoint;

     final String urlWithParams = UriComponentsBuilder.fromHttpUrl(baseUrl)
             .queryParam("country", defaultCountry)
             .queryParam("apiKey", newsApiKey)
             .toUriString();

     log.info("Fetching top headlines from URL: {}", urlWithParams);

     return urlWithParams;
    }
}
