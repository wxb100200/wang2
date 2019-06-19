package com.base.wang.config;

import com.base.wang.entity.BasTest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * 封装shiro用对象获取
 * 
 */
public class AccountShiroUtil {
	/**
	 * 获取当前对象的拷贝
	 * 
	 * @return
	 */
	public static BasTest getCurrentUser() {
		BasTest customer = null;
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		if (null != session) {
			Object obj = session.getAttribute(SessionConstants.SESSION_LOGIN_USER);
			if (null != obj && obj instanceof BasTest) {
				try {
					/**
					 * 复制一份对象，防止被错误操作
					 */
					customer = (BasTest) BeanUtils.cloneBean(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return customer;
	}



	/**
	 * 切换身份，登录后，动态更改subject的用户属性
	 * @param user
	 */
	public static void setUser(BasTest user) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		session.setAttribute(SessionConstants.SESSION_LOGIN_USER, user);
		PrincipalCollection newPrincipalCollection =
				new SimplePrincipalCollection(user.getUsername(), user.getPassword());
		subject.runAs(newPrincipalCollection);
	}
}
