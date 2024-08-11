package clovider.clovider_be.domain.s3.service;

import clovider.clovider_be.global.s3.S3Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Service s3Service;

    // 여러 개의 이미지 업로드 - 비동기
    public List<String> uploadImagesAsync(List<MultipartFile> multipartFiles, String domainName) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            futures.add(s3Service.uploadImageAsync(file, domainName));
        }

        List<String> urls = new ArrayList<>();

        futures.forEach(future ->
                urls.add(future.join())
        );

        return urls;
    }

}
