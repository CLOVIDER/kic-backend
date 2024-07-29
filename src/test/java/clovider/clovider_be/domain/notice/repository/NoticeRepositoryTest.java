package clovider.clovider_be.domain.notice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.noticeImage.NoticeImage;
import clovider.clovider_be.domain.noticeImage.repository.NoticeImageRepository;
import clovider.clovider_be.global.config.QuerydslConfig;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private static Employee admin;

    @BeforeEach
    void setup(){
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

    @Test
    @DisplayName("공지사항 생성 및 조회 테스트")
    public void createNoticeTest() {
        // given
        Notice notice = Notice.builder()
                .title("공지사항 제목")
                .content("공지사항 내용")
                .admin(admin)
                .build();

        Notice savedNotice = noticeRepository.save(notice);

        NoticeImage image1 = NoticeImage.builder()
                .image("http://example.com/image1.jpg")
                .notice(savedNotice)
                .build();
        NoticeImage image2 = NoticeImage.builder()
                .image("http://example.com/image2.jpg")
                .notice(savedNotice)
                .build();

        savedNotice.getImages().add(image1);
        savedNotice.getImages().add(image2);

        // when
        Notice resultNotice = noticeRepository.findById(savedNotice.getId()).orElse(null);

        // then
        assertNotNull(resultNotice);
        assertThat(resultNotice.getId()).isNotNull();
        assertThat(resultNotice.getImages()).hasSize(2);
        assertThat(resultNotice.getImages().get(0).getImage()).isEqualTo("http://example.com/image1.jpg");
        assertThat(resultNotice.getImages().get(1).getImage()).isEqualTo("http://example.com/image2.jpg");
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    public void updateNoticeImagesTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Notice notice = Notice.builder()
                .title("원래 제목")
                .content("원래 내용")
                .admin(admin)
                .build();
        Notice savedNotice = noticeRepository.save(notice);

        NoticeImage image1 = NoticeImage.builder()
                .image("http://example.com/old_image1.jpg")
                .notice(savedNotice)
                .build();

        savedNotice.getImages().add(image1);

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
        Notice notice = Notice.builder()
                .title("공지사항 제목")
                .content("공지사항 내용")
                .admin(admin)
                .build();
        Notice savedNotice = noticeRepository.save(notice);

        NoticeImage image1 = NoticeImage.builder()
                .image("http://example.com/image1.jpg")
                .notice(savedNotice)
                .build();
        NoticeImage image2 = NoticeImage.builder()
                .image("http://example.com/image2.jpg")
                .notice(savedNotice)
                .build();

        savedNotice.getImages().add(image1);
        savedNotice.getImages().add(image2);

        // when
        noticeRepository.deleteById(savedNotice.getId());

        // then
        assertFalse(noticeRepository.findById(savedNotice.getId()).isPresent());
    }



}