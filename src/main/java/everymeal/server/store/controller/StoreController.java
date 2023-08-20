package everymeal.server.store.controller;


import everymeal.server.store.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/stores")
@RestController
@RequiredArgsConstructor
@Tag(name = "Store API", description = "학교 주변 식당 관련 API입니다")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{area}")
    public List<String> getStoresByArea(@PathVariable("area") String area) {
        //        return storeService.getStoresByArea(area);
        return null;
    }
}
