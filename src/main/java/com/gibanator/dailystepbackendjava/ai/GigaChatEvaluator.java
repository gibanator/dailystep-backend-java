package com.gibanator.dailystepbackendjava.ai;

import chat.giga.springai.GigaChatModel;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvalResult;
import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Реализация оценки дня через GigaChat (Sber) поверх Spring AI.
 *
 * <p>С Фазы 2 в контексте несколько моделей (GigaChat + вручную собранные Claude/Qwen/DeepSeek),
 * поэтому внедряем КОНКРЕТНЫЙ тип {@link GigaChatModel} (бин от стартера
 * {@code chat.giga:spring-ai-starter-model-gigachat}), а не абстрактный {@code ChatModel} —
 * иначе внедрение было бы неоднозначным. См. документ «08 — Мультипровайдерность».
 */
@Component
public class GigaChatEvaluator implements DayEvaluator {

    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;

    public GigaChatEvaluator(GigaChatModel gigaChatModel, PromptBuilder promptBuilder) {
        this.chatClient = ChatClient.create(gigaChatModel);
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
