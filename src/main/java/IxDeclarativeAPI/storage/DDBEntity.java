package IxDeclarativeAPI.storage;

import IxDeclarativeAPI.storage.attribute.Attribute;
import lombok.Builder;

import java.util.List;

@Builder
public class DDBEntity implements Entity {
    private final Schema schema;
    private final List<Attribute> attributes;

    @Override
    public void create() {

    }

    @Override
    public void read() {

    }

    @Override
    public void update(final List<Attribute> updatedAttributes) {

    }

    @Override
    public void delete() {

    }

    @Override
    public List<Entity> getAll() {
        return null;
    }

    @Override
    public void validate() {

    }
}
