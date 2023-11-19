package everymeal.server.store.controller.dto.response;

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
