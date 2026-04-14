package com.gibanator.dailystepbackendjava.dailycategoryprogress;

import com.gibanator.dailystepbackendjava.dailycategoryprogress.dto.DailyProgressResponse;
import com.gibanator.dailystepbackendjava.dailycategoryprogress.dto.SaveDailyProgressRequest;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class DailyCategoryProgressController {
    private final DailyCategoryProgressService service;

    @PostMapping
    public ResponseEntity<?> saveProgressForDay(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody SaveDailyProgressRequest req
            ){

        service.saveDailyProgress(user.getId(), req);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<DailyProgressResponse> getProgressForDay(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam LocalDate date
            ){

        return ResponseEntity.ok(
                service.getDailyProgress(user.getId(), date)
        );

    }
}
