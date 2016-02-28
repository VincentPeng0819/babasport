package cn.itcast.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.web.session.SessionProvider;

public class SpringmvcInterceptor implements HandlerInterceptor{

	@Autowired
	private SessionProvider sessionProvider;
	
	private Integer adminId;
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	private static final String INTERCEPTOR_URL = "/buyer/";
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Buyer buyer = (Buyer) sessionProvider.getAttribute(request, Constants.BUYER_SESSION_STRING);
		if(adminId != null){
			Buyer buyer2 = new Buyer();
			buyer2.setUsername("fbb2014");
			sessionProvider.setAttribute(request, Constants.BUYER_SESSION_STRING, buyer2);
		}else {
			boolean flag = false;
			if(null != buyer){
				flag = true;
			}
			request.setAttribute("isLogin", flag);
			
			//是否拦截
			String requestURI = request.getRequestURI();
			if(requestURI.startsWith(INTERCEPTOR_URL)){
				if(null == buyer){
					response.sendRedirect("/shopping/login.shtml?returnUrl=" + request.getParameter("returnUrl"));
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
