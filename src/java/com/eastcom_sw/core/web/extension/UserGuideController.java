package com.eastcom_sw.core.web.extension;

import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.service.configuration.ArgumentsConfigurateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.internet.MimeUtility;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 用户指南
 * 
 * @author JJF
 * @since NOV 14 2013
 */

@Controller
@RequestMapping(value = "/userguide")
public class UserGuideController extends BaseController {

	@Autowired
	private ArgumentsConfigurateService argumentsConfigurateService;

	/**
	 * 下载用户指南
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "download", method = RequestMethod.GET)
	private void download(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		Map args = argumentsConfigurateService.getSysArguments("startup");
		String fileName = ((Map) args.get("startup")).get("userGuideName").toString();
		String fn = this.getClass().getResource("/").getFile();
		String classPath = new File(fn).getParentFile().getAbsolutePath();
		String fileDir = "";
		String osname = System.getProperty("os.name");
		if (osname.equals("Linux") || osname.equals("Mac OS X")) {
			fileDir = classPath + "/classes/documents/" + fileName;
		} else {
			fileDir = classPath + "\\classes\\documents\\" + fileName;
		}
		File file = new File(fileDir);
		if (file.exists()) {
			FileInputStream in = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				String agent = (String) request.getHeader("USER-AGENT");
				if (agent != null && agent.indexOf("MSIE") == -1) {// FF
					fileName = java.net.URLDecoder.decode(fileName, "utf-8");
					fileName = MimeUtility.encodeText(fileName, "UTF8", "B");
				} else {
					fileName = URLEncoder.encode(fileName, "utf-8");
				}

				in = new FileInputStream(file);
				response.reset();
				response.setContentType("application/x-msdownload;charset=uft-8");
				response.setHeader("Content-disposition", "attachment; filename=" + fileName);
				bis = new BufferedInputStream(in);
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] buff = new byte[bis.available()];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bos.flush();
					bos.close();
					if (in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			String fileNotFoundInfo = "File not found " + file.getPath();
			request.setAttribute("javax.servlet.error.exception", new FileNotFoundException(fileNotFoundInfo));
			String errorUrl = "";
			if (osname.equals("Linux") || osname.equals("Mac OS X")) {
				errorUrl = classPath + "/views/error/500.jsp";
			} else {
				errorUrl = classPath + "\\views\\error\\500.jsp";
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(errorUrl);
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}