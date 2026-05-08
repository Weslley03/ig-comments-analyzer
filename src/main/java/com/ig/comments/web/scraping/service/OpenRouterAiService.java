package com.ig.comments.web.scraping.service;

import org.springframework.stereotype.Service;

import com.ig.comments.web.scraping.client.OpenRouterClient;
import com.ig.comments.web.scraping.dto.InstagramPostDataDTO;

@Service
public class OpenRouterAiService {
private final OpenRouterClient client;

    public OpenRouterAiService(OpenRouterClient client) {
        this.client = client;
    }

    public String analyzeInstagramPost(InstagramPostDataDTO postData) {
        String prompt = buildPrompt(postData);
        return client.sendPrompt(prompt);
    }

    private String buildPrompt(InstagramPostDataDTO postData) {
        String comments = String.join("\n- ", postData.comments());

        return """
                descrição da publicação:
                %s

                comentários coletados:
                - %s

                analise os comentários como especialista em comportamento digital e percepção pública.

                instruções importantes:
                - priorize o que é predominante nos comentários.
                - não force equilíbrio entre elogios e críticas.
                - se elogios forem raros, informe isso.
                - se críticas forem raras, informe isso.
                - diferencie volume, intensidade e recorrência.
                - Ignore spam, piadas soltas e comentários irrelevantes.

                gere resposta objetiva contendo:

                1. sentimento predominante do público (positivo, negativo, misto ou neutro)
                2. principais percepções recorrentes
                3. críticas relevantes (somente se existirem)
                4. elogios relevantes (somente se existirem)
                5. nível de engajamento percebido (baixo/médio/alto)
                6. conclusão estratégica fiel ao conjunto dos comentários

                responda em português claro e profissional.
                """.formatted(
                postData.description(),
                comments);
    }
}
