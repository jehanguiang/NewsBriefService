package com.jehan.newsBriefService.service;

import com.jehan.newsBriefService.client.NewsApiClient;
import com.jehan.newsBriefService.client.OllamaClient;
import com.jehan.newsBriefService.dto.NewsApiResponse;
import com.jehan.newsBriefService.dto.NewsSummaryResponse;
import com.jehan.newsBriefService.dto.OllamaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewsBriefService {

    private final NewsApiClient newsApiClient;
    private final OllamaClient ollamaClient;

    @Autowired
    public NewsBriefService(NewsApiClient newsApiClient, OllamaClient ollamaClient) {
        this.newsApiClient = newsApiClient;
        this.ollamaClient = ollamaClient;
    }

    @Cacheable(value = "newsBrief", key = "#root.method.name")
    public NewsSummaryResponse generateGeneralNewsBrief(Boolean toRender) {
        final NewsApiResponse newsApiResponse = newsApiClient.getTopHeadLines();
        log.info("Fetched top headlines: {}", newsApiResponse);

        final OllamaResponse ollamaResponse =
                ollamaClient.generateSummary(newsApiResponse.getArticles(), toRender);

        return NewsSummaryResponse.builder()
                .createdAt(java.time.LocalDateTime.now())
                .summary(ollamaResponse.getResponse())
                .build();
    }
}
