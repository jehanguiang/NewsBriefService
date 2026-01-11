package com.jehan.newsBriefService.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void article_ShouldBuildCorrectly() {
        Article article = Article.builder()
                .title("Test Title")
                .description("Test Description")
                .content("Test Content")
                .build();

        assertEquals("Test Title", article.getTitle());
        assertEquals("Test Description", article.getDescription());
        assertEquals("Test Content", article.getContent());
    }

    @Test
    void newsApiResponse_ShouldContainArticles() {
        List<Article> articles = Arrays.asList(
                Article.builder().title("Article 1").build(),
                Article.builder().title("Article 2").build()
        );

        NewsApiResponse response = NewsApiResponse.builder()
                .totalResults(2)
                .articles(articles)
                .build();

        assertEquals(2, response.getTotalResults());
        assertEquals(2, response.getArticles().size());
        assertEquals("Article 1", response.getArticles().get(0).getTitle());
    }

    @Test
    void newsSummaryResponse_ShouldBuildWithTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        NewsSummaryResponse response = NewsSummaryResponse.builder()
                .summary("Test summary")
                .createdAt(now)
                .build();

        assertEquals("Test summary", response.getSummary());
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    void ollamaRequest_ShouldBuildCorrectly() {
        OllamaRequest request = OllamaRequest.builder()
                .model("mistral:7b")
                .prompt("Test prompt")
                .stream(false)
                .build();

        assertEquals("mistral:7b", request.getModel());
        assertEquals("Test prompt", request.getPrompt());
        assertFalse(request.isStream());
    }

    @Test
    void ollamaResponse_ShouldBuildCorrectly() {
        OllamaResponse response = OllamaResponse.builder()
                .response("Generated response")
                .done(true)
                .build();

        assertEquals("Generated response", response.getResponse());
        assertTrue(response.isDone());
    }

    @Test
    void article_ShouldSupportEqualsAndHashCode() {
        Article article1 = Article.builder()
                .title("Same Title")
                .description("Same Description")
                .build();

        Article article2 = Article.builder()
                .title("Same Title")
                .description("Same Description")
                .build();

        assertEquals(article1, article2);
        assertEquals(article1.hashCode(), article2.hashCode());
    }

    @Test
    void newsSummaryResponse_ShouldSupportNoArgsConstructor() {
        NewsSummaryResponse response = new NewsSummaryResponse();
        assertNull(response.getSummary());
        assertNull(response.getCreatedAt());

        response.setSummary("Updated summary");
        assertEquals("Updated summary", response.getSummary());
    }
}
