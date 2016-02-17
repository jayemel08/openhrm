package com.dhruvchaudhary.hrm.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dhruvchaudhary.hrm.model.Holiday;
import com.dhruvchaudhary.hrm.dao.interfaces.HolidayDAO;

@Repository
public class CalenderLeavesDAOImpl implements HolidayDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public int add(Holiday newHoliday) {
		sessionFactory.getCurrentSession().save(newHoliday);
		return newHoliday.getId();
	}

	@Override
	public void delete(int id) {
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	@Override
	public void edit(Holiday updatedEntity) {
		sessionFactory.getCurrentSession().update(updatedEntity);
	}

	@Override
	public Holiday findById(int id) {
		return (Holiday) sessionFactory.getCurrentSession().get(Holiday.class,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Holiday> listAll() {
		return (List<Holiday>) sessionFactory.getCurrentSession().createCriteria(Holiday.class).list();
	}

	@Override
	public Holiday findByDate(Date date) {
		
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(Holiday.class)
											.add(Restrictions.eq("date", date));
		return (cr.list().size() > 0? (Holiday) cr.list().get(0):null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Holiday> listHolidays(Date date) {
		return sessionFactory.getCurrentSession()
							.createCriteria(Holiday.class)
							.add(Restrictions.ge("date", date))
							.list();
	}

}
