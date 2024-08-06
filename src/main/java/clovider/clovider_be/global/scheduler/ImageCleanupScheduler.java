package clovider.clovider_be.global.scheduler;

import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import clovider.clovider_be.domain.noticeImage.NoticeImage;
import clovider.clovider_be.domain.noticeImage.repository.NoticeImageRepository;
import clovider.clovider_be.global.s3.S3Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageCleanupScheduler {

    private final S3Service s3Service;
    private final NoticeImageRepository noticeImageRepository;
    private final KindergartenImageRepository kindergartenImageRepository;
    
    static final String NOTICE_FOLDER = "images/notice/";
    static final String KINDERGARTEN_FOLDER = "images/kindergarten/";

    @Scheduled(cron = "0 0 2 * * ?")  // 매일 새벽 2시에 실행
    public void cleanUpUnusedImages() {
        log.info("사용하지 않는 이미지 삭제 스케줄러 시작");

        // 공지사항 이미지 삭제
        cleanUpFolder(NOTICE_FOLDER, noticeImageRepository.findAll().stream()
                .map(NoticeImage::getImage)
                .collect(Collectors.toSet()));

        // 어린이집 이미지 삭제
        cleanUpFolder(KINDERGARTEN_FOLDER, kindergartenImageRepository.findAll().stream()
                .map(KindergartenImage::getImage)
                .collect(Collectors.toSet()));
    }

    private void cleanUpFolder(String folder, Set<String> usedImageUrls) {
        List<String> s3ImageUrls = s3Service.getImageUrls(folder);

        for (String url : s3ImageUrls) {
            if (!usedImageUrls.contains(url)) {
                s3Service.deleteObject(url);
            }
        }
    }

}
