package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Meal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class MealRepositoryImpl implements MealRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Meal> findAllByOfferedAtToday(String restId) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        LocalDate tomorrow = today.plusDays(1);
        Criteria date = Criteria.where("offeredAt").gte(today).lt(tomorrow);
        Criteria combine =
                Criteria.where("restaurant._id").is(new ObjectId(restId)).andOperator(date);
        Query query = new Query(combine);
        return mongoTemplate.find(query, Meal.class);
    }
}
