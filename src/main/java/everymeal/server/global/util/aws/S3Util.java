package everymeal.server.global.util.aws;


import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class S3Util {

    public static AmazonS3 amazonS3;
    public static String bucket;
    public static String runningName;

    @Value("${cloud.aws.s3.bucket}")
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Value("${running.name}")
    public void setRunningName(String runningName) {
        this.runningName = runningName;
    }

    public S3Util(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        amazonS3 =
                AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withRegion(Regions.AP_NORTHEAST_2)
                        .build();
    }

    public URL getPresignedUrl(String fileName) {
        String path = runningName + File.separator + fileName;
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, path);
        request.setExpiration(new Date(Instant.now().plus(60, ChronoUnit.SECONDS).toEpochMilli()));
        request.setMethod(HttpMethod.PUT);
        return amazonS3.generatePresignedUrl(request);
    }

    public static String getImgUrl(String fileName) {
        URL url = amazonS3.getUrl(bucket, runningName + File.separator + fileName);
        return url.toString();
    }

    public void deleteImage(String fileUrl) {
        try {
            String fileKey = "dev/" + fileUrl;
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileKey));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("S3 이미지 삭제 실패 fileUrl: {}", fileUrl);
        }
    }
}
