package openair.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface IStorageService {
    String uploadFile(MultipartFile file);
    String upload(File file);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);
}
