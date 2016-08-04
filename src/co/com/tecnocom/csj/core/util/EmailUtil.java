package co.com.tecnocom.csj.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;

import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;

public enum EmailUtil {
	INSTANCE;

	private Map<String, String> resources;

	{
		resources = new LinkedHashMap<String, String>();
	}

	public final void sendEmail(String from, String to, String subject, ServletContext servletContext, String pathKey, String... parameters) {
		try {
			MailMessage mailMessage = new MailMessage();
			mailMessage.setHTMLFormat(true);
			mailMessage.setTo(new InternetAddress(to));
			mailMessage.setFrom(new InternetAddress(from));
			mailMessage.setSubject(subject);

			String htmlData = getFromCache(pathKey);
			if (htmlData == null || htmlData.isEmpty()) {
				putDataInCache(pathKey, servletContext);
				htmlData = getFromCache(pathKey);
			}

			htmlData = setParameters(htmlData, parameters);

			mailMessage.setBody(htmlData);

			System.out.println("HTML DATA:");
			System.out.println(htmlData);
			
			MailServiceUtil.sendEmail(mailMessage);
		} catch (AddressException e) {
			e.printStackTrace();
		}
	}

	public void removeFromCache(String key) {
		resources.remove(key);
	}

	private String setParameters(String htmlData, String... parameters) {
		return String.format(htmlData, (Object[]) parameters);
	}

	private String getFromCache(String key) {
		return resources.get(key);
	}

	private void putDataInCache(String key, ServletContext servletContext) {
		String htmlData = getHtmlFile(servletContext, key);
		resources.put(key, htmlData);
	}

	private final String getHtmlFile(ServletContext servletContext, String path) {
		InputStream is = null;
		try {
			is = servletContext.getResourceAsStream(path);
			Scanner s = new Scanner(is).useDelimiter("\\A");
			String data = s.hasNext() ? s.next() : "";
			s.close();

			return data;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
