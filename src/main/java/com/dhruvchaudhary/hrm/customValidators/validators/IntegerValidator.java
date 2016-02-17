package com.dhruvchaudhary.hrm.customValidators.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.dhruvchaudhary.hrm.customValidators.annotations.MustBeInteger;

public class IntegerValidator implements ConstraintValidator<MustBeInteger, String> {

	MustBeInteger mbi;
	@Override
	public void initialize(MustBeInteger arg0) {
		this.mbi = arg0;
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext context) {
		try {
			int i = Integer.parseInt(arg0);
			if((i>=mbi.min()) && (i<=mbi.max())) {
				return true;
			}
			else {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("value must be between " + mbi.min() + " and " + mbi.max()).addConstraintViolation();
			}
		}catch(NumberFormatException e) {
		}
		return false;
	}
	

	

}
