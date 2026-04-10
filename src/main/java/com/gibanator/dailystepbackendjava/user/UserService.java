package com.gibanator.dailystepbackendjava.user;

import com.gibanator.dailystepbackendjava.user.exception.DuplicateEmailException;
import com.gibanator.dailystepbackendjava.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    public UserEntity create(String email){
        try {
            UserEntity user = new UserEntity();
            user.setEmail(email);
            return repo.save(user);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateEmailException(email);
        }
    }

    public UserEntity getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
