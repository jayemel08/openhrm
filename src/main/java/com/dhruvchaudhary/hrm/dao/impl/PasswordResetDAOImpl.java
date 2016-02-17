package com.dhruvchaudhary.hrm.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dhruvchaudhary.hrm.dao.interfaces.PasswordResetDAO;
import com.dhruvchaudhary.hrm.model.Employee;
import com.dhruvchaudhary.hrm.model.PasswordReset;

@Repository
public class PasswordResetDAOImpl implements PasswordResetDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public int add(PasswordReset newEntity) {
		//delete(newEntity.getEmployee().getEmpCode());
		sessionFactory.getCurrentSession().save(newEntity);
		return newEntity.getId();
	}

	@Override
	public void delete(int id) {
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	@Override
	public void edit(PasswordReset updatedEntity) {
		sessionFactory.getCurrentSession().update(updatedEntity);
	}

	@Override
	public PasswordReset findById(int id) {
		return (PasswordReset) sessionFactory.getCurrentSession().get(PasswordReset.class,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PasswordReset> listAll() {
		return (List<PasswordReset>) sessionFactory.getCurrentSession().createCriteria(PasswordReset.class).list();
	}

	@Override
	public void deleteAllByEmployee(int empCode) {
		sessionFactory.getCurrentSession().createQuery("delete from PasswordReset where employee.empCode = :empCode")
										.setParameter("empCode", empCode)
										.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Employee findByAuthCode(String authCode) {
		List<PasswordReset> list = (List<PasswordReset>)sessionFactory.getCurrentSession()
									.createQuery("from PasswordReset where authenticationCode = :authCode")
									.setParameter("authCode", authCode)
									.list();
			return (list.size() > 0 ?(list.get(0)).getEmployee():null);
	}
}
