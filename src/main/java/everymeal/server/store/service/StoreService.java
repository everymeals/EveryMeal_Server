package everymeal.server.store.service;


import java.util.List;

public interface StoreService {
    List<String> getStoresByArea(String area);
}
