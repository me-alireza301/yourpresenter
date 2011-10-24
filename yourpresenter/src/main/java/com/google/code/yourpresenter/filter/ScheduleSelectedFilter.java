package com.google.code.yourpresenter.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.view.ScheduleView;

@Component("scheduleSelectedFilter")
// implementing ApplicationContextAware is needed to be able to autowire session scoped bean
// (ScheduleView), as this component can't be managed by web-scope for some reason :)
// it's not fully clear to me, but 
// seems like injection is to happen prio to any request and that causes problems 
// see: http://www.mkyong.com/spring/spring-request-scope-error-no-thread-bound-request-found/
// for injection of appcontext, see: http://forum.springsource.org/showthread.php?10165-injecting-applicationContext-into-a-bean
public class ScheduleSelectedFilter implements Filter, ApplicationContextAware {

	private ApplicationContext appCtx;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		// autowiring done programically here (using ApplicationContextAware)
		ScheduleView scheduleView = appCtx.getBean(ScheduleView.class);
		
		if (null == scheduleView.getSchedule()) {
			String path = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(path + "/main.jsf");
		} else {
			filterChain.doFilter(request, response);	
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setApplicationContext(ApplicationContext appCtx)
			throws BeansException {
		this.appCtx = appCtx;
	}
	
}
