package clovider.clovider_be.domain.application.service;

import static clovider.clovider_be.domain.application.QApplication.application;
import static clovider.clovider_be.domain.enums.DocumentType.DISABILITY;
import static clovider.clovider_be.domain.enums.DocumentType.DUAL_INCOME;
import static clovider.clovider_be.domain.enums.DocumentType.MULTI_CHILDREN;
import static clovider.clovider_be.domain.enums.DocumentType.RESIDENT_REGISTER;
import static clovider.clovider_be.domain.enums.DocumentType.SIBLING;
import static clovider.clovider_be.domain.enums.DocumentType.SINGLE_PARENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.document.service.ApplicationDocumentCommandService;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.DocumentType;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.lottery.service.LotteryCommandService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplicationCommandServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;



    @Mock
    private ApplicationDocumentCommandService applicationDocumentCommandService;

    @Mock
    private LotteryCommandService lotteryCommandService;

    @InjectMocks
    private ApplicationCommandServiceImpl applicationCommandService;

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
    void setUp() {

        employee = Employee.builder()
                .nameKo("홍길동")
                .accountId("abc")
                .password("1234")
                .employeeNo("E001")
                .joinDt(LocalDate.of(2022, 1, 1))
                .dept("IT")
                .role(Role.EMPLOYEE)
                .build();

        MockitoAnnotations.openMocks(this);
    }

    public Application createApplication(ApplicationRequest applicationRequest)
    {
        return
                Application.builder()
                        .employee(employee)
                        .workYears(LocalDate.now().getYear() - employee.getJoinDt().getYear()) //현재 년도 - 입사 년도
                        .isSingleParent(applicationRequest.getIsSingleParent())
                        .childrenCnt(applicationRequest.getChildrenCnt())
                        .isDisability(applicationRequest.getIsDisability())
                        .isDualIncome(applicationRequest.getIsDualIncome())
                        .isEmployeeCouple(applicationRequest.getIsEmployeeCouple())
                        .isSibling(applicationRequest.getIsSibling())
                        .isTemp('0')
                        .build();

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
        });
    }

    @Test
    @DisplayName(value = "신청서 작성 테스트")
    void applicationCreate() {
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
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        CustomResult result = applicationCommandService.applicationCreate(applicationRequest, employee);

        //THEN
        assertNotNull(result);
        verify(applicationRepository, times(1)).save(any(Application.class));
        verify(lotteryCommandService, times(1)).insertLottery(anyList(), anyLong());
    }

    @Test
    @DisplayName(value = "신청서 수정 테스트")
    void applicationUpdate() {

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

        when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(updatedApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(updatedApplication);

        CustomResult result = applicationCommandService.applicationUpdate(application.getId(), applicationRequest);

        assertNotNull(result);
        verify(applicationRepository, times(1)).findById(anyLong());
        verify(applicationRepository, times(1)).save(any(Application.class));

        assertEquals(updatedApplication.getIsSingleParent(), application.getIsSingleParent());
        assertEquals(updatedApplication.getChildrenCnt(), application.getChildrenCnt());
        assertEquals(updatedApplication.getIsDisability(), application.getIsDisability());
        assertEquals(updatedApplication.getIsDualIncome(), application.getIsDualIncome());
        assertEquals(updatedApplication.getIsEmployeeCouple(), application.getIsEmployeeCouple());
        assertEquals(updatedApplication.getIsSibling(), application.getIsSibling());
    }

    @Test
    @DisplayName(value = "신청서 삭제 테스트")
    void applicationDelete() {
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

        when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));

        CustomResult result = applicationCommandService.applicationDelete(application.getId());

        assertNotNull(result);
        verify(applicationRepository, times(1)).findById(anyLong());
        verify(applicationRepository, times(1)).delete(any(Application.class));

    }

    @Test
    @DisplayName(value = "신청서 임시저장 테스트")
    void applicationTempSave() {
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

        Application tempApplication = Application.builder()
                .employee(employee)
                .workYears(LocalDate.now().getYear() - employee.getJoinDt().getYear())
                .isSingleParent(applicationRequest.getIsSingleParent())
                .childrenCnt(applicationRequest.getChildrenCnt())
                .isDisability(applicationRequest.getIsDisability())
                .isDualIncome(applicationRequest.getIsDualIncome())
                .isEmployeeCouple(applicationRequest.getIsEmployeeCouple())
                .isSibling(applicationRequest.getIsSibling())
                .isTemp('1')
                .build();

        when(applicationRepository.save(any(Application.class))).thenReturn(tempApplication);

        CustomResult result = applicationCommandService.applicationTempSave(applicationRequest, employee);

        assertNotNull(result);
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    @DisplayName(value = "관리자 신청서 승인 테스트")
    void applicationAccept() {
        Application application = Application.builder()
                .employee(employee)
                .workYears(LocalDate.now().getYear() - employee.getJoinDt().getYear())
                .isSingleParent('0')
                .childrenCnt(0)
                .isDisability('0')
                .isDualIncome('0')
                .isEmployeeCouple('0')
                .isSibling('0')
                .isTemp('0')
                .build();

        when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> {
            Application app = invocation.getArgument(0);
            app.isAccept(Accept.ACCEPT); // Assuming Accept.ACCEPTED is a valid enum value
            return app;
        });

        CustomResult result = applicationCommandService.applicationAccept(1L, Accept.ACCEPT);

        assertNotNull(result);
        verify(applicationRepository, times(1)).findById(anyLong());
        verify(applicationRepository, times(1)).save(any(Application.class));

        // Verify that the isAccept method was called
        assertEquals(Accept.ACCEPT, application.getIsAccept());
    }
}