package clovider.clovider_be.global.s3;

import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application-dev.yml")
public class S3Service {
    private final AmazonS3Client amazonS3Client;

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
    public String uploadFile(MultipartFile file, String applicationId) {
        String fileName = createFileName(file.getOriginalFilename());
        String folder = "documents/" + applicationId + "/";
        return uploadToS3(file, folder, fileName);
    }

    // 이미지 업로드
    public String uploadImage(MultipartFile file, String domainName) {
        String fileName = createFileName(file.getOriginalFilename());
        String folder = "images/" + domainName + "/";
        return uploadToS3(file, folder, fileName);
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
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String applicationId) {
        List<String> fileList = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            fileList.add(uploadFile(file, applicationId));
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

    // 이미지 삭제
    public void deleteFile(String fileName) throws AmazonS3Exception {
        String s3File = extractImageFromUrl(fileName);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, s3File));
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
}

