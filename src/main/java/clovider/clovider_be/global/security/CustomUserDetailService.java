package clovider.clovider_be.global.security;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final EmployeeQueryService employeeQueryService;

    @Override
    public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {

        Employee employee = employeeQueryService.getEmployee(Long.parseLong(employeeId));

        return new CustomUserDetails(employee);
    }
}
