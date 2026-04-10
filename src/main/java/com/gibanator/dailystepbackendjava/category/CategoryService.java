package com.gibanator.dailystepbackendjava.category;

import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import com.gibanator.dailystepbackendjava.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // UNSAFE !!! NEED TO SWITCH TO AUTH WAY (EITHER PASS UserEntity OR GET FROM AuthService) !!!!!!
    public CategoryEntity create(Long userId, String name) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        CategoryEntity cat = new CategoryEntity();

        cat.setName(name);
        cat.setUser(user);

        return categoryRepository.save(cat);
    }

    public List<CategoryEntity> findByUserId(Long userId) {
       return categoryRepository.findByUserId(userId);
    }
}
