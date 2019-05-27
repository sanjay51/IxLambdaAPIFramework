package IxDeclarativeAPI.storage;

import IxDeclarativeAPI.storage.attribute.Attribute;

import java.util.List;

public interface Entity {
    void create();
    void read();
    void update(List<Attribute> updatedAttributes);
    void delete();
    List<? extends Entity> getAll();
    void validate();
}
