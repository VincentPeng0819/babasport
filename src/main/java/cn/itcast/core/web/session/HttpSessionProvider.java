package cn.itcast.core.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 本地session
 * @author Shadow
 *
 */
public class HttpSessionProvider implements SessionProvider {

	@Override
	public void setAttribute(HttpServletRequest request, String name,
			Serializable value) {
		HttpSession session = request.getSession();
		session.setAttribute(name, value);
	}

	@Override
	public Serializable getAttribute(HttpServletRequest request, String name) {
		HttpSession session = request.getSession(false);
		if(session != null){
			return (Serializable) session.getAttribute(name);
		}return null;
	}

	@Override
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null){
			 session.invalidate();
		}
	}

	@Override
	public String getSessionId(HttpServletRequest request) {
		return request.getSession().getId();//request.getRequestedSessionId(); // http://localhost:8080/html?JSESSIONID=fjdkls798
	}
}