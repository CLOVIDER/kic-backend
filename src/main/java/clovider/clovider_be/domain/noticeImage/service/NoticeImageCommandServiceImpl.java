package clovider.clovider_be.domain.noticeImage.service;

import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.noticeImage.NoticeImage;
import clovider.clovider_be.domain.noticeImage.repository.NoticeImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeImageCommandServiceImpl implements NoticeImageCommandService {

    private final NoticeImageRepository noticeImageRepository;

    public void createNoticeImages(List<String> imageUrls, Notice notice) {
        imageUrls.forEach(imageUrl -> {
            NoticeImage image = NoticeImage.builder()
                    .image(imageUrl)
                    .notice(notice)
                    .build();
            noticeImageRepository.save(image);

            notice.getImages().add(image);
        });
    }
}
