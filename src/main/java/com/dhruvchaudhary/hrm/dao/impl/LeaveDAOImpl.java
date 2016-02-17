package com.dhruvchaudhary.hrm.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dhruvchaudhary.hrm.dao.interfaces.LeaveDAO;
import com.dhruvchaudhary.hrm.model.Leave;
import com.dhruvchaudhary.hrm.utils.LeaveType;

@Repository
public class LeaveDAOImpl implements LeaveDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public int add(Leave newLeave) {
		sessionFactory.getCurrentSession().save(newLeave);
		return newLeave.getId();
	}

	@Override
	public void delete(int id) {
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	@Override
	public void edit(Leave updatedLeave) {
		sessionFactory.getCurrentSession().update(updatedLeave);
	}

	@Override
	public Leave findById(int id) {
		return (Leave) sessionFactory.getCurrentSession().get(Leave.class,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> listAll() {
		return (List<Leave>) sessionFactory.getCurrentSession().createCriteria(Leave.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> listAll(int empCode, LeaveType type) {
		return (List<Leave>)sessionFactory.getCurrentSession()
								.createQuery("from Leave where employee.empCode = :empCode and leaveType = :type order by date desc")
								.setParameter("empCode", empCode)
								.setParameter("type", type)
								.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> listAll(Date date) {
		return (List<Leave>)sessionFactory.getCurrentSession()
								.createQuery("from Leave where date = :date")
								.setParameter("date", date)
								.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, ArrayList<String>> getEmployeeWiseReport(int mgrCode, Date fromDate, Date toDate) {
		Map<Integer, ArrayList<String>> returnValue = new HashMap<Integer, ArrayList<String>>();
		
		Query q = sessionFactory.getCurrentSession()
								.createQuery("select employee.empCode, leaveType, count(leaveType) from Leave where (employee.manager.empCode = :mgrCode) and (date between :fromDate and :toDate) and (cancelDate = null) group by employee, leaveType")
								.setParameter("mgrCode", mgrCode)
								.setParameter("fromDate", fromDate)
								.setParameter("toDate", toDate);
		int empCode = -1;
		long l = 0, lwp = 0, lc = 0;
		for(Iterator<Object> it = q.list().iterator(); it.hasNext();) {
			Object[] values = (Object[]) it.next();
			if(empCode != (int) values[0]) {
				empCode = (int) values[0];
				ArrayList<String> list = new ArrayList<String>();					
				list.add(Long.toString(l));
				list.add(Long.toString(lc));
				list.add(Long.toString(lwp));
				returnValue.put(empCode, list);
				l = lwp = lc = 0;
			}
			if(LeaveType.LEAVE_PAID == (LeaveType)values[1])
				l = (long) values[2];
			else if(LeaveType.LEAVE_AGAINST_COMP_OFF == (LeaveType)values[1])
				lc = (long) values[2];
			else
				lwp = (long) values[2];
		}
		ArrayList<String> list = new ArrayList<String>();
		list.add(Long.toString(l));
		list.add(Long.toString(lwp));
		list.add(Long.toString(lc));
		returnValue.put(empCode, list);
		return returnValue;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, ArrayList<String>> getManagerWiseReport(int mgrCode, Date fromDate, Date toDate) {
		Map<Integer, ArrayList<String>> returnValue = new HashMap<Integer, ArrayList<String>>();
		Query q = sessionFactory.getCurrentSession().createQuery("select leaveType, count(leaveType) from Leave where ((employee.manager.empCode = :mgrCode) or (employee.empCode = :mgrCode)) and (date between :fromDate and :toDate) and (cancelDate = null) group by leaveType");
		q.setParameter("mgrCode", mgrCode);
		q.setParameter("fromDate", fromDate);
		q.setParameter("toDate", toDate);
		long l = 0, lwp = 0, lc = 0;
		for(Iterator<Object> it = q.list().iterator(); it.hasNext();) {
			Object[] values = (Object[]) it.next();
			if(LeaveType.LEAVE_PAID == (LeaveType)values[0])
				l = (long) values[1];
			else if(LeaveType.LEAVE_AGAINST_COMP_OFF == (LeaveType)values[0])
				lc = (long) values[1];
			else
				lwp = (long) values[1];
		}
		ArrayList<String> list = new ArrayList<String>();
		list.add(Long.toString(l));
		list.add(Long.toString(lwp));
		list.add(Long.toString(lc));
		returnValue.put(mgrCode, list);
		return returnValue; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> getEmployeeReport(int empCode, Date fromDate, Date toDate) {
		return (List<Leave>)sessionFactory.getCurrentSession()
								.createQuery("from Leave where (employee.empCode = :empCode) and (date between :fromDate and :toDate)")
								.setParameter("fromDate", fromDate)
								.setParameter("toDate", toDate)
								.setParameter("empCode", empCode)
								.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> listAll(int empCode, int applicationID) {
		return (List<Leave>)sessionFactory.getCurrentSession()
								.createQuery("from Leave where (employee.empCode = :empCode) and (application.id = :applicationID)")
								.setParameter("empCode", empCode)
								.setParameter("applicationID", applicationID)
								.list();
	}

	@Override
	public void cancel(int id) {
		sessionFactory.getCurrentSession()
			.createQuery("update Leave set cancelDate = :cancelDate where id = :id")
			.setParameter("cancelDate", new Date())
			.setParameter("id", id)
			.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> listAll(Date fromDate, Date toDate, int empCode) {
		return sessionFactory.getCurrentSession()
							.createQuery("from Leave where employee.empCode = :empCode and (date between :fromDate and :toDate) and cancelDate is null")
							.setParameter("fromDate", fromDate)
							.setParameter("toDate", toDate)
							.setParameter("empCode", empCode)
							.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> listAll(Date fromDate, Date toDate, int empCode,
			LeaveType type) {
		return sessionFactory.getCurrentSession()
				.createQuery("from Leave where employee.empCode = :empCode and (date between :fromDate and :toDate) and leaveType = :type and cancelDate is null")
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("empCode", empCode)
				.setParameter("type", type)
				.list();
	}
	
	

}
