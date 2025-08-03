package com.mailGenerator.mailGenerator.controller;




import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.mailGenerator.mailGenerator.service.GeminiService;
import com.mailGenerator.mailGenerator.model.ReplyRequest;
import com.mailGenerator.mailGenerator.model.ReplyResponse;

@RestController
@RequestMapping("/api")
public class EmailController {

    private final GeminiService geminiService;

    /**
     * Constructs the EmailController with an instance of GeminiService.
     * Spring will automatically inject the dependency.
     *
     * @param geminiService The service for interacting with the Gemini API.
     */
    public EmailController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * REST endpoint to generate a reply for a given email.
     * This endpoint is non-blocking and reactive, using Mono for the response.
     *
     * @param request The request body containing the email body and desired tone.
     * @return A Mono emitting a ResponseEntity with the generated reply.
     */
    @PostMapping("/generate-reply")
    public Mono<ResponseEntity<ReplyResponse>> generateReply(@RequestBody ReplyRequest request) {
        // Call the service to generate the reply and wrap the result in a ResponseEntity.
        return geminiService.generateReply(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

