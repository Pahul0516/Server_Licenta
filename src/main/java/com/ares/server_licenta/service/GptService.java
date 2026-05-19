package com.ares.server_licenta.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class GptService {

    private final ChatModel chatModel;

    public GptService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generateNarrative(String timelineJson) {
        // 1. Define the "Rules" for GPT (System Prompt)
        String systemInstructions = """
    You are an empathetic Memory Recovery Assistant. I will provide a JSON timeline 
    of detected locations, people, and objects from a user's day. 
    
    Your mission is to help a user with amnesia reconstruct their day by following 
    these specific guidelines:
    
    1. ORIENTATION: Begin by stating the overall 'theme' of the day (e.g., 'Today was a 
       quiet day at home' or 'You had a busy morning in the city').
    2. CHRONOLOGICAL CLARITY: Use clear time markers (e.g., 'At around 9:00 AM...') 
       to help the user follow the sequence of events.
    3. ACTIVITY INFERENCE: Instead of just listing objects, describe the actions they 
       suggest. (e.g., instead of 'sink and toothbrush,' say 'You spent some time 
       getting ready in the bathroom').
    4. EMPHASIZE PEOPLE: Highlight any specific names or 'Unknown' persons to help 
       the user recall social interactions.
    5. NOISE REDUCTION: Filter out technical detections that don't make sense 
       chronologically so the user doesn't get confused by 'hallucinated' data.
    6. TONE: Be supportive, gentle, and use the second person ('You'). Use a calm, 
       grounded 'vibe' to reduce the user's anxiety about their memory loss.
    """;

        Message systemMessage = new SystemPromptTemplate(systemInstructions).createMessage();

        // 2. Provide the Data (User Message)
        Message userMessage = new UserMessage("Here is the timeline data: " + timelineJson);

        // 3. Call the Model
        return chatModel.call(new Prompt(List.of(systemMessage, userMessage)))
                .getResult()
                .getOutput()
                .getText();
    }
}