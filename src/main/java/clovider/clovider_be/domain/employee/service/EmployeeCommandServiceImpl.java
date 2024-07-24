package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public CustomResult changePassword(Employee employee, String newPassword) {

        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        employee.changePassword(encodePassword);

        return CustomResult.toCustomResult(employee.getId());
    }

}
