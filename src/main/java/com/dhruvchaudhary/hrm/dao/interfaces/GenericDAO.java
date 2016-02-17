package com.dhruvchaudhary.hrm.dao.interfaces;

import java.util.List;

public interface GenericDAO<T> {
	public int add(T newEntity);
	public void delete(int id);
	public void edit(T updatedEntity);
	public T findById(int id);
	public List<T> listAll();
}
