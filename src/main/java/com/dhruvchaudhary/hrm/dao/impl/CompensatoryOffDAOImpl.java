package com.dhruvchaudhary.hrm.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dhruvchaudhary.hrm.dao.interfaces.CompensatoryOffDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dhruvchaudhary.hrm.model.CompensatoryOff;
import com.dhruvchaudhary.hrm.model.Employee;

@Repository
public class CompensatoryOffDAOImpl implements CompensatoryOffDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public int add(CompensatoryOff newCompOff) {
		sessionFactory.getCurrentSession().save(newCompOff);
		return newCompOff.getId();
	}

	@Override
	public void delete(int id) {
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	@Override
	public void edit(CompensatoryOff updatedCompOff) {
		sessionFactory.getCurrentSession().update(updatedCompOff);
	}

	@Override
	public CompensatoryOff findById(int id) {
		return (CompensatoryOff) sessionFactory.getCurrentSession().get(CompensatoryOff.class,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompensatoryOff> listAll() {
		return (List<CompensatoryOff>) sessionFactory.getCurrentSession().createCriteria(CompensatoryOff.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompensatoryOff> getValidUnusedCompensatoryOffs(Employee employee, Date date) {
			Calendar validDate = Calendar.getInstance();
			validDate.setTime(date);
			validDate.add(Calendar.DATE, -90);
			System.out.println(validDate.getTime().toString());
			return sessionFactory.getCurrentSession().createQuery("from CompensatoryOff where (appliedAgainst between :fromDate and :toDate) and employee.empCode = :empCode and leave is null order by appliedAgainst asc")
													.setParameter("empCode", employee.getEmpCode())
													.setParameter("fromDate", validDate.getTime())
													.setParameter("toDate", date)
													.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompensatoryOff> listAll(int empCode) {
		return sessionFactory.getCurrentSession().createQuery("from CompensatoryOff where employee.empCode = :empCode")
												.setParameter("empCode", empCode)
												.list();
	}	

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, String> getEmployeeWiseReport(int mgrCode, Date fromDate, Date toDate) {
		Map<Integer, String>  returnValue = new HashMap<Integer, String>();
		for(Iterator<Object> it = sessionFactory.getCurrentSession().createQuery("select employee.empCode, count(*) from CompensatoryOff where (employee.manager.empCode = :mgrCode) and (appliedAgainst between :fromDate and :toDate) group by employee")
										.setParameter("mgrCode", mgrCode)
										.setParameter("fromDate", fromDate)
										.setParameter("toDate", toDate)
										.list()
										.iterator(); 
				it.hasNext();) {
			Object[] values = (Object[]) it.next();
			returnValue.put((int)values[0], Long.toString((long)values[1]));
		}
		return returnValue; 
	}
	
	
	@Override
	public Map<Integer, String> getManagerWiseReport(int mgrCode, Date fromDate, Date toDate) {
		Map<Integer, String>  returnValue = new HashMap<Integer, String>();
		
		returnValue.put(mgrCode, Long.toString((long)sessionFactory.getCurrentSession().createQuery("select count(*) from CompensatoryOff where ((employee.manager.empCode = :mgrCode) or (employee.empCode = :mgrCode)) and (appliedAgainst between :fromDate and :toDate)")
																						.setParameter("mgrCode", mgrCode)
																						.setParameter("fromDate", fromDate)
																						.setParameter("toDate", toDate)
																						.list()
																						.get(0)));
		return returnValue; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompensatoryOff> getEmployeeReport(int empCode, Date fromDate, Date toDate) {
		return (List<CompensatoryOff>) sessionFactory.getCurrentSession().createQuery("from CompensatoryOff where (employee.empCode = :empCode) and (appliedAgainst between :fromDate and :toDate)")
																		.setParameter("empCode", empCode)
																		.setParameter("fromDate", fromDate)
																		.setParameter("toDate", toDate)
																		.list();
	}

	@Override
	public void cancelLeaveAgainstCompensatoryOff(int leaveId) {
		sessionFactory.getCurrentSession()
					.createQuery("update CompensatoryOff set leave = null where leave.id = :leaveId")
					.setParameter("leaveId", leaveId)
					.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompensatoryOff> listAll(Date fromDate, Date toDate, int empCode) {
		return sessionFactory.getCurrentSession()
				.createQuery("from CompensatoryOff where employee.empCode = :empCode and (appliedAgainst between :fromDate and :toDate)")
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("empCode", empCode)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompensatoryOff> getAllUnusedCompOffs(int empCode) {
		return sessionFactory.getCurrentSession()
					.createQuery("from CompensatoryOff where employee.empCode = :empCode and leave is null order by appliedAgainst asc")
					.setParameter("empCode", empCode)
					.list();
	}
}
