package everymeal.server.global.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Cursor<T> {
    private List<T> contents;
    private Boolean hasNext;

}
