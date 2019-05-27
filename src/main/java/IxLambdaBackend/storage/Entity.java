package IxLambdaBackend.storage;

import IxLambdaBackend.exception.EntityNotFoundException;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.storage.attribute.Attribute;

import java.util.List;

public interface Entity {
    void create() throws InternalException;
    void read() throws EntityNotFoundException, InternalException;
    void update(List<Attribute> updatedAttributes) throws EntityNotFoundException, InternalException;
    void delete() throws InternalException;
    List<? extends Entity> getAll() throws InternalException;
    void validate();
}
