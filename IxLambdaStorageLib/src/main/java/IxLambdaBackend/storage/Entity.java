package IxLambdaBackend.storage;

import IxLambdaBackend.storage.exception.EntityAlreadyExistsException;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;

import java.io.IOException;
import java.util.List;

public interface Entity<T extends Entity<T>> {
    T create() throws InternalException, EntityAlreadyExistsException;
    T read() throws EntityNotFoundException, InternalException;
    T update() throws EntityNotFoundException, InternalException;
    void delete() throws InternalException;
    List<Entity> getAll() throws InternalException, EntityNotFoundException;
    default Page getAllWithPagination(final String paginationHandle, final int pageSize) throws InternalException, EntityNotFoundException, IOException {
        return null;
    }
    void validate();
}
