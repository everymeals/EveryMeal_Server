package everymeal.server.meal.service;


import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.exception.ExceptionList;
import everymeal.server.meal.controller.dto.request.RestaurantRegisterReq;
import everymeal.server.meal.entity.Restaurant;
import everymeal.server.meal.repository.RestaurantRepository;
import everymeal.server.university.entity.University;
import everymeal.server.university.service.UniversityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UniversityService universityServiceImpl;

    @Override
    public Restaurant getRestaurant(Long restaurantIdx) {
        return restaurantRepository
                .findById(restaurantIdx)
                .orElseThrow(() -> new ApplicationException(ExceptionList.RESTAURANT_NOT_FOUND));
    }

    @Override
    @Transactional
    public Boolean createRestaurant(RestaurantRegisterReq restaurantRegisterReq) {
        // 학교 조회
        University university =
                universityServiceImpl.getUniversity(
                        restaurantRegisterReq.universityName(), restaurantRegisterReq.campusName());
        // 식당 등록
        Restaurant restaurant =
                Restaurant.builder()
                        .restaurantRegisterReq(restaurantRegisterReq)
                        .university(university)
                        .build();
        return restaurantRepository.save(restaurant).getIdx() != null;
    }

    @Override
    public List<Restaurant> getAllByUniversityAndIsDeletedFalse(University university) {
        return restaurantRepository.findAllByUniversityAndIsDeletedFalse(university);
    }
}
