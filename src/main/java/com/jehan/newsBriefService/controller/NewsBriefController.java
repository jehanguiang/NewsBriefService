package com.jehan.newsBriefService.controller;

import com.jehan.newsBriefService.service.NewsBriefService;
import com.jehan.newsBriefService.dto.NewsSummaryResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news-brief")
public class NewsBriefController {
    private final NewsBriefService newsBriefService;

    public NewsBriefController(NewsBriefService newsBriefService) {
        this.newsBriefService = newsBriefService;
    }

    @GetMapping(value = "/general-brief", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewsSummaryResponse generalBrief() {
        return newsBriefService.generateGeneralNewsBrief(false);
    }

    @GetMapping(value = "/general-brief/render", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> generalBriefUI() {
        final NewsSummaryResponse newsSummaryResponse = newsBriefService.generateGeneralNewsBrief(true);
        String summary = newsSummaryResponse.getSummary();

        Pattern pattern = Pattern.compile("```html(.*?)```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(summary);

        final String htmlContent;
        if (matcher.find()) {
            htmlContent = matcher.group(1).trim();
        } else {
            htmlContent = summary.trim();
        }

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlContent);
    }
}
