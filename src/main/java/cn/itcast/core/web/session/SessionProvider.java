package cn.itcast.core.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public interface SessionProvider {
	public void setAttribute(HttpServletRequest request,String name,Serializable value);
	public Serializable getAttribute(HttpServletRequest request,String name);
	public void logout(HttpServletRequest request);
	public String getSessionId(HttpServletRequest request);
}	
