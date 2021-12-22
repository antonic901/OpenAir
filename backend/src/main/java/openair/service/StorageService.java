package openair.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import openair.service.interfaces.IStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class StorageService implements IStorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    @Value("${application.bucket.name}")
    private String bucketName;

    private AmazonS3 s3Client;

    @Autowired
    public StorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj).withCannedAcl(CannedAccessControlList.PublicRead));
        fileObj.delete();
        return "File uploaded: " + fileName;
    }

    @Override
    public String upload(File file) {
        String fileName = file.getName();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        file.delete();
        return "File uploaded: " + fileName;
    }


    @Override
    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName,fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName,fileName);
        return fileName + "removed...";
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            logger.error("Error converting multipartFile to File: {}", e.getMessage());
        }
        return convertedFile;
    }


}

