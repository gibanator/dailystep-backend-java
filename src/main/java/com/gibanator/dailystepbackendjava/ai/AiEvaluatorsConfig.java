package com.gibanator.dailystepbackendjava.ai;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ручная сборка провайдеров, у которых Spring AI autoconfig отключён
 * (см. {@code spring.ai.model.chat=gigachat} в application.yaml): Claude, Qwen, DeepSeek.
 *
 * <p>Каждый бин-эвалюатор поднимается ТОЛЬКО если задан соответствующий ключ
 * ({@code @ConditionalOnExpression} по env-переменной). Без ключа бин не создаётся,
 * провайдер не попадает в {@code GET /api/v1/ai/models} и запрос на него вернёт 400.
 * Это позволяет добавлять ключи по одному, не трогая код.
 *
 * <p>Модели строят HTTP-клиент из ключа прямо в конструкторе, поэтому создаём их только под
 * гейтом ключа — иначе падение на старте. Qwen и DeepSeek оба OpenAI-совместимые: одна
 * реализация {@link OpenAiChatModel}, отличаются только base-url, ключом и именем модели.
 */
@Configuration
public class AiEvaluatorsConfig {

    // ---- Claude (Anthropic) ----

    @Bean
    @ConditionalOnExpression("'${CLAUDE_API_KEY:}' != ''")
    public DayEvaluator claudeEvaluator(
            PromptBuilder promptBuilder,
            @Value("${CLAUDE_API_KEY}") String apiKey,
            @Value("${CLAUDE_MODEL:claude-haiku-4-5}") String model,
            @Value("${CLAUDE_BASE_URL:}") String baseUrl) {

        AnthropicChatOptions.Builder options = AnthropicChatOptions.builder()
                .apiKey(apiKey)
                .model(model)
                .temperature(0.3)
                .maxTokens(1024);
        if (baseUrl != null && !baseUrl.isBlank()) {
            options.baseUrl(baseUrl);
        }

        AnthropicChatModel chatModel = AnthropicChatModel.builder()
                .options(options.build())
                .build();
        return new SpringAiDayEvaluator(AiProvider.CLAUDE, chatModel, promptBuilder);
    }

    // ---- Qwen (Alibaba DashScope, OpenAI-совместимый) ----

    @Bean
    @ConditionalOnExpression("'${QWEN_API_KEY:}' != ''")
    public DayEvaluator qwenEvaluator(
            PromptBuilder promptBuilder,
            @Value("${QWEN_API_KEY}") String apiKey,
            @Value("${QWEN_BASE_URL:https://dashscope-intl.aliyuncs.com/compatible-mode}") String baseUrl,
            @Value("${QWEN_MODEL:qwen-plus}") String model) {
        return new SpringAiDayEvaluator(
                AiProvider.QWEN, openAiCompatibleModel(apiKey, baseUrl, model), promptBuilder);
    }

    // ---- DeepSeek (OpenAI-совместимый) ----

    @Bean
    @ConditionalOnExpression("'${DEEPSEEK_API_KEY:}' != ''")
    public DayEvaluator deepSeekEvaluator(
            PromptBuilder promptBuilder,
            @Value("${DEEPSEEK_API_KEY}") String apiKey,
            @Value("${DEEPSEEK_BASE_URL:https://api.deepseek.com}") String baseUrl,
            @Value("${DEEPSEEK_MODEL:deepseek-chat}") String model) {
        return new SpringAiDayEvaluator(
                AiProvider.DEEPSEEK, openAiCompatibleModel(apiKey, baseUrl, model), promptBuilder);
    }

    /** Общая сборка OpenAI-совместимой модели (Qwen/DeepSeek отличаются только параметрами). */
    private OpenAiChatModel openAiCompatibleModel(String apiKey, String baseUrl, String model) {
        OpenAiApi api = OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(new SimpleApiKey(apiKey))
                .build();
        return OpenAiChatModel.builder()
                .openAiApi(api)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(model)
                        .temperature(0.3)
                        .maxTokens(1024)
                        .build())
                .build();
    }
}
