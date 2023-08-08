package everymeal.server.meal.controller;


import everymeal.server.meal.service.MealService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meals")
@Tag(name = "Meal API", description = "학식 식단 관련 API입니다")
public class MealController {

    private final MealService mealService;

    @GetMapping("/test")
    public void test() {
        mealService.test();
    }
}
