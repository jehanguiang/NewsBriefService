package com.jehan.newsBriefService.controller;

import com.jehan.newsBriefService.dto.NewsSummaryResponse;
import com.jehan.newsBriefService.service.NewsBriefService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsBriefControllerTest {

    @Mock
    private NewsBriefService newsBriefService;

    @InjectMocks
    private NewsBriefController newsBriefController;

    private NewsSummaryResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = NewsSummaryResponse.builder()
                .summary("Test summary content")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void generalBrief_ShouldReturnNewsSummaryResponse() {
        when(newsBriefService.generateGeneralNewsBrief(false)).thenReturn(mockResponse);

        NewsSummaryResponse result = newsBriefController.generalBrief();

        assertNotNull(result);
        assertEquals("Test summary content", result.getSummary());
        verify(newsBriefService, times(1)).generateGeneralNewsBrief(false);
    }

    @Test
    void generalBriefUI_ShouldReturnHtmlContent_WhenNoMarkdownBlock() {
        NewsSummaryResponse htmlResponse = NewsSummaryResponse.builder()
                .summary("<html><body><h1>News</h1></body></html>")
                .createdAt(LocalDateTime.now())
                .build();

        when(newsBriefService.generateGeneralNewsBrief(true)).thenReturn(htmlResponse);

        ResponseEntity<String> result = newsBriefController.generalBriefUI();

        assertNotNull(result);
        assertEquals(MediaType.TEXT_HTML, result.getHeaders().getContentType());
        assertEquals("<html><body><h1>News</h1></body></html>", result.getBody());
        verify(newsBriefService, times(1)).generateGeneralNewsBrief(true);
    }

    @Test
    void generalBriefUI_ShouldExtractHtmlFromMarkdownBlock() {
        String markdownWrappedHtml = "Here is the HTML:\n```html\n<html><body><h1>News</h1></body></html>\n```";
        NewsSummaryResponse htmlResponse = NewsSummaryResponse.builder()
                .summary(markdownWrappedHtml)
                .createdAt(LocalDateTime.now())
                .build();

        when(newsBriefService.generateGeneralNewsBrief(true)).thenReturn(htmlResponse);

        ResponseEntity<String> result = newsBriefController.generalBriefUI();

        assertNotNull(result);
        assertEquals(MediaType.TEXT_HTML, result.getHeaders().getContentType());
        assertEquals("<html><body><h1>News</h1></body></html>", result.getBody());
    }

    @Test
    void generalBriefUI_ShouldHandleMultilineHtmlInMarkdownBlock() {
        String markdownWrappedHtml = "```html\n<!DOCTYPE html>\n<html>\n<head><title>News</title></head>\n<body>\n<h1>Headlines</h1>\n</body>\n</html>\n```";
        NewsSummaryResponse htmlResponse = NewsSummaryResponse.builder()
                .summary(markdownWrappedHtml)
                .createdAt(LocalDateTime.now())
                .build();

        when(newsBriefService.generateGeneralNewsBrief(true)).thenReturn(htmlResponse);

        ResponseEntity<String> result = newsBriefController.generalBriefUI();

        assertNotNull(result);
        assertTrue(result.getBody().contains("<!DOCTYPE html>"));
        assertTrue(result.getBody().contains("<h1>Headlines</h1>"));
        assertFalse(result.getBody().contains("```"));
    }
}
