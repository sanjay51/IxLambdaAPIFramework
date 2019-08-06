package IxLambdaBackend.storage;

import IxLambdaBackend.storage.exception.InternalException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.IOUtils;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public abstract class S3Entity <T extends S3Entity<T>> implements Entity <T> {
    private AmazonS3 s3;

    private final String bucketName;
    @Getter private String key = null;
    @Getter private String value = null;

    public S3Entity(final String bucketName, final String key, final String value) {
        this(bucketName, key);
        this.value = value;
    }

    public S3Entity(final String bucketName, final String key) {
        this.bucketName = bucketName;
        this.key = key;
    }

    public S3Entity(final String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public T create() throws InternalException {
        try {
            this.getS3().putObject(this.bucketName, this.key, this.value);
        } catch (final Exception e) {
            throw new InternalException(e.getMessage());
        }

        return (T) this;
    }

    @Override
    public T read() throws InternalException {
        try {
            this.value = IOUtils.toString(this.getS3().getObject(this.bucketName, this.key).getObjectContent());
        } catch (final Exception e) {
            throw new InternalException(e.getMessage());
        }

        return (T) this;
    }

    @Override
    public T update() throws InternalException {
        this.create();
        return (T) this;
    }

    @Override
    public void delete() throws InternalException {
        try {
            this.getS3().deleteObject(this.bucketName, this.key);
        } catch (final Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<T> getAll() throws InternalException {
        return this.getS3().listObjectsV2(this.bucketName).getObjectSummaries()
                .stream()
                .map(a -> (T) new GenericS3Entity(a.getBucketName(), a.getKey(), this.getS3()))
                .collect(Collectors.toList());
    }

    @Override
    public void validate() {

    }

    public AmazonS3 getS3() {
        if (this.s3 == null) this.s3 = createS3Client();

        return this.s3;
    }

    public abstract AmazonS3 createS3Client();

    class GenericS3Entity extends S3Entity {
        private AmazonS3 s3;

        GenericS3Entity(String bucketName, final String key, final AmazonS3 s3) {
            super(bucketName, key);
            this.s3 = s3;
        }

        @Override
        public AmazonS3 createS3Client() {
            return this.s3;
        }
    }
}
