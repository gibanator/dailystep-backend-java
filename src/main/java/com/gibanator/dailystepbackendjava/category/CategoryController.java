package com.gibanator.dailystepbackendjava.category;

import com.gibanator.dailystepbackendjava.category.dto.*;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService service;

    // NEED TO ADD AUTH LATER, JUST SIMPLE SOLUTION FOR NOW!!!
    @PostMapping
    public ResponseEntity<CreateCategoryResponse> create(
            @AuthenticationPrincipal UserEntity user,  // BETTER TO USE PRINCIPAL CLASS LATER
            @Valid @RequestBody CreateCategoryRequest req
    ) {
        CategoryEntity category = service.create(user.getId(), req.getName());

        CreateCategoryResponse resp = new CreateCategoryResponse();
        resp.setName(category.getName());

        return ResponseEntity
                 .status(HttpStatus.CREATED)
                .body(resp);
    }

    @GetMapping
    public ResponseEntity<List<GetCategoryResponse>> getMyCategories(
            @AuthenticationPrincipal UserEntity user
            ) {
        List<GetCategoryResponse> resp = service.findByUserId(user.getId())
                .stream()
                .map(cat -> {
                    GetCategoryResponse dto = new GetCategoryResponse();
                    dto.setName(cat.getName());
                    return dto;
                })
                .toList();
        return ResponseEntity
                .ok(resp);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GetCategoryResponse> editMyCategoryById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user,
            @RequestBody EditCategoryRequest req
    ){
        CategoryEntity cat = service.update(
                id,
                user.getId(),
                req.getName(),
                req.isActive(),
                req.isVisible()
        );

        GetCategoryResponse resp = new GetCategoryResponse();
        resp.setId(cat.getId());
        resp.setName(cat.getName());

        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user
    ){
        service.delete(id, user.getId());

        return ResponseEntity
                .noContent()
                .build();
    }
}
