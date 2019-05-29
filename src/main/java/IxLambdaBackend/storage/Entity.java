package IxLambdaBackend.storage;

import IxLambdaBackend.exception.EntityAlreadyExistException;
import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.Attribute;

import java.util.List;

public interface Entity<T extends Entity<T>> {
    T create() throws InternalException, EntityAlreadyExistException;
    T read() throws EntityNotFoundException, InternalException;
    T update(List<Attribute> updatedAttributes) throws EntityNotFoundException, InternalException;
    void delete() throws InternalException;
    List<T> getAll() throws InternalException;
    void validate();
}
