package IxLambdaBackend.storage;

import lombok.Data;

import java.util.List;

@Data
public class Page {
    final List<Entity> entities;
    final String paginationHandle;
}
