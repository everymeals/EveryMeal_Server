package everymeal.server.meal.service;

import static everymeal.server.global.exception.ExceptionList.RESTAURANT_NOT_FOUND;

import everymeal.server.global.exception.ApplicationException;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.university.entity.University;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RestaurantCommServiceImpl {

    private final RestaurantRepository restaurantRepository;

    public Optional<Restaurant> getRestaurantOptionalEntity(Long restaurantIdx) {
        return restaurantRepository.findById(restaurantIdx);
    }

    public Restaurant getRestaurantEntity(Long restaurantIdx) {
        return restaurantRepository
                .findById(restaurantIdx)
                .orElseThrow(() -> new ApplicationException(RESTAURANT_NOT_FOUND));
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllByUniversityAndIsDeletedFalse(University university) {
        return restaurantRepository.findAllByUniversityAndIsDeletedFalse(university);
    }
}
