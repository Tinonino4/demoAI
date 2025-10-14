package com.tinonio.ai.demoAI.advisors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinonio.ai.demoAI.exception.ValidationException;
import com.tinonio.ai.demoAI.model.AnalysisResponse;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.stereotype.Component;

@Component
public class JsonGuardAdvisor implements CallAdvisor {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        // 1) execute the rest of the chain
        ChatClientResponse response = chain.nextCall(request);

        // 2) validate the response content is valid JSON of the expected schema
        String content = response.chatResponse().getResult().getOutput().getText();

        AnalysisResponse dto;
        try {
            dto = mapper.readValue(content, AnalysisResponse.class);
        } catch (Exception e) {
            throw new ValidationException("Model response is not valid JSON");
        }

        if (dto.getSentiment() == null) {
            throw new ValidationException("Sentiment is empty.");
        }
        if (dto.getSeverity() == null) {
            throw new ValidationException("Severity is empty.");
        }
        if (dto.getSummary() == null || dto.getSummary().isBlank()) {
            throw new ValidationException("Summary is empty.");
        }
        return response;
    }

    @Override
    public String getName() {
        return "json-guard";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
