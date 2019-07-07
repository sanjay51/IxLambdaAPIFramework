package IxLambdaBackend.storage.schema;

/**
 * Defines what type of access a DDBEntity user can have, over a given attributes.
 */
public enum AccessType {
    READ_ONLY,
    WRITE_ONLY,
    READ_WRITE,
    RESTRICTED
}
