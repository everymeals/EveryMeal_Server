package everymeal.server.store.controller.dto.response;


import java.util.List;

public record StoreGetRes(
        Long idx,
        String name,
        String address,
        String phoneNumber,
        String categoryDetail,
        Integer distance,
        Double grade,
        Integer reviewCount,
        Integer recommendedCount,
        Boolean isLiked) {}
