package clovider.clovider_be.domain.application.repository;

import static clovider.clovider_be.domain.enums.DocumentType.DISABILITY;
import static clovider.clovider_be.domain.enums.DocumentType.DUAL_INCOME;
import static clovider.clovider_be.domain.enums.DocumentType.MULTI_CHILDREN;
import static clovider.clovider_be.domain.enums.DocumentType.RESIDENT_REGISTER;
import static clovider.clovider_be.domain.enums.DocumentType.SIBLING;
import static clovider.clovider_be.domain.enums.DocumentType.SINGLE_PARENT;
import static org.junit.jupiter.api.Assertions.*;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.document.repository.ApplicationDocumentRepository;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.DocumentType;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.Save;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.global.config.QuerydslConfig;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@Import(QuerydslConfig.class)
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicationDocumentRepository applicationDocumentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LotteryRepository lotteryRepository;

    private Employee employee;

    List<Map<String, Object>> childrenRecruitList = List.of(
            Map.of(
                    "childNm", "이주애",
                    "recruitIds", List.of(1, 2)
            ),
            Map.of(
                    "childNm", "정준희",
                    "recruitIds", List.of(3, 4)
            )
    );

    Map<DocumentType, String> imageUrls = Map.of(
            RESIDENT_REGISTER, "s3-1",
            DUAL_INCOME, "s3-2",
            SINGLE_PARENT, "s3-3",
            DISABILITY, "s3-4",
            MULTI_CHILDREN, "s3-5",
            SIBLING, "s3-6"
    );

    @BeforeEach
    void setUp(){
        employee = Employee.builder()
                .nameKo("홍길동")
                .accountId("abc")
                .password("1234")
                .employeeNo("E001")
                .joinDt(LocalDate.of(2022, 1, 1))
                .dept("IT")
                .role(Role.EMPLOYEE)
                .build();

        employeeRepository.save(employee);
    }


    public Application createApplication(ApplicationRequest applicationRequest)
    {
        Application savedApplication =
                Application.builder()
                        .employee(employee)
                        .workYears(LocalDate.now().getYear() - employee.getJoinDt().getYear()) //현재 년도 - 입사 년도
                        .isSingleParent(applicationRequest.getIsSingleParent())
                        .childrenCnt(applicationRequest.getChildrenCnt())
                        .isDisability(applicationRequest.getIsDisability())
                        .isDualIncome(applicationRequest.getIsDualIncome())
                        .isEmployeeCouple(applicationRequest.getIsEmployeeCouple())
                        .isSibling(applicationRequest.getIsSibling())
                        .isTemp(Save.APPLIED)
                        .build();

        applicationRepository.save(savedApplication);

        return savedApplication;
    }

    public void createApplicationDocuments(Map<DocumentType, String> imageUrls, Application application) {

        if (imageUrls == null) {
            imageUrls = new HashMap<>();
        }

        imageUrls.forEach((documentType, imageUrl) -> {
            Document document = Document.builder()
                    .image(imageUrl)
                    .application(application)
                    .documentType(documentType)
                    .build();
            applicationDocumentRepository.save(document);
        });
    }

    @Test
    @DisplayName("신청서 작성 및 조회")
    public void createApplicationTest(){
        //GIVEN

        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .isSingleParent('0')
                .childrenCnt(0)
                .isDisability('1')
                .isDualIncome('1')
                .isEmployeeCouple('1')
                .isSibling('1')
                .childrenRecruitList(childrenRecruitList)
                .imageUrls(imageUrls)
                .build();

        Application application = createApplication(applicationRequest);

        createApplicationDocuments(applicationRequest.getImageUrls(), application);

        //WHEN
        Application savedApplication = applicationRepository.findById(application.getId()).orElseThrow();

        //THEN
        assertNotNull(savedApplication);
        assertEquals(application.getIsSingleParent(), savedApplication.getIsSingleParent());
        assertEquals(application.getChildrenCnt(), savedApplication.getChildrenCnt());
        assertEquals(application.getIsDisability(), savedApplication.getIsDisability());
        assertEquals(application.getIsDualIncome(), savedApplication.getIsDualIncome());
        assertEquals(application.getIsEmployeeCouple(), savedApplication.getIsEmployeeCouple());
        assertEquals(application.getIsSibling(), savedApplication.getIsSibling());
        assertEquals(application.getLotteries(), savedApplication.getLotteries());

        List<Document> savedDocuments = applicationDocumentRepository.findByApplicationId(application.getId());
//        assertEquals(imageUrls.size(), savedDocuments.size());
//        for (int i = 0; i < imageUrls.size(); i++) {
//            assertEquals(imageUrls.get(i), savedDocuments.get(i).getImage());
//        }
    }

    @Test
    @DisplayName("신청서 수정 테스트")
    public void updateApplicationTest(){
        //GIVEN
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .isSingleParent('0')
                .childrenCnt(0)
                .isDisability('1')
                .isDualIncome('1')
                .isEmployeeCouple('1')
                .isSibling('1')
                .childrenRecruitList(childrenRecruitList)
                .imageUrls(imageUrls)
                .build();


        Application application = createApplication(applicationRequest);

        Application updatedApplication = application.toBuilder()
                .childrenCnt(3)
                .isSingleParent('0')
                .build();

        applicationRepository.save(updatedApplication);

        // WHEN
        Application savedApplication = applicationRepository.findById(application.getId()).orElseThrow();

        // THEN
        assertEquals(3, savedApplication.getChildrenCnt());
        assertEquals('0', savedApplication.getIsSingleParent());
    }

    @Test
    @DisplayName("신청서 삭제 테스트")
    public void deleteApplicationTest() {
        // GIVEN
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .isSingleParent('0')
                .childrenCnt(0)
                .isDisability('1')
                .isDualIncome('1')
                .isEmployeeCouple('1')
                .isSibling('1')
                .childrenRecruitList(childrenRecruitList)
                .imageUrls(imageUrls)
                .build();


        Application application = createApplication(applicationRequest);

        createApplicationDocuments(imageUrls, application);

        assertNotNull(applicationRepository.findById(application.getId()).orElse(null));
        assertEquals(imageUrls.size(), applicationDocumentRepository.findByApplicationId(application.getId()).size());

        // WHEN
        applicationRepository.delete(application);

        // THEN
        assertFalse(applicationRepository.findById(application.getId()).isPresent());

//        List<Document> remainingDocuments = applicationDocumentRepository.findByApplicationId(application.getId());
//        assertFalse(remainingDocuments.isEmpty());
    }

    @Test
    @DisplayName("신청서 리스트 테스트")
    public void applicationListTest(){
        //GIVEN

        //WHEN

        //THEN
    }

    @Test
    @DisplayName("관리자 신청서 승인 테스트")
    public void applicationAcceptTest(){
        //GIVEN
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .isSingleParent('0')
                .childrenCnt(0)
                .isDisability('1')
                .isDualIncome('1')
                .isEmployeeCouple('1')
                .isSibling('1')
                .childrenRecruitList(childrenRecruitList)
                .imageUrls(imageUrls)
                .build();


        Application application = createApplication(applicationRequest);

        createApplicationDocuments(imageUrls, application);

        Accept accept = Accept.ACCEPT;

        //WHEN
        Application savedApplication = applicationRepository.findById(application.getId()).orElseThrow();
        savedApplication.isAccept(accept);
        applicationRepository.save(savedApplication);

        //THEN
        Application updatedApplication = applicationRepository.findById(application.getId()).orElseThrow();
        assertEquals(accept, updatedApplication.getIsAccept());
    }

}