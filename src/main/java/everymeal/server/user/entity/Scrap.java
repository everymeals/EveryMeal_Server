package everymeal.server.user.entity;


import everymeal.server.store.entity.Store;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "scrap")
public class Scrap {
    @Id private String _id;
    private Store store;
    private User user;
}
