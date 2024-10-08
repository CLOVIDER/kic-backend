package clovider.clovider_be.global.s3;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application-dev.yml")
@Slf4j
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    private static final String DOCUMENTS_FOLDER = "documents/";
    private static final String IMAGES_FOLDER = "images/";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 파일 확장자 가져오기
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    // 고유한 파일명 생성
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 업로드
    public String uploadFile(MultipartFile file, Employee employee) {
        String fileName = createFileName(file.getOriginalFilename());
        String folder = DOCUMENTS_FOLDER + employee.getId() + "/";
        return uploadToS3(file, folder, fileName);
    }

    // 이미지 업로드
    public String uploadImage(MultipartFile file, String domainName) {
        String fileName = createFileName(file.getOriginalFilename());
        String folder = IMAGES_FOLDER + domainName + "/";

        // 시작 시간 기록
        Instant start = Instant.now();

        log.info("Starting upload for file: {} on thread: {}", fileName, Thread.currentThread().getName());

        // 업로드 수행
        String url = uploadToS3(file, folder, fileName);

        // 종료 시간 기록
        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        // 완료 로그와 소요 시간 출력
        log.info("Completed upload for file: {} on thread: {}. Duration: {} ms", fileName, Thread.currentThread().getName(), duration);

        return url;
    }

    // S3에 업로드 공통 로직
    private String uploadToS3(MultipartFile file, String folder, String fileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, folder + fileName, inputStream,
                objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "파일 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, folder + fileName).toString();
    }

    // TODO: 스케줄링 얘기하면서 수정헤야할 사항
    // 여러 개의 파일 업로드
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, Employee employee) {
        List<String> fileList = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            fileList.add(uploadFile(file, employee));
        }
        return fileList;
    }

    // 여러 개의 이미지 업로드
    public List<String> uploadImages(List<MultipartFile> multipartFiles, String domainName) {
        List<String> imageList = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            imageList.add(uploadImage(file, domainName));
        }
        return imageList;
    }

    // 이미지 업로드 - 비동기
    @Async("imageUploadExecutor")
    public CompletableFuture<String> uploadImageAsync(MultipartFile file, String domainName) {
        CompletableFuture<String> future = new CompletableFuture<>();
        String fileName = createFileName(file.getOriginalFilename());
        String folder = IMAGES_FOLDER + domainName + "/";

        long startTime = System.currentTimeMillis();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        future.complete(uploadToS3(file, folder, fileName));

        long endTime = System.currentTimeMillis();
        log.info("{} - 실행 시간: {}", Thread.currentThread().getName(), endTime - startTime);

        return future;
    }

    // 이미지, 문서 삭제
    public void deleteObject(String objectName) {
        try {
            String s3File = extractImageFromUrl(objectName);
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, s3File));
        } catch (AmazonS3Exception e) {
                throw new ApiException(ErrorStatus._S3_IMAGE_NOT_FOUND);
        }
    }

    // S3 주소에 이미지 URL 추출
    private String extractImageFromUrl(String imgUrl) {
        String bucketPrefix = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
        if (imgUrl.startsWith(bucketPrefix)) {
            return imgUrl.substring(bucketPrefix.length());
        } else {
            throw new ApiException(ErrorStatus._S3_IMAGE_NOT_FOUND);
        }
    }

    // 특정 폴더의 모든 이미지 URL 추출
    public List<String> getImageUrls(String folderName) {
        List<String> imageUrls = new ArrayList<>();

        try {
            // ListObjectsV2Request를 사용하여 객체 목록 가져오기
            ListObjectsV2Request listObjects = new ListObjectsV2Request()
                    .withBucketName(bucket)
                    .withPrefix(folderName);

            ListObjectsV2Result result;
            do {
                result = amazonS3Client.listObjectsV2(listObjects);

                // 객체 목록에서 각 객체의 URL 생성
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    String objectKey = objectSummary.getKey();
                    // 빈 문자열인 경우 무시
                    if (!objectKey.isEmpty() && !objectKey.equals(folderName)) {
                        String url = amazonS3Client.getUrl(bucket, objectKey).toString();
                        imageUrls.add(url);
                    }
                }

                // 다음 페이지가 있는 경우, 다음 페이지로 이동
                listObjects.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated()); // 페이지가 끝날 때까지 반복

        } catch (AmazonS3Exception e) {
            throw new ApiException(ErrorStatus._S3_IMAGE_NOT_FOUND);
        }

        return imageUrls;
    }


}

