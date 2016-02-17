package com.dhruvchaudhary.hrm.dao.impl;

import java.util.Date;
import java.util.List;

import com.dhruvchaudhary.hrm.dao.interfaces.ApplicationDAO;
import com.dhruvchaudhary.hrm.model.Application;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.utils.ApplicationTypes;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dhruvchaudhary.hrm.utils.ApplicationStatus;

@Repository
public class ApplicationDAOImpl implements ApplicationDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public int add(Application newApplication) {
		sessionFactory.getCurrentSession().save(newApplication);
		return newApplication.getId();		
	}

	@Override
	public void delete(int id) {		
		sessionFactory.getCurrentSession().delete(findById(id));		
	}

	@Override
	public void edit(Application updatedLeave) {
		sessionFactory.getCurrentSession().update(updatedLeave);
	}

	@Override
	public Application findById(int id) {
		return (Application) sessionFactory.getCurrentSession().get(Application.class,id);
	 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Application> listAll() {		
		return (List<Application>) sessionFactory.getCurrentSession().createCriteria(Application.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Application> listPendingApplicationsByManager(Employee manager) {
		return sessionFactory.getCurrentSession()
							.createQuery("from Application where manager.empCode = :mgrCode and status = :applicationStatus")
							.setParameter("mgrCode", manager.getEmpCode())
							.setParameter("applicationStatus", ApplicationStatus.PENDING)
							.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Application> listAcknowledgedApplicationsByManager(Employee manager) {
		return sessionFactory.getCurrentSession()
							.createQuery("from Application where manager.empCode = :mgrCode and status <> :applicationStatus")
							.setParameter("mgrCode", manager.getEmpCode())
							.setParameter("applicationStatus", ApplicationStatus.PENDING)
							.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Application> listApplicationsByEmployee(Employee employee, ApplicationTypes applicationType) {
		return sessionFactory.getCurrentSession()
							.createQuery("from Application where employee.empCode = :empCode and type = :applicationType")
							.setParameter("empCode", employee.getEmpCode())
							.setParameter("applicationType", applicationType)			
							.list(); 
	}

	@Override
	public boolean checkIfCompoffApplicationExists(Employee employee, Date date, String field) {
			return (sessionFactory.getCurrentSession()
							.createCriteria(Application.class)
							.add(Restrictions.and(
									Restrictions.eq("employee", employee),
									Restrictions.eq(field, date),
									Restrictions.eq("type", ApplicationTypes.COMPENSATORY_OFF))).list().size() > 0);
	}

	@Override
	public boolean checkIfApplicationExists(Employee employee, Date fromDate, Date toDate, ApplicationTypes type) {
		Session currentSession = sessionFactory.getCurrentSession(); 
		String queryString = "from Application where employee.empCode = :empCode ";
		if(type != null) {
			queryString = queryString + "and type = :applicationType ";				
		}
		queryString = queryString +		
					"and status = :status and (" +
					"(:fromDate between fromDate and toDate) "+
					"or " + 
					"(:toDate between fromDate and toDate) " +
					"or " + 
					"(fromDate between :fromDate and :toDate) " + 
					"or " + 
					"(toDate between :fromDate and :toDate))";
		Query q = currentSession.createQuery(queryString);
		q.setParameter("empCode", employee.getEmpCode());
		q.setParameter("fromDate", fromDate);
		q.setParameter("toDate", toDate);
		q.setParameter("status", ApplicationStatus.PENDING);
		if(type != null) {
			q.setParameter("applicationType", type);		
		}		
		return (q.list().size() > 0);
	}
}
