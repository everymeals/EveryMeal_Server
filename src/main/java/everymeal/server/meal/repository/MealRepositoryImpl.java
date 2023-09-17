package everymeal.server.meal.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.QMeal;
import everymeal.server.meal.entity.QRestaurant;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MealRepositoryImpl implements MealRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Meal> findAllByAfterOfferedAt(MealRegisterReq mealRegisterReq, Long restaurantIdx) {
        var queryResult =
                jpaQueryFactory
                        .select(QMeal.meal)
                        .from(QMeal.meal)
                        .leftJoin(QMeal.meal.restaurant, QRestaurant.restaurant)
                        .on(QRestaurant.restaurant.idx.eq(restaurantIdx))
                        .where(isAfterOfferedAt(mealRegisterReq.getOfferedAt()))
                        .fetch();
        return queryResult;
    }

    @Override
    public List<Meal> findAllByOfferedAt(LocalDate offeredAt, Long restaurantIdx) {
        var queryResult =
                jpaQueryFactory
                        .selectFrom(QMeal.meal)
                        .leftJoin(QMeal.meal.restaurant, QRestaurant.restaurant)
                        .on(QRestaurant.restaurant.idx.eq(restaurantIdx))
                        .where(isEqOfferedAt(offeredAt))
                        .fetch();
        return queryResult;
    }

    @Override
    public List<Meal> findAllByBetweenOfferedAtAndEndedAt(
            LocalDate startedAt, LocalDate endedAt, Long restaurantIdx) {
        var queryResult =
                jpaQueryFactory
                        .selectFrom(QMeal.meal)
                        .leftJoin(QMeal.meal.restaurant, QRestaurant.restaurant)
                        .on(QRestaurant.restaurant.idx.eq(restaurantIdx))
                        .where(isBetweenOfferedAt(startedAt, endedAt))
                        .orderBy(QMeal.meal.offeredAt.desc(), QMeal.meal.mealType.desc())
                        .fetch();
        return queryResult;
    }

    private BooleanExpression isBetweenOfferedAt(LocalDate startedAt, LocalDate endedAt) {
        return QMeal.meal.offeredAt.between(startedAt, endedAt);
    }

    /** offeredAt 과 동일 한 경우 */
    private BooleanExpression isEqOfferedAt(LocalDate offeredAt) {
        return QMeal.meal.offeredAt.eq(offeredAt);
    }

    /** offeredAt 이후인 경우 */
    private BooleanExpression isAfterOfferedAt(LocalDate offeredAt) {
        return QMeal.meal.offeredAt.after(offeredAt);
    }
}
