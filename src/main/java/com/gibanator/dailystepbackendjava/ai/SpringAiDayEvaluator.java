package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;

/**
 * Универсальная реализация {@link DayEvaluator} поверх любого Spring AI {@link ChatModel}.
 * Логика оценки у всех провайдеров одинаковая (единый промпт + structured output через Spring AI),
 * поэтому под каждый провайдер не нужен свой класс — достаточно передать нужную модель и enum.
 *
 * <p>Экземпляры создаются в {@link AiEvaluatorsConfig} по одному на провайдер и только при наличии
 * ключа. GigaChat живёт отдельно ({@link GigaChatEvaluator}), т.к. его модель приходит из autoconfig.
 */
public class SpringAiDayEvaluator implements DayEvaluator {

    private final AiProvider provider;
    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;

    public SpringAiDayEvaluator(AiProvider provider, ChatModel chatModel, PromptBuilder promptBuilder) {
        this.provider = provider;
        this.chatClient = ChatClient.create(chatModel);
        this.promptBuilder = promptBuilder;
    }

    @Override
    public AiProvider provider() {
        return provider;
    }

    @Override
    public AiEvalResult evaluate(AiEvaluateRequest req) {
        return chatClient.prompt()
                .system(PromptBuilder.SYSTEM)
                .user(promptBuilder.buildUserMessage(req))
                .call()
                .entity(AiEvalResult.class);
    }
}
