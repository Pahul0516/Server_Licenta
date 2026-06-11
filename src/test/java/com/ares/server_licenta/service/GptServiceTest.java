package com.ares.server_licenta.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GptServiceTest {

    @Mock
    private ChatModel chatModel;

    @InjectMocks
    private GptService gptService;

    @Test
    void generateNarrative_ShouldReturnNarrativeFromChatModel() {
        // Arrange
        String timelineJson = "{\"events\": []}";
        String expectedNarrative = "Today was a good day.";
        
        ChatResponse chatResponse = mock(ChatResponse.class);
        Generation generation = mock(Generation.class);
        AssistantMessage assistantMessage = new AssistantMessage(expectedNarrative);
        
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);
        when(chatResponse.getResult()).thenReturn(generation);
        when(generation.getOutput()).thenReturn(assistantMessage);

        // Act
        String result = gptService.generateNarrative(timelineJson);

        // Assert
        assertEquals(expectedNarrative, result);
    }
}
