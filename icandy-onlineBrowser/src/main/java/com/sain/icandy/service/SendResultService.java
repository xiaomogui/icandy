package com.sain.icandy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.sain.common.Constants;
import com.sain.common.DateKit;
import com.sain.icandy.model.SendResult;

public class SendResultService extends BaseService<SendResult> {
	public static final SendResultService service = Enhancer.enhance(SendResultService.class);

	public SendResult findOne(Integer hostId, Integer sportsEventsId) {
		String key = hostId + "_" + sportsEventsId;
		return SendResult.dao.findFirstByCache(Constants.CACHE_NAME_T_SEND_RESULT, key, "select * from t_send_result where hostId=? and sports_events_id=?", hostId, sportsEventsId);
	}

	public void update(SendResult sr) {
		if (sr.getId() != null && sr.getId() != 0) {
			if (sr.getStatus() == 2) {
				sr.setFailedNum(sr.getFailedNum() + 1);
			}
			sr.update();
		} else {
			SendResult old = findOne(sr.getHostId(), sr.getSportsEventsId());
			if (old != null) {
				sr.setId(old.getId());
				if (sr.getStatus() == 2) {
					sr.setFailedNum(old.getFailedNum() + 1);
				}
				sr.setCreateTime(old.getCreateTime());
				sr.update();
			} else {
				sr.setCreateTime(new Date());
				if (sr.getStatus() == 2) {
					sr.setFailedNum(1);
				} else {
					sr.setFailedNum(0);
				}
				sr.save();
			}
		}
		String key = sr.getHostId() + "_" + sr.getSportsEventsId() + "_" + sr.getId();
		CacheKit.put(Constants.CACHE_NAME_T_SEND_RESULT, key, sr);
	}

	public List<SendResult> findForSender(Date startTime) {
		return SendResult.dao.find("select * from t_send_result  where status=2 and createTime>? and failedNum<10 limit 500", startTime);
	}

	public Page<SendResult> paginate(String host, Integer status, Integer sportsEventsId, Date start, Date end, Integer pageNumber, Integer pageSize) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("from t_send_result where 1=1");
		if (StringUtils.isNotEmpty(host)) {
			sql.append(" and host=?");
			params.add(host);
		}
		if (status != null && status != 0) {
			sql.append(" and status=?");
			params.add(status);
		}
		if (sportsEventsId != null && sportsEventsId != 0) {
			sql.append(" and sports_events_id=?");
			params.add(sportsEventsId);
		}
		if (start != null) {
			sql.append(" and createTime>=?");
			params.add(DateKit.getDateZero(start));
		}
		if (end != null) {
			sql.append(" and createTime<?");
			params.add(DateKit.getDateLast(end));
		}
		sql.append(" order by createTime desc");
		return SendResult.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
	}

	public void deleteById(Integer id) {
		SendResult.dao.deleteById(id);
		CacheKit.removeAll(Constants.CACHE_NAME_T_SEND_RESULT);
	}

	public int clearData(Date time) {
		return Db.update("delete from t_send_result where createTime<?", time);
	}

	public Set<Integer> findExistIds(Integer hostId, List<Integer> ids) {
		if (ids == null || ids.isEmpty() || hostId == null || hostId == 0)
			return null;
		StringBuilder sql = new StringBuilder("select sports_events_id from t_send_result where hostId=? and sports_events_id in(");
		List<Object> param = new ArrayList<>();
		param.add(hostId);
		for (Integer id : ids) {
			sql.append("?,");
			param.add(id);
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		List<Integer> list = Db.query(sql.toString(), param.toArray());
		if (list != null && !list.isEmpty()) {
			return new HashSet<>(list);
		}
		return null;
	}

}
