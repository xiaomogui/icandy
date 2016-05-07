package com.sain.icandy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.sain.common.RandomString;
import com.sain.icandy.exception.SpiderException;
import com.sain.icandy.model.User;

public class UserService extends BaseService<User> {
	public static final UserService service = Enhancer.enhance(UserService.class);

	public Page<User> paginate(String username, Integer pageNumber, Integer pageSize) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("from t_user where 1=1");
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" and username=?");
			params.add(username);
		}
		sql.append(" order by id desc");
		return User.dao.paginate(pageNumber, pageSize, "select *", sql.toString(), params.toArray());
	}

	public User findById(Integer id) {
		if (id == null || id == 0)
			return null;
		return User.dao.findFirst("select * from t_user where id=?", id);
	}

	public void deleteById(Integer id) {
		if (id == null || id == 0)
			return;
		User.dao.deleteById(id);
	}

	public User login(String username, String password) {
		User user = null;
		try {
			user = findByUsername(username);
		} catch (Exception e) {
			logger.error("根据用户名查询管理员发生异常", e);
			throw new SpiderException("读取数据发生异常");
		}
		if (user == null) {
			throw new SpiderException("用户名或者密码不正确");
		}
		String pwd = md5Pwd(password, user.getSalt());
		if (!StringUtils.equals(pwd, user.getPassword())) {
			throw new SpiderException("用户名或者密码不正确");
		}
		return user;
	}

	private String md5Pwd(String password, String salt) {
		return DigestUtils.md5Hex(password + salt);
	}

	private User findByUsername(String username) {
		return User.dao.findFirst("select * from t_user where username=?", username);
	}

	public void updatePwd(User user, String newpassword, String oldpassword) {
		if (!StringUtils.equals(user.getPassword(), md5Pwd(oldpassword, user.getSalt()))) {
			throw new SpiderException("请输入正确的旧密码");
		}
		user.setSalt(RandomString.getKeyWitchLength(10));
		user.setPassword(UserService.service.md5Pwd(newpassword, user.getSalt()));
		user.update();
	}

	public void save(User user) {
		if (findByUsername(user.getUsername()) != null) {
			throw new SpiderException("账户名已经被使用");
		}
		user.setSalt(RandomString.getKeyWitchLength(10));
		user.setPassword(UserService.service.md5Pwd(user.getPassword(), user.getSalt()));
		user.save();
	}

	public void resetPwd(User user) {
		user.setSalt(RandomString.getKeyWitchLength(10));
		user.setPassword(UserService.service.md5Pwd("123456", user.getSalt()));
		user.update();
	}

}
