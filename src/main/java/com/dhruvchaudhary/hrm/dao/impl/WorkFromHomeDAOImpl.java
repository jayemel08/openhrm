package com.dhruvchaudhary.hrm.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dhruvchaudhary.hrm.dao.interfaces.WorkFromHomeDAO;
import com.dhruvchaudhary.hrm.model.WorkFromHome;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WorkFromHomeDAOImpl implements WorkFromHomeDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public int add(WorkFromHome newWorkFromHome) {
		sessionFactory.getCurrentSession().save(newWorkFromHome);
		return newWorkFromHome.getId();
	}

	@Override
	public void delete(int id) {
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	@Override
	public void edit(WorkFromHome updatedWorkFromHome) {
		sessionFactory.getCurrentSession().update(updatedWorkFromHome);
	}

	@Override
	public WorkFromHome findById(int id) {
		return (WorkFromHome) sessionFactory.getCurrentSession().get(WorkFromHome.class,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFromHome> listAll() {
		return (List<WorkFromHome>) sessionFactory.getCurrentSession().createCriteria(WorkFromHome.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFromHome> listAll(Date date) {
		return (List<WorkFromHome>) sessionFactory.getCurrentSession()
												.createQuery("from WorkFromHome where date = :date")
												.setParameter("date", date)
												.list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFromHome> listAll(int empCode) {
		return (List<WorkFromHome>) sessionFactory.getCurrentSession()
							.createQuery("from WorkFromHome where employee.empCode = :empCode")
							.setParameter("empCode", empCode)
							.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, String> getEmployeeWiseReport(int mgrCode, Date fromDate, Date toDate) {
		Map<Integer, String>  returnValue = new HashMap<Integer, String>();
		Query q = sessionFactory.getCurrentSession()
								.createQuery("select employee.empCode, count(*) from WorkFromHome where (employee.manager.empCode = :mgrCode) and (date between :fromDate and :toDate) and (cancelDate = null) group by employee")
								.setParameter("mgrCode", mgrCode)
								.setParameter("fromDate", fromDate)
								.setParameter("toDate", toDate);
		for(Iterator<Object> it = q.list().iterator(); it.hasNext();) {
			Object[] values = (Object[]) it.next();
			returnValue.put((int)values[0], Long.toString((long)values[1]));
		}
		return returnValue; 
	}
	
	@Override
	public Map<Integer, String> getManagerWiseReport(int mgrCode, Date fromDate, Date toDate) {
		Map<Integer, String>  returnValue = new HashMap<Integer, String>();
		returnValue.put(mgrCode, Long.toString((long)sessionFactory.getCurrentSession().createQuery("select count(*) from WorkFromHome where ((employee.manager.empCode = :mgrCode) or (employee.empCode = :mgrCode)) and (date between :fromDate and :toDate) and (cancelDate = null) ")
																.setParameter("mgrCode", mgrCode)
																.setParameter("fromDate", fromDate)
																.setParameter("toDate", toDate).list().get(0)));
		return returnValue; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFromHome> getEmployeeReport(int empCode, Date fromDate,	Date toDate) {
		return (List<WorkFromHome>) sessionFactory.getCurrentSession().createQuery("from WorkFromHome where (employee.empCode = :empCode) and (date between :fromDate and :toDate)")
												.setParameter("empCode", empCode)
												.setParameter("fromDate", fromDate)
												.setParameter("toDate", toDate)
												.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFromHome> listAll(int empCode, int applicationId) {
		return (List<WorkFromHome>) sessionFactory.getCurrentSession()
											.createQuery("from WorkFromHome where (employee.empCode = :empCode) and (application.id = :applicationId)")
											.setParameter("empCode", empCode)
											.setParameter("applicationId", applicationId)
											.list();	
	}

	@Override
	public void cancel(int id) {
		sessionFactory.getCurrentSession()
		.createQuery("update WorkFromHome set cancelDate = :cancelDate where id = :id")
		.setParameter("cancelDate", new Date())
		.setParameter("id", id)
		.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFromHome> listAll(Date fromDate, Date toDate, int empCode) {
		return sessionFactory.getCurrentSession()
				.createQuery("from WorkFromHome where employee.empCode = :empCode and (date between :fromDate and :toDate) and cancelDate is null")
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("empCode", empCode)
				.list();
	}
}
