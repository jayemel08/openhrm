package com.dhruvchaudhary.hrm.dao.interfaces;

import java.util.Date;
import java.util.List;

import com.dhruvchaudhary.hrm.model.EntitiesPendingApproval;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;

public interface EntitiesPendingApprovalDAO extends GenericDAO<EntitiesPendingApproval> {
	public List<EntitiesPendingApproval> listAll(int empCode, ApplicationTypes applictaionType);
	public void deleteAllByApplication(int applicationID);
	public List<EntitiesPendingApproval> listAll(Date fromDate, Date toDate, int empCode, ApplicationTypes applicationType);
}
