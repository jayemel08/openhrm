package com.dhruvchaudhary.hrm.customValidators.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;

import com.dhruvchaudhary.hrm.customValidators.validators.ManagerValidator;

@Documented
@Constraint(validatedBy = {ManagerValidator.class})
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
@NotNull
public @interface MustBeManager {
	String message() default "employee is not a manager";
	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
