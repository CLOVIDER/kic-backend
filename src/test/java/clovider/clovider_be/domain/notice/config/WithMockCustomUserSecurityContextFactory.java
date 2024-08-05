package clovider.clovider_be.domain.notice.config;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.global.security.CustomUserDetails;
import java.time.LocalDate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockAdmin> {

    @Override
    public SecurityContext createSecurityContext(WithMockAdmin annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Employee employee = Employee.builder()
                .id(Long.parseLong(annotation.username()))
                .nameKo("Test Name")
                .accountId("testAccountId")
                .password("testPassword")
                .employeeNo("12345")
                .joinDt(LocalDate.now())
                .dept("Test Dept")
                .role(Role.ADMIN)
                .build();

        UserDetails userDetails = new CustomUserDetails(employee);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        context.setAuthentication(auth);

        return context;

    }
}