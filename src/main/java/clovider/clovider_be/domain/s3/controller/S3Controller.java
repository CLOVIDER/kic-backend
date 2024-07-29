package clovider.clovider_be.domain.s3.controller;

import clovider.clovider_be.global.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "S3 업로드 API 명세서", description = "S3 관련 CRUD 작업을 처리하는 API")
public class S3Controller {
    private final S3Service s3Service;

    @Operation(summary = "이미지 업로드", description = "새로운 이미지를 업로드합니다.")
    @PostMapping("/upload/image")
    public String createAndUploadImage(
        @Parameter(description = "업로드할 이미지 파일", required = true, schema = @Schema(type = "string", format = "binary"))
        @RequestParam("file") MultipartFile file,
        @Parameter(description = "이미지가 업로드될 도메인 이름(notice or kindergarten)", required = true)
        @RequestParam("domainName") String domainName) {
        return s3Service.uploadImage(file, domainName);
    }

    @Operation(summary = "문서 업로드", description = "새로운 문서를 업로드합니다.")
    @PostMapping("/upload/document")
    public String createAndUploadDocument(
        @Parameter(description = "업로드할 도큐먼트 파일", required = true, schema = @Schema(type = "string", format = "binary"))
        @RequestParam("file") MultipartFile file,
        @Parameter(description = "해당 도큐먼트의 신청서 아이디", required = true)
        @RequestParam("applicationId") String applicationId) {
        return s3Service.uploadFile(file, applicationId);
    }

    

}
