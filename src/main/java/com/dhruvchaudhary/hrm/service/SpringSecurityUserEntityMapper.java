package com.dhruvchaudhary.hrm.service;

import java.util.ArrayList;
import java.util.Collection;

import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.EmployeeAdditionalRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhruvchaudhary.hrm.utils.EmployeeStatus;

@SuppressWarnings("deprecation")
@Service
public class SpringSecurityUserEntityMapper {
	
	@Transactional(readOnly = true)
	User buildUserFromEmployee(Employee employee) {
		String username = Integer.toString(employee.getEmpCode());
		String password = employee.getPassword();
		boolean enabled = !(employee.getStatus() == EmployeeStatus.RESIGNED);
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		authorities.add(new GrantedAuthorityImpl("ROLE_EMPLOYEE"));
		for (EmployeeAdditionalRoles role: employee.getRoles())
			authorities.add(new GrantedAuthorityImpl(role.getDescription()));
		
		return new User(username, password, enabled, true, true, true, authorities);
	}
}
