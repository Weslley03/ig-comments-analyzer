package com.ig.comments.web.scraping.demo.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.ig.comments.web.scraping.demo.dto.OpenRouterDTO.OpenRouterResponse;
import com.ig.comments.web.scraping.demo.exception.AiServiceException;

@Component
public class OpenRouterClient {

    private static final Logger log = LoggerFactory.getLogger(OpenRouterClient.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String sendPrompt(String prompt) {
        String url = "https://openrouter.ai/api/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.add("HTTP-Referer", "https://ig-comments-web-scraping-demo.com");
        headers.add("X-OpenRouter-Title", "ig-comments-web-scraping-demo");

        Map<String, Object> body = new HashMap<>();
        body.put("model", "openai/gpt-4o-mini");
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<OpenRouterResponse> response = restTemplate.postForEntity(url, request, OpenRouterResponse.class);

            if (response.getBody() == null) {
                throw new AiServiceException("o serviço de ia retornou uma resposta vazia.");
            }

            List<?> choices = response.getBody().choices();
            if (choices == null || choices.isEmpty()) {
                throw new AiServiceException("o serviço de ia não retornou nenhuma resposta de completação.");
            }

            return response.getBody().choices().get(0).message().content();

        } catch (HttpClientErrorException e) {
            int status = e.getStatusCode().value();
            if (status == 401) {
                log.error("authentication failed on openrouter. please check the api key.");
                throw new AiServiceException("falha na autenticação com o serviço de ia. verifique a configuração da api.", e);
            }
            if (status == 429) {
                log.warn("openrouter's request limit has been reached.");
                throw new AiServiceException("limite de requisições do serviço de ia atingido. tente novamente em alguns instantes.", e);
            }
            log.error("openrouter client error ({}): {}", status, e.getMessage(), e);
            throw new AiServiceException("o serviço de ia retornou um erro: " + status + ".", e);

        } catch (HttpServerErrorException e) {
            log.error("openrouter internal error ({}): {}", e.getStatusCode().value(), e.getMessage(), e);
            throw new AiServiceException("o serviço de ia está temporariamente indisponível. tente novamente mais tarde.", e);

        } catch (ResourceAccessException e) {
            log.error("unable to connect to openrouter: {}", e.getMessage(), e);
            throw new AiServiceException("não foi possível conectar ao serviço de ia. Verifique a conectividade de rede.", e);

        } catch (AiServiceException e) {
            throw e;

        } catch (Exception e) {
            log.error("unexpected error when calling openrouter: {}", e.getMessage(), e);
            throw new AiServiceException("erro inesperado ao comunicar com o serviço de ia.", e);
        }
    }
}
