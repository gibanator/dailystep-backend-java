package com.gibanator.dailystepbackendjava.category;

import com.gibanator.dailystepbackendjava.category.dto.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @Valid @RequestParam Long userId,  // USER SHOULD NOT BE PASSED
            @Valid @RequestBody CreateCategoryRequest req
    ) {
        CategoryEntity category = service.create(userId, req.getName());

        CreateCategoryResponse resp = new CreateCategoryResponse();
        resp.setName(category.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp);
    }

    @GetMapping
    public ResponseEntity<List<GetCategoryResponse>> getByUserId(
            @RequestParam Long userId
    ) {
        List<GetCategoryResponse> resp = service.findByUserId(userId)
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
    public ResponseEntity<GetCategoryResponse> editByUserId(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody EditCategoryRequest req
    ){
        CategoryEntity cat = service.update(
                id,
                userId,
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
            @RequestParam Long userId
    ){
        service.delete(id, userId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
