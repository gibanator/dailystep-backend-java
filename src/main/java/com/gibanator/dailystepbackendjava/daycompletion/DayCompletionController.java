package com.gibanator.dailystepbackendjava.daycompletion;

import com.gibanator.dailystepbackendjava.daycompletion.dto.DayCompletionResponse;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/day-completion")
public class DayCompletionController {
    private final DayCompletionService service;
    @PostMapping("/toggle")
    public ResponseEntity<DayCompletionResponse> toggleDayCompletion(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam LocalDate date
            ){
        boolean completed = service.setDayCompleted(user.getId(), date);
        return ResponseEntity.ok(new DayCompletionResponse(completed));
    }
}
