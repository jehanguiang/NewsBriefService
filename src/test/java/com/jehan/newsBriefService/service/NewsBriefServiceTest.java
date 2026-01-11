package com.jehan.newsBriefService.service;

import com.jehan.newsBriefService.client.NewsApiClient;
import com.jehan.newsBriefService.client.OllamaClient;
import com.jehan.newsBriefService.dto.Article;
import com.jehan.newsBriefService.dto.NewsApiResponse;
import com.jehan.newsBriefService.dto.NewsSummaryResponse;
import com.jehan.newsBriefService.dto.OllamaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsBriefServiceTest {

    @Mock
    private NewsApiClient newsApiClient;

    @Mock
    private OllamaClient ollamaClient;

    @InjectMocks
    private NewsBriefService newsBriefService;

    private NewsApiResponse mockNewsApiResponse;
    private OllamaResponse mockOllamaResponse;
    private List<Article> mockArticles;

    @BeforeEach
    void setUp() {
        mockArticles = Arrays.asList(
                Article.builder()
                        .title("Test Article 1")
                        .description("Description 1")
                        .content("Content 1")
                        .build(),
                Article.builder()
                        .title("Test Article 2")
                        .description("Description 2")
                        .content("Content 2")
                        .build()
        );

        mockNewsApiResponse = NewsApiResponse.builder()
                .totalResults(2)
                .articles(mockArticles)
                .build();

        mockOllamaResponse = OllamaResponse.builder()
                .response("This is an AI-generated summary of today's news.")
                .done(true)
                .build();
    }

    @Test
    void generateGeneralNewsBrief_ShouldReturnSummaryResponse_WhenToRenderIsFalse() {
        when(newsApiClient.getTopHeadLines()).thenReturn(mockNewsApiResponse);
        when(ollamaClient.generateSummary(eq(mockArticles), eq(false))).thenReturn(mockOllamaResponse);

        NewsSummaryResponse result = newsBriefService.generateGeneralNewsBrief(false);

        assertNotNull(result);
        assertEquals("This is an AI-generated summary of today's news.", result.getSummary());
        assertNotNull(result.getCreatedAt());
        verify(newsApiClient, times(1)).getTopHeadLines();
        verify(ollamaClient, times(1)).generateSummary(mockArticles, false);
    }

    @Test
    void generateGeneralNewsBrief_ShouldReturnHtmlResponse_WhenToRenderIsTrue() {
        OllamaResponse htmlResponse = OllamaResponse.builder()
                .response("<html><body><h1>News Summary</h1></body></html>")
                .done(true)
                .build();

        when(newsApiClient.getTopHeadLines()).thenReturn(mockNewsApiResponse);
        when(ollamaClient.generateSummary(eq(mockArticles), eq(true))).thenReturn(htmlResponse);

        NewsSummaryResponse result = newsBriefService.generateGeneralNewsBrief(true);

        assertNotNull(result);
        assertTrue(result.getSummary().contains("<html>"));
        verify(ollamaClient, times(1)).generateSummary(mockArticles, true);
    }

    @Test
    void generateGeneralNewsBrief_ShouldSetCreatedAtTimestamp() {
        when(newsApiClient.getTopHeadLines()).thenReturn(mockNewsApiResponse);
        when(ollamaClient.generateSummary(any(), any())).thenReturn(mockOllamaResponse);

        NewsSummaryResponse result = newsBriefService.generateGeneralNewsBrief(false);

        assertNotNull(result.getCreatedAt());
    }

    @Test
    void generateGeneralNewsBrief_ShouldCallNewsApiClientFirst() {
        when(newsApiClient.getTopHeadLines()).thenReturn(mockNewsApiResponse);
        when(ollamaClient.generateSummary(any(), any())).thenReturn(mockOllamaResponse);

        newsBriefService.generateGeneralNewsBrief(false);

        var inOrder = inOrder(newsApiClient, ollamaClient);
        inOrder.verify(newsApiClient).getTopHeadLines();
        inOrder.verify(ollamaClient).generateSummary(any(), any());
    }
}
