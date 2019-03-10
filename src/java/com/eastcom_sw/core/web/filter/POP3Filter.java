/**
 * 
 */
package com.eastcom_sw.core.web.filter;

import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Cason Eastcom
 * 
 */
public class POP3Filter implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static org.apache.commons.logging.Log logWriter = LogFactory
			.getLog(POP3Filter.class.getName());

	public POP3Filter() {
		super();
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		// iframe引起的内部cookie丢失
		res.setHeader("P3P", "CP=CAO PSA OUR");
		if (chain != null)
			chain.doFilter(request, response);

	}

	public void destroy() {

	}
}
