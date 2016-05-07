package com.sain.icandy.timer.job;

import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sain.icandy.service.SendResultService;

/**
 * 清理3天前的数据
 * 
 * @author macair
 *
 */
public class ClearDataJob implements Job {
	private static Logger logger = LoggerFactory.getLogger(ClearDataJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("开始清理3天前的lottery数据");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -3);
		// int i = LotteryService.service.clearData(c.getTime());
		int i = 0;
		logger.info("共清理" + i + "条lottery数据");
		logger.info("开始清理3天前的推送数据");
		i = SendResultService.service.clearData(c.getTime());
		logger.info("共清理" + i + "条推送数据");
	}

}
