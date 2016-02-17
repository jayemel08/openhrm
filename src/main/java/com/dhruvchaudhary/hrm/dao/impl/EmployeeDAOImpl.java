package com.dhruvchaudhary.hrm.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dhruvchaudhary.hrm.dao.interfaces.EmployeeDAO;
import com.dhruvchaudhary.hrm.model.Employee;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dhruvchaudhary.hrm.utils.EmployeeStatus;

@Repository("employeeDAOImpl")
public class EmployeeDAOImpl implements EmployeeDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public int add(Employee newEmployee) {
		sessionFactory.getCurrentSession().save(newEmployee);
		return newEmployee.getEmpCode();
	}

	@Override
	public void delete(int empCode) {
		sessionFactory.getCurrentSession().delete(findById(empCode));
	}

	@Override
	@Transactional
	public void edit(Employee updatedEmployee) {
		sessionFactory.getCurrentSession().merge(updatedEmployee);
	}

	@Override
	@Transactional
	public Employee findById(int empCode) {
		return (Employee) sessionFactory.getCurrentSession().get(Employee.class,empCode);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> listAll() {
		return sessionFactory.getCurrentSession()
							.createQuery("from Employee")
							.list();
	}
	
	@Override
	public void changePassword(Employee updatedEmployee) {
		sessionFactory.getCurrentSession()
					.createQuery("update employee e set e.password=? where e.empCode=?")
					.setParameter(0, updatedEmployee.getPassword())
					.setParameter(1, updatedEmployee.getEmpCode())
					.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> listAll(String criteria) {
		List<Employee> returnValue = new ArrayList<Employee>();
		Session currentSession = null;
		try {
			currentSession = sessionFactory.openSession(); 
			Criteria cr = currentSession.createCriteria(Employee.class);
			int empCode;
			
			try{
				empCode = Integer.parseInt(criteria);
				cr.add(Restrictions.eq("empCode", empCode));
			} catch (NumberFormatException e) {
				cr.add(Restrictions.like("name", "%" + criteria + "%"));
			}

			for(Employee employee:(ArrayList<Employee>) cr.list()){
				if(!returnValue.contains(employee))
					returnValue.add(employee);
			}
		
			cr = null;
			cr = currentSession.createCriteria(Employee.class)
								.createAlias("manager", "M")
								.add(Restrictions.like("M.name", "%" + criteria + "%"));
			
			for(Employee employee:(ArrayList<Employee>) cr.list()){
				if(!returnValue.contains(employee)) {
					returnValue.add(employee);
				}
			}
			
		} catch (HibernateException e) {
			throw e;
		}
		finally {
			if(currentSession != null)
				currentSession.close();
		}
		return returnValue; 
	}

	@Override
	public int getEmployeeCount(EmployeeStatus status) {
		return (int) ((long) sessionFactory.getCurrentSession()
							.createQuery("select count(*) from Employee where status = :status")
							.setParameter("status", status)
							.list()
							.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, ArrayList<String>> listAllByManager(int mgrCode) {
		Map<Integer, ArrayList<String>> returnValue = new HashMap<Integer, ArrayList<String>>();
		for(Iterator<Object> it = sessionFactory.getCurrentSession()
												.createQuery("select empCode, name from Employee where manager.empCode = :mgrCode")
												.setParameter("mgrCode", mgrCode)
												.list()
												.iterator();
				it.hasNext();) {
				Object[] values = (Object[])it.next();
				ArrayList<String> list= new ArrayList<String>();
				list.add((String)values[1]);
				list.add("0");
				list.add("0");
				list.add("0");
				list.add("0");
				list.add("0");
				returnValue.put((int)values[0], list);
		}
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> listAllManagers() {
		List<Employee> returnValue = new ArrayList<Employee>();
		Session currentSession = null;
		try {
			currentSession = sessionFactory.openSession(); 
			Query q = currentSession.createQuery("from Employee e join e.roles r where r.description = :description");
			q.setParameter("description", "ROLE_MANAGER");
			for(Iterator<Object> it = q.list().iterator(); it.hasNext();)
			{
				Object[] val = (Object[])it.next();
				returnValue.add((Employee)val[0]);
			}
		} catch (HibernateException e) {
			throw e;
		}
		finally {
			if(currentSession != null)
				currentSession.close();
		}
		return returnValue; 
	}

	@Override
	public Employee findByEmail(String email) {
		List<?> list = sessionFactory.getCurrentSession()
				.createQuery("from Employee where email like :email")
				.setParameter("email", email)
				.list();
		return (Employee) ((list.size() > 0)?list.get(0):null);
	}

	@Override
	public void changePassword(int empCode, String password) {
		sessionFactory.getCurrentSession()
				.createQuery("update Employee set password = :password where empCode = :empCode")
				.setParameter("password", password)
				.setParameter("empCode", empCode)
				.executeUpdate();
		
	}
	
}
