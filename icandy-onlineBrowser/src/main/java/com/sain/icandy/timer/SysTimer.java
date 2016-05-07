package com.sain.icandy.timer;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sain.icandy.timer.job.ClearDataJob;

public class SysTimer {
	private Logger logger = LoggerFactory.getLogger(SysTimer.class);

	public void start() {
		logger.info("开启系统定时器");
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = null;
		try {
			sched = sf.getScheduler();
		} catch (Exception e) {
			logger.error("获取equartz scheduler发生错误", e);
		}
		if (sched == null) {
			logger.error("equartz scheduler为空");
			return;
		}
		clearDateJob(sched);
		try {
			sched.start();
		} catch (Exception e) {
			logger.info("系统定时器启动时发生错误", e);
		}

		logger.info("系统定时器已全部开启");
	}

	/**
	 * 开启定期删除过期数据 每天23点20分开始运行
	 * 
	 * @param sched
	 */
	private void clearDateJob(Scheduler sched) {
		try {
			logger.info("开启定期删除过期数据");
			JobDetail job = newJob(ClearDataJob.class).withIdentity("clearDataJob", "default").build();
			Trigger trigger = newTrigger().withIdentity("clearDataTrigger", "default").forJob(job.getKey()).withSchedule(cronSchedule("0 20 23 * * ?")).build();
			sched.scheduleJob(job, trigger);
			logger.info("定期删除过期数据已开启");
		} catch (Exception e) {
			logger.error("开启定期删除过期数据发生错误", e);
		}
	}

}
