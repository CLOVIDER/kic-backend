package clovider.clovider_be.domain.s3.controller;

import clovider.clovider_be.domain.s3.service.ImageService;
import clovider.clovider_be.global.response.ApiResponse;
import clovider.clovider_be.global.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "S3 업로드 API 명세서", description = "S3 관련 CRUD 작업을 처리하는 API")
public class S3Controller {
    private final S3Service s3Service;
    private final ImageService imageService;

    @Operation(summary = "이미지 업로드", description = "새로운 이미지를 업로드합니다.")
    @PostMapping(value =  "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> createAndUploadImage(
        @Parameter(description = "업로드할 이미지 파일", required = true, schema = @Schema(type = "string", format = "binary"))
        @RequestPart("file") MultipartFile file,
        @Parameter(description = "이미지가 업로드될 도메인 이름(notice or kindergarten or qna)", required = true)
        @RequestParam("domainName") String domainName) {
        return ApiResponse.onSuccess(s3Service.uploadImage(file, domainName));
    }

    @Operation(summary = "다중 이미지 업로드 - 비동기", description = "새로운 이미지를 다중으로 업로드합니다.")
    @PostMapping(value = "/images/async", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<String>> uploadMultipleImagesAsync(
            @Parameter(description = "업로드할 이미지 파일들", required = true, schema = @Schema(type = "array", format = "binary"))
            @RequestPart("files") List<MultipartFile> files,
            @Parameter(description = "이미지들이 업로드될 도메인 이름(notice or kindergarten)", required = true)
            @RequestParam("domainName") String domainName) {
        List<String> urls = imageService.uploadImagesAsync(files, domainName);
        return ApiResponse.onSuccess(urls);
    }

    @Operation(summary = "다중 이미지 업로드 - 동기", description = "새로운 이미지를 다중으로 업로드합니다.")
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<String>> uploadMultipleImages(
            @Parameter(description = "업로드할 이미지 파일들", required = true, schema = @Schema(type = "array", format = "binary"))
            @RequestPart("files") List<MultipartFile> files,
            @Parameter(description = "이미지들이 업로드될 도메인 이름(notice or kindergarten)", required = true)
            @RequestParam("domainName") String domainName) {
        List<String> urls = s3Service.uploadImages(files, domainName);
        return ApiResponse.onSuccess(urls);
    }

    @Operation(summary = "문서 업로드", description = "새로운 문서를 업로드합니다.")
    @PostMapping(value = "/upload/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> createAndUploadDocument(
        @Parameter(description = "업로드할 도큐먼트 파일", required = true, schema = @Schema(type = "string", format = "binary"))
        @RequestPart("file") MultipartFile file,
        @Parameter(description = "해당 도큐먼트의 신청서 아이디", required = true)
        @RequestParam("applicationId") String applicationId) {
        return ApiResponse.onSuccess(s3Service.uploadFile(file, applicationId));
    }

    

}
