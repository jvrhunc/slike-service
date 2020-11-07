package si.fri.rso.slike.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class AmazonClient {

    private AmazonS3 s3;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    private Logger log = LoggerFactory.getLogger(AmazonClient.class);

    @PostConstruct
    private void initializeAmazon () {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3 = new AmazonS3Client(credentials);
    }

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileToS3Bucket(fileName, file);
            file.delete();
        } catch (AmazonServiceException ase) {
            log.error("Napaka pri nalaganju datoteke na S3", ase);
        }
        return fileUrl;
    }

    /**
     * Uploads file to S3 Bucket
     * @param fileName
     * @param file
     */
    private void uploadFileToS3Bucket(String fileName, File file) {
        s3.putObject(
                new PutObjectRequest(bucketName, fileName, file)
        );
    }

    /**
     * Generates unique fileName for a file (by date and original name)
     * @param multipartFile
     * @return
     */
    private String generateFileName(MultipartFile multipartFile) {
        return new Date().getTime() + "-"
                + Objects.requireNonNull(multipartFile.getOriginalFilename()).replace(" ", "_");
    }

    /**
     * Converts MultipartFile to File
     * @param multipartFile
     * @return
     */
    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        return "Datoteka uspešno izbrisana";
    }
}
