package com.brianlukonsolo.Controller;

import com.brianlukonsolo.services.OllamaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RagChatController {

    private final OllamaService service;

    public RagChatController(OllamaService service) {
        this.service = service;
    }

    // Request DTO
    public record ProductQueryRequest(
            @NotBlank(message = "query must not be blank")
            String query
    ) {}

    // Response DTO
    public record ProductQueryResponse(String query, String answer) {}

    @PostMapping(
            value = "/chat-rag",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ProductQueryResponse productData(@Valid @RequestBody ProductQueryRequest req) {
        String answer = service.generateAnswerWithRetrievalAugmentedGeneration(req.query());
        return new ProductQueryResponse(req.query(), answer);
    }
}
