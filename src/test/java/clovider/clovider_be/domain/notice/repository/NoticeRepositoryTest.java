package clovider.clovider_be.domain.notice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.noticeImage.NoticeImage;
import clovider.clovider_be.global.config.QuerydslConfig;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QuerydslConfig.class)
class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee admin;

    @BeforeEach
    void setup() {
        admin = Employee.builder()
                .nameKo("홍길동")
                .accountId("hong123")
                .password("securePassword")
                .employeeNo("E001")
                .joinDt(LocalDate.of(2022, 1, 1))
                .dept("IT")
                .role(Role.ADMIN)
                .build();

        employeeRepository.save(admin);
    }

    private Notice createAndSaveNotice(String title, String content) {
        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .admin(admin)
                .build();
        return noticeRepository.save(notice);
    }

    private void addImagesToNotice(Notice notice, List<String> imageUrls) {
        imageUrls.forEach(url -> {
            NoticeImage image = NoticeImage.builder()
                    .image(url)
                    .notice(notice)
                    .build();
            notice.getImages().add(image);
        });
    }

    @Test
    @DisplayName("공지사항 생성 및 조회 테스트")
    public void createNoticeTest() {
        // given
        Notice savedNotice = createAndSaveNotice("공지사항 제목", "공지사항 내용");
        addImagesToNotice(savedNotice, List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg"));

        // when
        Notice resultNotice = noticeRepository.findById(savedNotice.getId()).orElse(null);

        // then
        assertNotNull(resultNotice);
        assertThat(resultNotice.getImages()).hasSize(2);
        assertThat(resultNotice.getImages().get(0).getImage()).isEqualTo("http://example.com/image1.jpg");
        assertThat(resultNotice.getImages().get(1).getImage()).isEqualTo("http://example.com/image2.jpg");
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    public void updateNoticeImagesTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Notice savedNotice = createAndSaveNotice("원래 제목", "원래 내용");
        addImagesToNotice(savedNotice, List.of("http://example.com/old_image1.jpg"));

        NoticeRequest noticeRequest = new NoticeRequest();
        Field titleField = NoticeRequest.class.getDeclaredField("title");
        Field contentField = NoticeRequest.class.getDeclaredField("content");
        Field imageUrlsField = NoticeRequest.class.getDeclaredField("imageUrls");

        titleField.setAccessible(true);
        contentField.setAccessible(true);
        imageUrlsField.setAccessible(true);

        titleField.set(noticeRequest, "수정된 제목");
        contentField.set(noticeRequest, "수정된 내용");
        imageUrlsField.set(noticeRequest, List.of("http://example.com/new_image1.jpg", "http://example.com/new_image2.jpg"));

        savedNotice.updateNotice(noticeRequest);

        // when
        Notice updatedNotice = noticeRepository.findById(savedNotice.getId()).orElse(null);

        // then
        assertNotNull(updatedNotice);
        assertThat(updatedNotice.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedNotice.getContent()).isEqualTo("수정된 내용");
        assertThat(updatedNotice.getImages()).hasSize(2);
        assertThat(updatedNotice.getImages().get(0).getImage()).isEqualTo("http://example.com/new_image1.jpg");
        assertThat(updatedNotice.getImages().get(1).getImage()).isEqualTo("http://example.com/new_image2.jpg");
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    public void deleteNoticeTest() {
        // given
        Notice savedNotice = createAndSaveNotice("공지사항 제목", "공지사항 내용");
        addImagesToNotice(savedNotice, List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg"));

        // when
        noticeRepository.deleteById(savedNotice.getId());

        // then
        assertFalse(noticeRepository.findById(savedNotice.getId()).isPresent());
    }

    @Test
    @DisplayName("최신 3개 공지사항 조회 테스트")
    public void findTop3ByOrderByIdDescTest() {
        // given
        Notice notice1 = createAndSaveNotice("Notice 1", "Content 1");
        addImagesToNotice(notice1, List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg"));

        Notice notice2 = createAndSaveNotice("Notice 2", "Content 2");
        addImagesToNotice(notice2, List.of("http://example.com/image3.jpg", "http://example.com/image4.jpg"));

        Notice notice3 = createAndSaveNotice("Notice 3", "Content 3");
        addImagesToNotice(notice3, List.of("http://example.com/image5.jpg", "http://example.com/image6.jpg"));

        Notice notice4 = createAndSaveNotice("Notice 4", "Content 4");
        addImagesToNotice(notice4, List.of("http://example.com/image7.jpg", "http://example.com/image8.jpg"));

        // when
        List<Notice> top3Notices = noticeRepository.findTop3ByOrderByIdDesc();

        // then
        assertThat(top3Notices).hasSize(3);
        assertThat(top3Notices.get(0).getId()).isGreaterThan(top3Notices.get(1).getId());
        assertThat(top3Notices.get(1).getId()).isGreaterThan(top3Notices.get(2).getId());
        assertThat(top3Notices).containsExactly(notice4, notice3, notice2);
        assertThat(top3Notices.get(0).getImages()).hasSize(2);
        assertThat(top3Notices.get(1).getImages()).hasSize(2);
        assertThat(top3Notices.get(2).getImages()).hasSize(2);

        assertThat(top3Notices.get(0).getImages().stream()
                .map(NoticeImage::getImage)
                .collect(Collectors.toList()))
                .containsExactly("http://example.com/image7.jpg", "http://example.com/image8.jpg");

        assertThat(top3Notices.get(1).getImages().stream()
                .map(NoticeImage::getImage)
                .collect(Collectors.toList()))
                .containsExactly("http://example.com/image5.jpg", "http://example.com/image6.jpg");

        assertThat(top3Notices.get(2).getImages().stream()
                .map(NoticeImage::getImage)
                .collect(Collectors.toList()))
                .containsExactly("http://example.com/image3.jpg", "http://example.com/image4.jpg");
    }

    @Test
    @DisplayName("공지사항 검색 테스트")
    public void searchNoticeTest(){
        // given
        Notice notice1 = createAndSaveNotice("Notice 1", "Content 1");
        addImagesToNotice(notice1, List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg"));

        Notice notice2 = createAndSaveNotice("Notice 2", "Content 2");
        addImagesToNotice(notice2, List.of("http://example.com/image3.jpg", "http://example.com/image4.jpg"));

        Notice notice3 = createAndSaveNotice("Notice 3", "Content 3");
        addImagesToNotice(notice3, List.of("http://example.com/image5.jpg", "http://example.com/image6.jpg"));

        Notice notice4 = createAndSaveNotice("Notice 4", "Content 4");
        addImagesToNotice(notice4, List.of("http://example.com/image7.jpg", "http://example.com/image8.jpg"));

        PageRequest pageRequest = PageRequest.of(0, 3);

        // when
        Page<NoticeResponse> noticeList = noticeRepository.searchNotices(pageRequest, SearchType.TITLE,
                "Notice");

        // then
        assertThat(noticeList).isNotNull();
        assertThat(noticeList.getTotalElements()).isEqualTo(4);
        assertThat(noticeList.getTotalPages()).isEqualTo(2);
        assertThat(noticeList.getNumberOfElements()).isEqualTo(3);
        assertThat(noticeList.getNumber()).isEqualTo(0);
    }

}