package com.team3.otboo.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
// 현재는 프로필 설정이 적용되어 있지 않음
//@Profile("local")
public class LocalImageStorage implements ImageStorage {

    private final Path rootPath = Paths.get("uploads");

    public LocalImageStorage() throws IOException {
        Files.createDirectories(rootPath);
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = rootPath.resolve(filename);
            file.transferTo(targetPath.toFile()); // 저장
            return "/uploads/" + filename; // 서버에 따라 변경
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public void delete(String imageUrl) {
        try {
            // "/uploads/xxx.jpg" → "xxx.jpg" 추출
            String filename = Paths.get(imageUrl).getFileName().toString();
            Path filePath = rootPath.resolve(filename);

            // uploads 디렉토리 내 파일만 삭제하도록 제한
            if (!filePath.normalize().startsWith(rootPath.normalize())) {
                throw new SecurityException("Invalid image path");
            }

            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + imageUrl, e);
        }
    }

}
