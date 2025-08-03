package com.mailGenerator.mailGenerator.service;

import com.mailGenerator.mailGenerator.model.ReplyRequest;
import com.mailGenerator.mailGenerator.model.ReplyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final String geminiApiKey;

    /**
     * Constructs the GeminiService with a WebClient and the API key from application.properties.
     * The @Value annotation injects the properties from the file.
     *
     * @param webClientBuilder The WebClient.Builder instance.
     * @param geminiApiKey The Gemini API key from the configuration.
     * @param geminiApiUrl The Gemini API URL from the configuration.
     */
    public GeminiService(WebClient.Builder webClientBuilder,
                         @Value("${gemini.api.key}") String geminiApiKey,
                         @Value("${gemini.api.url}") String geminiApiUrl) {
        this.geminiApiKey = geminiApiKey;
        this.webClient = webClientBuilder.baseUrl(geminiApiUrl).build();
    }

    /**
     * Generates a conversational response to an email using the Gemini API, with a specified tone.
     * The method constructs a prompt and makes an asynchronous, non-blocking POST request.
     *
     * @param request The request containing the email body and the desired tone.
     * @return A Mono emitting the generated email reply.
     */
    public Mono<ReplyResponse> generateReply(ReplyRequest request) {
        // Construct a new, more specific prompt for the Gemini API.
        // This prompt instructs the model to provide a single, direct reply without options.
        String prompt = String.format(
                "Generate one single, concise, and %s reply for the following email. Do not provide any options or introductory phrases. Just provide the final response text.\n\nBody: %s",
                request.getTone(),
                request.getBody()
        );

        // Build the request body for the Gemini API.
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", Collections.singletonList(part));

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", Collections.singletonList(content));

        // Use WebClient to make the POST request.
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", geminiApiKey).build())
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(Map.class) // The response is a Map
                .map(response -> {
                    // Parse the nested JSON response to get the generated text.
                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                    if (candidates != null && !candidates.isEmpty()) {
                        Map<String, Object> candidate = candidates.get(0);
                        Map<String, Object> contentMap = (Map<String, Object>) candidate.get("content");
                        List<Map<String, String>> parts = (List<Map<String, String>>) contentMap.get("parts");
                        if (parts != null && !parts.isEmpty()) {
                            String replyText = parts.get(0).get("text");
                            return new ReplyResponse(replyText);
                        }
                    }
                    // Return an empty response if parsing fails
                    return new ReplyResponse("Could not generate a reply.");
                })
                .doOnError(e -> System.err.println("Error calling Gemini API: " + e.getMessage()));
    }
}
