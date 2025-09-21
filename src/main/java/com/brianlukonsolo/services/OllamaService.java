package com.brianlukonsolo.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    private final String modelName;

    public OllamaService(
            VectorStore vectorStore,
            ChatClient.Builder builder,
            @Value("${spring.ai.ollama.chat.options.model:mistral}")
            String modelName
    ) {
        this.vectorStore = vectorStore;
        this.modelName = modelName;
        // no memory (RAG style). For memory: new MessageChatMemoryAdvisor(new InMemoryChatMemory())
        this.chatClient = builder.defaultAdvisors().build();
    }

    public ChatResponse generateAnswer(String question) {
        return chatClient
                .prompt(new Prompt(
                        question,
                        OllamaOptions.builder()
                                .withModel(modelName)
                                .withTemperature(0.4)
                                .build()
                ))
                .call()
                .chatResponse();
    }

    public String generateAnswerWithRetrievalAugmentedGeneration(String question) {
        return chatClient
                .prompt(new Prompt(
                        question,
                        OllamaOptions.builder()
                                .withModel(modelName)
                                .withTemperature(0.4)
                                .build()
                ))
                .system("You use the provided context to answer questions. You respond with Im sorry no context is provided if the information is not in the context")
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .content();
    }
}
