package com.dhruvchaudhary.hrm.dao.interfaces;

import com.dhruvchaudhary.hrm.model.EmployeeAdditionalRoles;

public interface EmployeeAdditionalRolesDAO extends GenericDAO<EmployeeAdditionalRoles>{
	public EmployeeAdditionalRoles getRoleByDescription(String description);
}
