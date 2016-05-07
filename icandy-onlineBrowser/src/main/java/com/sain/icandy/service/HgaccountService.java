package com.sain.icandy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sain.common.Constants;
import com.sain.icandy.model.Hgaccount;

public class HgaccountService extends BaseService<Hgaccount> {
	public static final HgaccountService service = Enhancer.enhance(HgaccountService.class);

	public List<Hgaccount> getAll() {
		return Hgaccount.dao.findByCache(Constants.CACHE_NAME_T_HGACCOUNT, "ALL", "select * from t_hgaccount where status='1'");
	}

	public Hgaccount findById(Integer id) {
		if (id == null || id == 0)
			return null;
		return Hgaccount.dao.findFirstByCache(Constants.CACHE_NAME_T_HGACCOUNT, id, "select * from t_hgaccount where id=?", id);
	}

	public Hgaccount findById(Integer id, String status) {
		if (id == null || id == 0)
			return null;
		return Hgaccount.dao.findFirstByCache(Constants.CACHE_NAME_T_HGACCOUNT, id, "select * from t_hgaccount where id=? and status=?", id, status);
	}

	public Page<Hgaccount> paginate(String hgusername, Integer status, Integer pageNumber, Integer pageSize) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("from t_hgaccount where 1=1");
		if (StringUtils.isNotEmpty(hgusername)) {
			sql.append(" and hg_username like ?");
			params.add("%" + hgusername + "%");
		}
		if (status != null && status != 0) {
			sql.append(" and status=?");
			params.add(status);
		}
		sql.append(" order by id desc");
		return Hgaccount.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
	}

	public void deleteById(Integer id) {
		if (id == null || id == 0)
			return;
		Hgaccount.dao.deleteById(id);
		CacheKit.remove(Constants.CACHE_NAME_T_HGACCOUNT, id);
		CacheKit.remove(Constants.CACHE_NAME_T_HGACCOUNT, "ALL");
	}

	public void update(Hgaccount h) {
		super.update(h);
		CacheKit.remove(Constants.CACHE_NAME_T_HGACCOUNT, "ALL");
	}

	@Override
	public void save(Hgaccount t) {
		super.save(t);
		CacheKit.remove(Constants.CACHE_NAME_T_HGACCOUNT, "ALL");
	}
}
