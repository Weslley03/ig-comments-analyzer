package com.ig.comments.web.scraping.demo.dto;

import java.util.List;

public class OpenRouterDTO {
    public record OpenRouterResponse(
            List<Choice> choices) {
    }

    public record Choice(
            Message message) {
    }

    public record Message(
            String content) {
    }
}
