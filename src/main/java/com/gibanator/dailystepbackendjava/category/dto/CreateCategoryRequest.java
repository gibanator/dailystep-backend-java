package com.gibanator.dailystepbackendjava.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {
    @NotNull
    private String name;
}
