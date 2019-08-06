package IxLambdaBackend.storage;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.jupiter.api.Test;

public class S3EntityTest {
    @Test
    public void test() throws Exception {
        S3Entity s3Entity = new TestS3Entity("inspirationalshortstories", "key", "value");
        //s3Entity.create();

        S3Entity s3Entity2 = new TestS3Entity("inspirationalshortstories", "key");
        //s3Entity2.read();

        System.out.println(s3Entity2.getValue());
    }

    class TestS3Entity extends S3Entity {

        public TestS3Entity(String bucketName, String key, String value) {
            super(bucketName, key, value);
        }

        public TestS3Entity(String bucketName, String key) {
            super(bucketName, key);
        }

        @Override
        public AmazonS3 createS3Client() {
            return AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        }
    }
}
