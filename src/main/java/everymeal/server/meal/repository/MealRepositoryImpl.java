package everymeal.server.meal.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static everymeal.server.meal.entity.QMeal.meal;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealStatus;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.QMeal;
import everymeal.server.meal.entity.Restaurant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MealRepositoryImpl implements MealRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    /**
     * ============================================================================================
     * GLOBAL STATIC CONSTANTS
     * =============================================================================================
     */
    private final String UN_REGISTERED_MEAL = "등록된 식단이 없습니다.";
    /**
     * ============================================================================================
     * 요청 DTO 내의 제공일자 이후의 데이터가 존재하는 지를 확인합니다. <br>
     *
     * @param offeredAt 제공일자
     * @param mealType 식사구분 ( 조식/중식/석식 )
     * @param restaurant 학생식당
     * @return List<Meal>
     *     =========================================================================================
     */
    @Override
    public List<Meal> findAllByOfferedAtOnDateAndMealType(
            LocalDate offeredAt, MealType mealType, Restaurant restaurant) {
        QMeal qMeal = meal;
        var queryResult =
                jpaQueryFactory
                        .selectFrom(qMeal)
                        .where(
                                qMeal.restaurant.eq(restaurant),
                                isEqOfferedAt(offeredAt),
                                isEqMealType(mealType))
                        .fetch();
        return queryResult;
    }

    /**
     * ============================================================================================
     * 일별 식사구분에 따른 식사 데이터 조회 <br>
     *
     * @param offeredAt 제공일자
     * @param restaurant 식당
     * @return List<Meal>
     *     =========================================================================================
     */
    @Override
    public List<DayMealListGetRes> findAllByOfferedAtOnDate(
            LocalDate offeredAt, Restaurant restaurant) {
        QMeal qMeal = meal;
        var queryResult =
                jpaQueryFactory
                        .selectFrom(qMeal)
                        .where(qMeal.restaurant.eq(restaurant), isEqOfferedAt(offeredAt))
                        .transform(
                                groupBy(qMeal.mealType)
                                        .as(
                                                GroupBy.list(
                                                        Projections.constructor(
                                                                DayMealListGetRes.class,
                                                                qMeal.menu.as("menu"),
                                                                qMeal.mealType.as("mealType"),
                                                                qMeal.mealStatus.as("mealStatus"),
                                                                qMeal.offeredAt.as("offeredAt"),
                                                                qMeal.price.as("price"),
                                                                qMeal.category.as("category"),
                                                                qMeal.restaurant.name.as(
                                                                        "restaurantName")))));

        List<DayMealListGetRes> resultList = new ArrayList<>();
        for (MealType mealType : MealType.values()) {
            List<DayMealListGetRes> dayMeals = queryResult.get(mealType);
            if (dayMeals == null || dayMeals.isEmpty()) {
                resultList.add(
                        new DayMealListGetRes(
                                UN_REGISTERED_MEAL,
                                mealType,
                                MealStatus.CLOSED,
                                offeredAt,
                                0.0,
                                MealCategory.DEFAULT,
                                restaurant.getName()));
            } else {
                resultList.addAll(dayMeals);
            }
        }
        return resultList;
    }
    /**
     * ============================================================================================
     * 주간 식사 데이터 조회 <br>
     *
     * @param restaurant 식당
     * @param monday 월요일
     * @param sunday 일요일
     * @return List<WeekMealListGetResTest>
     *     =========================================================================================
     */
    @Override
    public List<WeekMealListGetRes> getWeekMealList(
            Restaurant restaurant, LocalDate monday, LocalDate sunday) {
        QMeal qMeal = meal;
        var transform =
                jpaQueryFactory
                        .selectFrom(qMeal)
                        .where(
                                qMeal.restaurant
                                        .eq(restaurant)
                                        .and(qMeal.offeredAt.between(monday, sunday)))
                        .transform(
                                groupBy(qMeal.offeredAt, qMeal.mealType)
                                        .as(
                                                GroupBy.list(
                                                        Projections.constructor(
                                                                DayMealListGetRes.class,
                                                                qMeal.menu.as("menu"),
                                                                qMeal.mealType.as("mealType"),
                                                                qMeal.mealStatus.as("mealStatus"),
                                                                qMeal.offeredAt.as("offeredAt"),
                                                                qMeal.price.as("price"),
                                                                qMeal.category.as("category"),
                                                                qMeal.restaurant.name.as(
                                                                        "restaurantName")))));
        LocalDate currentInstant = monday;
        List<WeekMealListGetRes> result = new ArrayList<>();
        while (!currentInstant.isAfter(sunday)) {
            List<DayMealListGetRes> dayMealListGetResList = new ArrayList<>();

            for (MealType mealType : MealType.values()) {
                List<DayMealListGetRes> dayMeals = transform.get(List.of(currentInstant, mealType));
                if (dayMeals == null || dayMeals.isEmpty()) {
                    dayMealListGetResList.add(
                            new DayMealListGetRes(
                                    UN_REGISTERED_MEAL,
                                    mealType,
                                    MealStatus.CLOSED,
                                    currentInstant,
                                    0.0,
                                    MealCategory.DEFAULT,
                                    restaurant.getName()));
                } else {
                    dayMealListGetResList.addAll(dayMeals);
                }
            }
            currentInstant = currentInstant.plusDays(1);
            result.add(new WeekMealListGetRes(currentInstant, dayMealListGetResList));
        }
        return result;
    }

    /** 단일 메뉴, 복합 메뉴를 판별 */
    private BooleanExpression isSingleMenu(boolean isSingle) {
        return isSingle ? meal.category.eq(MealCategory.DEFAULT) : null;
    }

    /** offeredAt 과 동일한 경우 */
    private BooleanExpression isEqOfferedAt(LocalDate offeredAt) {
        return QMeal.meal.offeredAt.eq(offeredAt);
    }
    /** mealType 과 동일한 경우 */
    private BooleanExpression isEqMealType(MealType mealType) {
        return meal.mealType.eq(mealType);
    }
}
