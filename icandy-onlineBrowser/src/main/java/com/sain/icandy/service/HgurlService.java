package com.sain.icandy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sain.common.Constants;
import com.sain.icandy.model.Hgurl;

public class HgurlService extends BaseService<Hgurl> {
	public static final HgurlService service = Enhancer.enhance(HgurlService.class);

	public List<Hgurl> getAll() {
		return Hgurl.dao.findByCache(Constants.CACHE_NAME_T_HGURL, "ALL", "select * from t_hgurl where status=1");
	}

	public Hgurl findById(Integer id) {
		if (id == null || id == 0)
			return null;
		return Hgurl.dao.findFirstByCache(Constants.CACHE_NAME_T_HGURL, id, "select * from t_hgurl where id=?", id);
	}

	public Hgurl findById(Integer id, String status) {
		if (id == null || id == 0)
			return null;
		return Hgurl.dao.findFirstByCache(Constants.CACHE_NAME_T_HGURL, id, "select * from t_hgurl where id=? and status=?", id, status);
	}

	public Page<Hgurl> paginate(String hgurl, String status, Integer pageNumber, Integer pageSize) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("from t_hgurl where 1=1");
		if (StringUtils.isNotEmpty(hgurl)) {
			sql.append(" and hg_url like ?");
			params.add("%" + hgurl + "%");
		}
		if (StringUtils.isNotEmpty(status)) {
			sql.append(" and status=?");
			params.add(status);
		}
		sql.append(" order by id desc");
		return Hgurl.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
	}

	public void deleteById(Integer id) {
		if (id == null || id == 0)
			return;
		Hgurl.dao.deleteById(id);
		CacheKit.remove(Constants.CACHE_NAME_T_HGURL, id);
		CacheKit.remove(Constants.CACHE_NAME_T_HGURL, "ALL");
	}

	public void update(Hgurl h) {
		super.update(h);
		CacheKit.remove(Constants.CACHE_NAME_T_HGURL, "ALL");
	}

	@Override
	public void save(Hgurl t) {
		super.save(t);
		CacheKit.remove(Constants.CACHE_NAME_T_HGURL, "ALL");
	}
}
