package com.dhruvchaudhary.hrm.customValidators.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhruvchaudhary.hrm.customValidators.annotations.MustBeManager;
import com.dhruvchaudhary.hrm.dao.interfaces.EmployeeDAO;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.EmployeeAdditionalRoles;

@Service
@Transactional
public class ManagerValidator implements ConstraintValidator<MustBeManager, String> {

	@Autowired
	EmployeeDAO employeeDAOImpl;
	
	/*@Autowired
	private SessionFactory sessionfactory;*/
	
	MustBeManager annotation;
	@Override
	public void initialize(MustBeManager arg0) {
		this.annotation = arg0;
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext context) {
		/*sessionfactory.openSession();*/
		try {
			if(arg0.length() == 0)
				return true;
			int mgrCode = Integer.parseInt(arg0);
			Employee mgr = employeeDAOImpl.findById(mgrCode);
			if(mgr == null) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("employee with code " + arg0 + " does not exist.").addConstraintViolation();
			}
			else {
				for(EmployeeAdditionalRoles role:mgr.getRoles())
					if(role.getDescription().equals("ROLE_MANAGER"))
						return true;
				return false;
			}
			
		}catch(NumberFormatException e) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("must be a valid employee code.").addConstraintViolation();
		}
		return false;
	}
	

	

}
