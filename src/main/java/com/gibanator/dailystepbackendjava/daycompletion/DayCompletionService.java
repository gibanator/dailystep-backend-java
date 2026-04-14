package com.gibanator.dailystepbackendjava.daycompletion;

import com.gibanator.dailystepbackendjava.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DayCompletionService {

    private final DayCompletionRepository dayCompletionRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean setDayCompleted(Long userId, LocalDate date){
        DayCompletionId id = new DayCompletionId(userId, date);

        if (dayCompletionRepository.existsById(id)){
            dayCompletionRepository.deleteById(id);
            return false;
        }

        DayCompletionEntity entity = new DayCompletionEntity();
        entity.setId(id);
        entity.setUser(userRepository.getReferenceById(userId));

        dayCompletionRepository.save(entity);

        return true;
    }
}
