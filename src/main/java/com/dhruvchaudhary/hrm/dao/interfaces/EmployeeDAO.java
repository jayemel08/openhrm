package com.dhruvchaudhary.hrm.dao.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.utils.EmployeeStatus;

public interface EmployeeDAO extends GenericDAO<Employee>{
	public void changePassword(Employee updatedEmployee);
	public List<Employee> listAll(String criteria);
	public int getEmployeeCount(EmployeeStatus status);
	public Map<Integer, ArrayList<String>> listAllByManager(int mgrCode);
	public List<Employee> listAllManagers();
	public Employee findByEmail(String email);
	public void changePassword(int empCode, String password);
}
