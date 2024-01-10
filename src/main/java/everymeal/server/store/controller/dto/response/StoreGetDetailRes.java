package everymeal.server.store.controller.dto.response;


import java.util.List;

public record StoreGetDetailRes(
        Long idx,
        String name,
        String address,
        String phoneNumber,
        String categoryDetail,
        Integer distance,
        Integer x,
        Integer y,
        Double grade,
        Integer reviewCount,
        Integer recommendedCount,
        List<String> images,
        Boolean isLiked) {}
