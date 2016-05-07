package com.sain.icandy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;

public abstract class BaseService<T extends Model<T>> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public void save(T t) {
		t.save();
	}

	public void update(T t) {
		t.update();
	}

}
