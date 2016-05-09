package com.gc.sports.pool;

import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.sports.webclient.FootballWebclient;

public final class HGSportExecutePool {
	private final static Logger LOGGER = LoggerFactory.getLogger(HGSportExecutePool.class);

	public static final ArrayBlockingQueue<FootballWebclient> FOOTBALLWEBCLIENT_POOL = HGSportExecutePoolPreLoading.FOOTBALLWEBCLIENT_POOL;

	private HGSportExecutePool(){}

	/**
	 * 预加载类.
	 * 用于第一次使用提速
	 * @author zimen-gc
	 *
	 */
	private final static class HGSportExecutePoolPreLoading{
		public static final ArrayBlockingQueue<FootballWebclient> FOOTBALLWEBCLIENT_POOL = new ArrayBlockingQueue<FootballWebclient>(5);
		static{
			HGSportExecutePoolPreLoading.loadingHgaccount();
		}

		public static boolean loadingHgaccount(){
			FOOTBALLWEBCLIENT_POOL.clear();
			String hgusername = "";
			String hgpassword = "";

			try {
				String hgUrlStr = "";

				FootballWebclient footballWebclientHjsj = new FootballWebclient(hgusername, hgpassword, hgUrlStr);
				boolean loginFlag = false;
				Integer tryCount = 3; // 尝试三次，为了避免因暂时网络链接超时而返回false
				while(!loginFlag && tryCount-- > 0){
					loginFlag = footballWebclientHjsj.login();
				}

				if(loginFlag){
					FOOTBALLWEBCLIENT_POOL.put(footballWebclientHjsj);
				}
			} catch (InterruptedException e) {
				LOGGER.error(e.getLocalizedMessage(), e);
			}
		
		
			/*
			try {
				ClassLoader classLoader = HGSportExecutePool.class.getClassLoader();
				InputStream inStream = classLoader.getResourceAsStream("hgaccount.properties");
				Properties properties = new Properties();
				try {
					properties.load(inStream);
					Enumeration<?> enumeration = properties.propertyNames();
					while (enumeration.hasMoreElements()) {
						String key = (String) enumeration.nextElement();
						String value = (String) properties.get(key);

						FootballWebclient footballWebclientHjsj = new FootballWebclient(key, value);
						footballWebclientHjsj.login();
						FOOTBALLWEBCLIENT_POOL.put(footballWebclientHjsj);
					}
				} catch (IOException e) {
					LOGGER.error(e.getLocalizedMessage(), e);
				}
			} catch (InterruptedException e) {
				LOGGER.error(e.getLocalizedMessage(), e);
			}
			*/

			return true;
		}

		private HGSportExecutePoolPreLoading(){}

	}
}
