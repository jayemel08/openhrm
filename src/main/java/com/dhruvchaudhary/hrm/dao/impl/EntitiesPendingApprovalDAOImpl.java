package com.dhruvchaudhary.hrm.dao.impl;

import java.util.Date;
import java.util.List;

import com.dhruvchaudhary.hrm.model.EntitiesPendingApproval;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;
import org.hibernate.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dhruvchaudhary.hrm.dao.interfaces.EntitiesPendingApprovalDAO;

@Repository("entitiesPendingApprovalDAOImpl")
public class EntitiesPendingApprovalDAOImpl implements
		EntitiesPendingApprovalDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public int add(EntitiesPendingApproval newEntity) {
		sessionFactory.getCurrentSession().save(newEntity);
		return newEntity.getId();
	}

	@Override
	public void delete(int id) { 
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	@Override
	public void edit(EntitiesPendingApproval updatedEntity) {
		sessionFactory.getCurrentSession().update(updatedEntity);
	}

	@Override
	public EntitiesPendingApproval findById(int id) {
		return (EntitiesPendingApproval) sessionFactory.getCurrentSession().get(EntitiesPendingApproval.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntitiesPendingApproval> listAll() {
		return sessionFactory.getCurrentSession().createQuery("from EntitiesPendingApproval").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntitiesPendingApproval> listAll(int empCode,
			ApplicationTypes applictaionType) {
		return sessionFactory.getCurrentSession()
				.createQuery("from EntitiesPendingApproval where employee.empCode = :empCode and application.type = :applicationType order by date asc")
				.setParameter("empCode", empCode)
				.setParameter("applicationType", applictaionType)
				.list();
	}

	@Override
	public void deleteAllByApplication(int applicationID) {
		sessionFactory.getCurrentSession()
				.createQuery("delete from EntitiesPendingApproval where application.id = :applicationID")
				.setParameter("applicationID", applicationID)
				.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntitiesPendingApproval> listAll(Date fromDate, Date toDate,
			int empCode, ApplicationTypes applicationType) {
		String hql = "from EntitiesPendingApproval where employee.empCode = :empCode and (date between :fromDate and :toDate)";
		if(applicationType!=null) {
			hql += " and application.type = :applicationType";
		}
		
		Query q = sessionFactory.getCurrentSession()
						.createQuery(hql)
						.setParameter("empCode", empCode)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate);
		
		if(applicationType != null)
			q.setParameter("applicationType", applicationType);
		
		return q.list();
	}

}
