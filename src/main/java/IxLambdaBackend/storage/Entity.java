package IxLambdaBackend.storage;

import IxLambdaBackend.exception.EntityAlreadyExistsException;
import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;

import java.util.List;

public interface Entity<T extends Entity<T>> {
    T create() throws InternalException, EntityAlreadyExistsException;
    T read() throws EntityNotFoundException, InternalException;
    T update() throws EntityNotFoundException, InternalException;
    void delete() throws InternalException;
    List<T> getAll() throws InternalException;
    void validate();
}
