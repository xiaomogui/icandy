package com.sain.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingUtil {
	// Jdk1.5的InetAddresss,代码简单。
	public static boolean ping(String ipAddress) throws Exception {
		int timeOut = 3000; // 超时应该在3钞以上
		boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut); // 当返回值是true时，说明host是可用的，false则不可。
		return status;
	}

	// 使用java调用cmd命令,这种方式最简单，可以把ping的过程显示在本地。
	public static void ping02(String ipAddress) throws Exception {
		String line = null;
		try {
			Process pro = Runtime.getRuntime().exec("ping " + ipAddress);
			BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			while ((line = buf.readLine()) != null)
				System.out.println(line);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	// 也是使用java调用控制台的ping命令，这个比较可靠，还通用，使用起来方便：传入个ip，设置ping的次数和超时，就可以根据返回值来判断是否ping通。
	public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
		BufferedReader in = null;
		Runtime r = Runtime.getRuntime();
		// 将要执行的ping命令,此命令是windows格式的命令
		String pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;
		// linux格式的命令
		// String pingCommand = "ping " + ipAddress + " -c " + pingTimes + " -t
		// " + timeOut;
		try { // 执行命令并获取输出
			System.out.println(pingCommand);
			Process p = r.exec(pingCommand);
			if (p == null) {
				return false;
			}
			in = new BufferedReader(new InputStreamReader(p.getInputStream())); // 逐行检查输出,计算类似出现=23ms
																				// TTL=62字样的次数
			int connectedCount = 0;
			String line = null;
			while ((line = in.readLine()) != null) {
				connectedCount += getCheckResult(line);
			} // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
			return connectedCount == pingTimes;
		} catch (Exception ex) {
			ex.printStackTrace(); // 出现异常则返回假
			return false;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 获取ping的延迟毫秒数
	public static String pingDelay(String ipAddress) {
		int pingTimes = 1;
		int timeOut = 3600;

		ipAddress = ipAddress.replace("http://", "").replace("https://", "");
		int _index;
		if((_index = ipAddress.indexOf(":")) >= 0){
			ipAddress = ipAddress.substring(0, _index);
		}
		int index_;
		if((index_ = ipAddress.indexOf("/")) >= 0){
			ipAddress = ipAddress.substring(0, index_);
		}

		String pingCommand = "";
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			// 将要执行的ping命令,此命令是windows格式的命令
			pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;
		} else if (os.toLowerCase().startsWith("mac")) {
			// 将要执行的ping命令,此命令是mac格式的命令
			pingCommand = "ping " + "-c " + pingTimes + " -t " + timeOut + " " + ipAddress;
		} else {
			// linux格式的命令
			pingCommand = "ping " + "-c " + pingTimes + " " + ipAddress;
		}

		BufferedReader in = null;
		Runtime r = Runtime.getRuntime();

		try { // 执行命令并获取输出
			Process p = r.exec(pingCommand);
			if (p == null) {
				return "－";
			}
			in = new BufferedReader(new InputStreamReader(p.getInputStream())); // 逐行检查输出,计算类似出现=23ms
																				// TTL=62字样的次数
			String line = null;
			while ((line = in.readLine()) != null) {
				if(line.toLowerCase().contains("ttl")){
					return line;
				}
			}
			return "－";
		} catch (Exception ex) {
			ex.printStackTrace(); // 出现异常则返回假
			return "－";
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
	private static int getCheckResult(String line) { // System.out.println("控制台输出的结果为:"+line);
		Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			return 1;
		}
		return 0;
	}

	public static void main(String[] args) throws Exception {
		String ipAddress = "127.0.0.1";
		System.out.println(ping(ipAddress));
		ping02(ipAddress);
		System.out.println(ping(ipAddress, 5, 5000));
	}
}
