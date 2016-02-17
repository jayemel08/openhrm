package com.dhruvchaudhary.hrm.dao.interfaces;

import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.PasswordReset;

public interface PasswordResetDAO extends GenericDAO<PasswordReset> {
	public void deleteAllByEmployee(int empCode);
	public Employee findByAuthCode(String authCode);
}
