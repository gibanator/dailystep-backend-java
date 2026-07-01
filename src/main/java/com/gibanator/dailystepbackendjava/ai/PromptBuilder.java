package com.gibanator.dailystepbackendjava.ai;

import com.gibanator.dailystepbackendjava.ai.dto.AiEvaluateRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Единый построитель промпта для ВСЕХ провайдеров. System-инструкция одинаковая,
 * user-сообщение собирается из текста дня + списков категорий и целей.
 * Саму JSON-схему ответа в промпт добавлять не нужно — это делает Spring AI
 * при {@code .entity(AiEvalResult.class)}.
 */
@Component
public class PromptBuilder {

    public static final String SYSTEM = """
            Ты — помощник в приложении-трекере жизни. Пользователь описывает, как прошёл его день.
            Тебе дают список его категорий и список его целей. Определи по тексту, что выполнено сегодня.
            Для каждой категории верни completed (true/false) и короткий comment (до 100 символов или пусто).
            Для каждой цели верни completed (true/false).
            Правила:
            - Оценивай ТОЛЬКО по тексту пользователя, не выдумывай факты.
            - Если про категорию или цель в тексте ничего не сказано — completed=false.
            - Это бинарная отметка: только выполнено/не выполнено, без баллов и оценок.
            - Верни оценку по КАЖДОЙ присланной категории и КАЖДОЙ присланной цели, используя их id.
            """;

    public String buildUserMessage(AiEvaluateRequest req) {
        String cats = formatNamed(req.categories(), "(категорий нет)");
        String tgts = formatNamed(req.targets(), "(целей нет)");

        return """
                Категории пользователя (id — название):
                %s

                Цели пользователя (id — название):
                %s

                Текст дня пользователя:
                "%s"
                """.formatted(cats, tgts, req.dayText());
    }

    private String formatNamed(List<AiEvaluateRequest.Named> items, String emptyText) {
        if (items == null || items.isEmpty()) {
            return emptyText;
        }
        return items.stream()
                .map(it -> it.id() + " — " + it.name())
                .collect(Collectors.joining("\n"));
    }
}
