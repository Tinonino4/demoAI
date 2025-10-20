package com.tinonio.ai.demoAI.config;

import com.tinonio.ai.demoAI.advisors.JsonGuardAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Bean
    ChatClient chatClient(ChatModel model, JsonGuardAdvisor jsonGuardAdvisor) {
        return ChatClient
                .builder(model)
                //.defaultAdvisors(jsonGuardAdvisor)
                .build();
    }
}
