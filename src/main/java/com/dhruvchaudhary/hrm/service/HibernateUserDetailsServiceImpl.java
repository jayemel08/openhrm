package com.dhruvchaudhary.hrm.service;

import com.dhruvchaudhary.hrm.dao.interfaces.GenericDAO;
import com.dhruvchaudhary.hrm.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HibernateUserDetailsServiceImpl implements UserDetailsService {

	private GenericDAO<Employee> employeeDAOImpl;
	@Autowired
	private SpringSecurityUserEntityMapper assembler;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		int empCode = Integer.parseInt(username); 
		Employee employee = employeeDAOImpl.findById(empCode);
		return assembler.buildUserFromEmployee(employee);
	}

	public GenericDAO<Employee> getEmployeeDAOImpl() {
		return employeeDAOImpl;
	}

	public void setEmployeeDAOImpl(GenericDAO<Employee> employeeDAOImpl) {
		this.employeeDAOImpl = employeeDAOImpl;
	}

}
