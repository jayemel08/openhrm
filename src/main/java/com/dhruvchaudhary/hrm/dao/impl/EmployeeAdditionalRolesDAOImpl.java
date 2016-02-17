package com.dhruvchaudhary.hrm.dao.impl;

import java.util.List;

import com.dhruvchaudhary.hrm.model.EmployeeAdditionalRoles;
import com.dhruvchaudhary.hrm.dao.interfaces.EmployeeAdditionalRolesDAO;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class EmployeeAdditionalRolesDAOImpl implements EmployeeAdditionalRolesDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public int add(EmployeeAdditionalRoles newRole) {
		sessionFactory.getCurrentSession().save(newRole);
		return newRole.getId();
	}

	@Override
	public void delete(int id) {
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	@Override
	public void edit(EmployeeAdditionalRoles updatedRole) {
		sessionFactory.getCurrentSession().update(updatedRole);
	}

	@Override
	public EmployeeAdditionalRoles findById(int id) {
		return (EmployeeAdditionalRoles) sessionFactory.getCurrentSession().get(EmployeeAdditionalRoles.class,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EmployeeAdditionalRoles> listAll() {
		return (List<EmployeeAdditionalRoles>) sessionFactory.getCurrentSession().createCriteria(EmployeeAdditionalRoles.class).list();
	}
	
	public EmployeeAdditionalRoles getRoleByDescription(String description) {
		return (EmployeeAdditionalRoles) sessionFactory.getCurrentSession().createCriteria(EmployeeAdditionalRoles.class)
										.add(Restrictions.like("description", description))
										.list()
										.get(0);
	}
}
