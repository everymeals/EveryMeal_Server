package everymeal.server.review.entity;


import java.io.Serializable;

public class MarkId implements Serializable {

    private Long user;
    private Long review;

    public MarkId() {}

    public MarkId(Long user, Long review) {
        super();
        this.user = user;
        this.review = review;
    }
}
