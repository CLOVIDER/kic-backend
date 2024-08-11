package clovider.clovider_be.domain.employee.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.service.ApplicationQueryServiceImpl;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.dto.EmployeeResponse.EmployeeInfo;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.utils.CreateUtil;
import clovider.clovider_be.domain.utils.dto.TestDto.LoginRequest;
import clovider.clovider_be.global.exception.ApiException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class EmployeeQueryServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ApplicationQueryServiceImpl applicationQueryService;

    @InjectMocks
    private EmployeeQueryServiceImpl employeeQueryService;

    @Test
    @DisplayName("사내 직원 조회")
    void getEmployee() throws NoSuchFieldException, IllegalAccessException {

        // given
        List<Employee> employeeList = CreateUtil.getEmployeeList();
        Employee employee1 = employeeList.get(0);

        // when
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        Employee employee = employeeQueryService.getEmployee(employee1.getId());

        // then
        verify(employeeRepository, times(1)).findById(1L);
        assertThat(employee).isSameAs(employee1);
        assertThat(employee.getNameKo()).isEqualTo("준희");
        assertThat(employee.getEmployeeNo()).isEqualTo("20292929");
        assertThat(employee.getRole()).isInstanceOf(Role.class);

    }

    @Test
    @DisplayName("ID & PWD 검증")
    void checkAccountIdAndPwd() throws NoSuchFieldException, IllegalAccessException {

        // given
        LoginRequest loginRequest = new LoginRequest("qwer123", "1234");
        List<Employee> employeeList = CreateUtil.getEmployeeList();
        Employee employee = employeeList.get(0);
        System.out.println("employee = " + employee.getPassword());

        // when
        when(employeeRepository.findByAccountId(loginRequest.getAccountId())).thenReturn(
                Optional.of(employee));
        when(bCryptPasswordEncoder.matches(loginRequest.getPassword(),
                employee.getPassword())).thenReturn(true);
        Employee employee1 = employeeQueryService.checkAccountIdAndPwd(loginRequest);

        // then
        assertThat(employee).isEqualTo(employee1);
    }

    @Test
    @DisplayName("Bcrypt PWD 검증 오류 테스트")
    void checkAccountIdAndPwdThrowsAuthInvaild()
            throws NoSuchFieldException, IllegalAccessException {

        // given
        LoginRequest loginRequest = new LoginRequest("qwer123", "1234");
        List<Employee> employeeList = CreateUtil.getEmployeeList();
        Employee employee = employeeList.get(0);

        // when
        when(employeeRepository.findByAccountId(loginRequest.getAccountId())).thenReturn(
                Optional.of(employee));

        // then
        assertThrows(ApiException.class,
                () -> employeeQueryService.checkAccountIdAndPwd(loginRequest));

    }

    @Test
    @DisplayName("신청서 ID를 통한 직원 정보 조회")
    void getEmployeeInfo() throws NoSuchFieldException, IllegalAccessException {

        // given
        List<Application> applicationList = CreateUtil.getApplicationList();
        Application application = applicationList.get(0);

        // when
        when(applicationQueryService.getApplicationWithEmployee(application.getId())).thenReturn(
                application);
        EmployeeInfo employeeInfo1 = employeeQueryService.getEmployeeInfo(application.getId());

        // then
        verify(applicationQueryService, times(1)).getApplicationWithEmployee(application.getId());
        assertThat(employeeInfo1).isInstanceOf(EmployeeInfo.class);
        assertThat(employeeInfo1.getWorkedAt()).isNotEmpty();
        assertThat(employeeInfo1.getEmployeeNo()).isEqualTo("20292929");

    }
}