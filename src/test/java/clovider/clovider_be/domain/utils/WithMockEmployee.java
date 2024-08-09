package clovider.clovider_be.domain.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomEmployeeSecurityContextFactory.class)
public @interface WithMockEmployee {
}