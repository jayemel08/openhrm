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

import com.dhruvchaudhary.hrm.customValidators.validators.IntegerValidator;

@Documented
@Constraint(validatedBy = {IntegerValidator.class})
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
@NotNull
public @interface MustBeInteger {
	int min() default Integer.MIN_VALUE;
	int max() default Integer.MAX_VALUE;
	String message() default "must be a valid integer";
	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
