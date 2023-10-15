package everymeal.server.meal.repository;


import static com.querydsl.core.group.GroupBy.groupBy;

import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.DayMealListGetResTest;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetResTest;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealCategory;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.QMeal;
import everymeal.server.meal.entity.QRestaurant;
import everymeal.server.meal.entity.Restaurant;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MealRepositoryImpl implements MealRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    /**
     * ============================================================================================
     * 현재 사용하지 않는 함수입니다. -- 추후 사용 가능성을 고려해서 유지하고 있습니다. <br>
     * 요청 DTO 내의 제공일자 이후의 데이터가 존재하는 지를 확인합니다.
     *
     * @param restaurantIdx 식당 아이디
     * @param mealRegisterReq 요청 dto
     * @return List<Meal>
     *     =========================================================================================
     */
    @Override
    public List<Meal> findAllByAfterOfferedAt(MealRegisterReq mealRegisterReq, Long restaurantIdx) {
        var queryResult =
                jpaQueryFactory
                        .select(QMeal.meal)
                        .from(QMeal.meal)
                        .leftJoin(QMeal.meal.restaurant, QRestaurant.restaurant)
                        .on(QRestaurant.restaurant.idx.eq(restaurantIdx))
                        .where(isAfterOfferedAt(mealRegisterReq.offeredAt()))
                        .fetch();
        return queryResult;
    }
    /**
     * ============================================================================================
     * 일별 식사구분에 따른 식사 데이터 조회 <br>
     *
     * @param restaurantIdx 식당 아이디
     * @param offeredAt 제공일자
     * @param mealType 식사구분 ( 아침/점심/저녁 )
     * @return List<Meal>
     *     =========================================================================================
     */
    @Override
    public List<Meal> findAllByOfferedAtOnDateAndMealType(
            Instant offeredAt, MealType mealType, Long restaurantIdx) {
        var queryResult =
                jpaQueryFactory
                        .selectFrom(QMeal.meal)
                        .leftJoin(QMeal.meal.restaurant, QRestaurant.restaurant)
                        .on(QRestaurant.restaurant.idx.eq(restaurantIdx))
                        .where(isEqOfferedAt(offeredAt), isEqMealType(mealType))
                        .fetch();
        return queryResult;
    }

    @Override
    public List<WeekMealListGetResTest> getWeekMealList(Restaurant restaurant, Instant mondayInstant,
        Instant sundayInstant) {
        QMeal qMeal = QMeal.meal;
        Map<Instant, WeekMealListGetResTest> transform = jpaQueryFactory.selectFrom(qMeal)
            .where(qMeal.restaurant.eq(restaurant)
                .and(qMeal.offeredAt.between(mondayInstant, sundayInstant)))
            .transform(
                groupBy(qMeal.offeredAt)
                    .as(Projections.constructor(WeekMealListGetResTest.class,
                        qMeal.offeredAt,
                        GroupBy.list(Projections.constructor(DayMealListGetResTest.class,
                            qMeal.menu.as("menu"),
                            qMeal.mealType.as("mealType"),
                            qMeal.mealStatus.as("mealStatus"),
                            qMeal.offeredAt.as("offeredAt"),
                            qMeal.price.as("price"),
                            qMeal.category.as("category"),
                            qMeal.restaurant.name.as("restaurantName")
                        ))
                    ))
            );
        return transform.keySet().stream()
            .map(transform::get)
            .toList();
    }

    /** 단일 메뉴, 복합 메뉴를 판별 */
    private BooleanExpression isSingleMenu(boolean isSingle) {
        return isSingle ? QMeal.meal.category.eq(MealCategory.DEFAULT) : null;
    }

    /** offeredAt 과 동일한 경우 */
    private BooleanExpression isEqOfferedAt(Instant offeredAt) {
        Instant startOfDay = offeredAt.truncatedTo(ChronoUnit.DAYS);
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.MILLIS);
        return QMeal.meal.offeredAt.between(startOfDay, endOfDay);
    }
    /** mealType 과 동일한 경우 */
    private BooleanExpression isEqMealType(MealType mealType) {
        return QMeal.meal.mealType.eq(mealType);
    }

    /** offeredAt 이후인 경우 */
    private BooleanExpression isAfterOfferedAt(Instant offeredAt) {
        return QMeal.meal.offeredAt.after(offeredAt);
    }
}
