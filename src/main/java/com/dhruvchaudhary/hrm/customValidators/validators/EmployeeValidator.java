package com.dhruvchaudhary.hrm.customValidators.validators;

import com.dhruvchaudhary.hrm.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.dhruvchaudhary.hrm.dao.interfaces.EmployeeDAO;

@Component
public class EmployeeValidator implements Validator {

	@Autowired
	private EmployeeDAO employeeDAO;
	
	/*@Autowired
	private SessionFactory sessionFactory;*/
	
	@Override
	public boolean supports(Class<?> arg0) {
		return EmployeeDTO.class.isAssignableFrom(arg0);
	}

	@Override
	
	public void validate(Object obj, Errors errors) {
		/*sessionFactory.openSession();*/
		EmployeeDTO employee = (EmployeeDTO) obj;
		if(employeeDAO.findById(Integer.parseInt(employee.getEmpCode())) != null) {
			errors.rejectValue("empCode", "empCode", "Employee with code " + employee.getEmpCode() + " already exists");
		}
		if(employeeDAO.findByEmail(employee.getEmail()) != null) {
			errors.rejectValue("email", "email", "email must be unique");
		}
		/*sessionFactory.getCurrentSession().close();*/
	}
}
