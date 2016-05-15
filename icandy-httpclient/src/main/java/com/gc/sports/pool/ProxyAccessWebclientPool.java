package com.gc.sports.pool;

import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.sports.webclient.ProxyAccessWebclient;

public final class ProxyAccessWebclientPool {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProxyAccessWebclientPool.class);

	private static final int CLIENT_COUNT = 200;

	public static final ArrayBlockingQueue<ProxyAccessWebclient> FOOTBALLWEBCLIENT_POOL = new ArrayBlockingQueue<ProxyAccessWebclient>(CLIENT_COUNT);

	static{
		loadingHgaccount();
	}

	public static boolean loadingHgaccount(){
		FOOTBALLWEBCLIENT_POOL.clear();

		try {
			ProxyAccessWebclient proxyAccessWebclient = null;
			for(int i = 0; i < CLIENT_COUNT; i++){
				proxyAccessWebclient = new ProxyAccessWebclient();
				FOOTBALLWEBCLIENT_POOL.put(proxyAccessWebclient);
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

	private ProxyAccessWebclientPool(){}

}
