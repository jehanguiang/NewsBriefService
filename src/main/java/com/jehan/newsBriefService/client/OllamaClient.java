package com.jehan.newsBriefService.client;

import com.jehan.newsBriefService.dto.Article;
import com.jehan.newsBriefService.dto.OllamaRequest;
import com.jehan.newsBriefService.dto.OllamaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class OllamaClient {
    @Value("${ollama.base-url}")
    private String ollamaUrl;

    @Value("${ollama.mistral-model}")
    private String aiModel;

    public OllamaResponse generateSummary(List<Article> articles, Boolean toRender) {

        final RestTemplate restTemplate = new RestTemplate();

        final String prompt = getPrompt(articles, toRender);

        final OllamaRequest requestPayload = OllamaRequest.builder()
                .model(aiModel)
                .prompt(prompt)
                .stream(false)
                .build();

        final HttpEntity<OllamaRequest> requestEntity = getHttpEntity(requestPayload);

        final ResponseEntity<OllamaResponse> ollamaResponse = restTemplate.postForEntity(ollamaUrl, requestEntity, OllamaResponse.class);

        log.info("Ollama response: {}", ollamaResponse);
        return ollamaResponse.getBody();
    }

    private static HttpEntity<OllamaRequest> getHttpEntity(OllamaRequest requestPayload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestPayload, headers);
    }

    private String getPrompt(List<Article> articles, Boolean toRender) {
        final StringBuilder promptBuilder = new StringBuilder();

        if(toRender) {
            promptBuilder.append("You are an expert HTML generator. " +
                    "Summarize the following news articles and return only the full HTML code for a complete webpage. " +
                    "Make the page look clean and readable using inline CSS or basic embedded styles. " +
                    "Do NOT include any explanations, introductions, or markdown formatting -- " +
                    "only raw HTML code that can be copied and run in a browser. Do NOT use ```html code blocks.");

        } else {
            promptBuilder.append("You are a news summarizer." +
                    "Summarize the top global news stories from today in a consise and informative way." +
                    "Focus on major events, political developments, economic updates, and major technology or " +
                    "health developments. Keep the summary clear, objective and easy to read like a daily news brief.");

        }

        for (Article article : articles) {
            promptBuilder.append("Title: ").append(article.getTitle()).append("\n");
            promptBuilder.append("Description: ").append(article.getDescription()).append("\n");
            promptBuilder.append("End of article\n\n ");
        }

        return promptBuilder.toString();
    }
}
