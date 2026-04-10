package com.gibanator.dailystepbackendjava.user;

import com.gibanator.dailystepbackendjava.user.dto.CreateUserRequest;
import com.gibanator.dailystepbackendjava.user.dto.CreateUserResponse;
import com.gibanator.dailystepbackendjava.user.dto.GetUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest req){
        UserEntity user = service.create(req.getEmail());

        CreateUserResponse resp = new CreateUserResponse();
        resp.setEmail(user.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getById(@PathVariable Long id){
        UserEntity user = service.getById(id);

        GetUserResponse resp = new GetUserResponse();
        resp.setEmail(user.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
    }
}
