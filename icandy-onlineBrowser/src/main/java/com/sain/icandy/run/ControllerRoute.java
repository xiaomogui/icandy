package com.sain.icandy.run;

import com.jfinal.config.Routes;
import com.sain.icandy.web.controller.HgaccountController;
import com.sain.icandy.web.controller.HgurlController;
import com.sain.icandy.web.controller.HostController;
import com.sain.icandy.web.controller.IndexController;
import com.sain.icandy.web.controller.SendResultController;
import com.sain.icandy.web.controller.SportsEventsController;
import com.sain.icandy.web.controller.UserController;

public class ControllerRoute extends Routes {
	public void config() {
		add("/", IndexController.class, "sys");
		add("/user", UserController.class, "sys/user");
		add("/host", HostController.class, "sys/host");
		add("/sendResult", SendResultController.class, "sys/sendResult");
		add("/sportsevents", SportsEventsController.class, "sys/sportsevents");
		add("/hgaccount", HgaccountController.class, "sys/hgaccount");
		add("/hgurl", HgurlController.class, "sys/hgurl");
	}
}