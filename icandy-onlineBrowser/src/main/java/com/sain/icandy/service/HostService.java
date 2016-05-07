package com.sain.icandy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sain.common.Constants;
import com.sain.icandy.model.Host;

public class HostService extends BaseService<Host> {
	public static final HostService service = Enhancer.enhance(HostService.class);

	public List<Host> getAll() {
		return Host.dao.findByCache(Constants.CACHE_NAME_T_HOST, "ALL", "select * from t_host where status=1");
	}

	public Host findById(Integer id) {
		if (id == null || id == 0)
			return null;
		return Host.dao.findFirstByCache(Constants.CACHE_NAME_T_HOST, id, "select * from t_host where id=? and status=1", id);
	}

	public Page<Host> paginate(String host, Integer status, Integer pageNumber, Integer pageSize) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("from t_host where 1=1");
		if (StringUtils.isNotEmpty(host)) {
			sql.append(" and host like ?");
			params.add("%" + host + "%");
		}
		if (status != null && status != 0) {
			sql.append(" and status=?");
			params.add(status);
		}
		sql.append(" order by id desc");
		return Host.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
	}

	public void deleteById(Integer id) {
		if (id == null || id == 0)
			return;
		Host.dao.deleteById(id);
		CacheKit.remove(Constants.CACHE_NAME_T_HOST, id);
		CacheKit.remove(Constants.CACHE_NAME_T_HOST, "ALL");
	}

	public void update(Host h) {
		super.update(h);
		CacheKit.remove(Constants.CACHE_NAME_T_HOST, "ALL");
	}

	@Override
	public void save(Host t) {
		super.save(t);
		CacheKit.remove(Constants.CACHE_NAME_T_HOST, "ALL");
	}
}
