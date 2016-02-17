package com.dhruvchaudhary.hrm.dao.interfaces;

import java.util.Date;
import java.util.List;

import com.dhruvchaudhary.hrm.model.Holiday;

public interface HolidayDAO extends GenericDAO<Holiday> {
	public Holiday findByDate(Date date);
	public List<Holiday> listHolidays(Date date);
}
