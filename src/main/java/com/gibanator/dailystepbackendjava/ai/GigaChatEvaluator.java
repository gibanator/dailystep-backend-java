package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

/**
 * Реализация оценки дня через GigaChat (Sber) поверх Spring AI.
 *
 * <p>Сейчас в контексте единственный {@link ChatModel} (его создаёт стартер
 * {@code chat.giga:spring-ai-starter-model-gigachat}), поэтому внедряем его напрямую.
 * Когда добавим Claude/Qwen/DeepSeek, моделей станет несколько — тогда здесь нужно будет
 * внедрять КОНКРЕТНЫЙ бин (через {@code @Qualifier} или типизированный класс модели),
 * иначе внедрение {@link ChatModel} станет неоднозначным. См. документ «08 — Мультипровайдерность».
 */
@Component
public class GigaChatEvaluator implements DayEvaluator {

    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;

    public GigaChatEvaluator(ChatModel chatModel, PromptBuilder promptBuilder) {
        this.chatClient = ChatClient.create(chatModel);
        this.promptBuilder = promptBuilder;
    }

    @Override
    public AiProvider provider() {
        return AiProvider.GIGACHAT;
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
