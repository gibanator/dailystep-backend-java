package com.gibanator.dailystepbackendjava.category;

import com.gibanator.dailystepbackendjava.category.dto.CreateCategoryRequest;
import com.gibanator.dailystepbackendjava.category.dto.CreateCategoryResponse;
import com.gibanator.dailystepbackendjava.category.dto.GetCategoriesForUserResponse;
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
            @Valid @RequestBody CreateCategoryRequest req) {
        CategoryEntity category = service.create(userId, req.getName());

        CreateCategoryResponse resp = new CreateCategoryResponse();
        resp.setName(category.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp);
    }

    @GetMapping
    public ResponseEntity<List<GetCategoriesForUserResponse>> getByUserId(
            @RequestParam Long userId
    ) {
        List<GetCategoriesForUserResponse> resp = service.findByUserId(userId)
                .stream()
                .map(cat -> {
                    GetCategoriesForUserResponse dto = new GetCategoriesForUserResponse();
                    dto.setName(cat.getName());
                    return dto;
                })
                .toList();
        return ResponseEntity
                .ok(resp);
    }
}
