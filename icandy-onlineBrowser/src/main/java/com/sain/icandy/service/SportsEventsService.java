package com.sain.icandy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sain.common.Constants;
import com.sain.icandy.model.SportsEvents;

public class SportsEventsService extends BaseService<SportsEvents> {
	public static final SportsEventsService service = Enhancer.enhance(SportsEventsService.class);

	public Page<SportsEvents> paginate(Integer id, String gid, String league, String teamH, String teamC, Integer pageNumber, Integer pageSize) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("from t_sports_events where 1=1");
		if (id != null && id != 0) {
			sql.append(" and id=?");
			params.add(id);
		}
		if (!StringUtils.isEmpty(gid)) {
			sql.append(" and gid=?");
			params.add(gid);
		}
		if (!StringUtils.isEmpty(league)) {
			sql.append(" and league=?");
			params.add(league);
		}
		if (!StringUtils.isEmpty(teamH)) {
			sql.append(" and team_h=?");
			params.add(teamH);
		}
		if (!StringUtils.isEmpty(teamC)) {
			sql.append(" and team_c=?");
			params.add(teamC);
		}
		sql.append(" order by id desc");
		return SportsEvents.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
	}

	public void deleteById(Integer id) {
		SportsEvents.dao.deleteById(id);
		CacheKit.removeAll(Constants.CACHE_NAME_T_SPORTS_EVENTS);
	}

}
