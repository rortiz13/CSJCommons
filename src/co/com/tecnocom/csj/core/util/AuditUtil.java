package co.com.tecnocom.csj.core.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;


public enum AuditUtil {
	INSTANCE;
	
	public void auditResource(ServiceContext serviceContext, String layoutURL, Integer auditedResourceId, String auditedOperation, String auditedResource) {
		String message = generateAuditInfo(serviceContext, layoutURL, auditedResourceId, auditedOperation, auditedResource);
		
		MessageBusUtil.sendMessage("audit/register", message);
	}
	
	private String generateAuditInfo(ServiceContext serviceContext, String layoutURL, Integer auditedResourceId, String auditedOperation, String auditedResource) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		jsonObject.put("userID", serviceContext.getUserId());
		String username = null;
		try {
			username = UserLocalServiceUtil.getUser(serviceContext.getUserId()).getScreenName();

		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		jsonObject.put("userName", username);

		jsonObject.put("command", auditedOperation);
		jsonObject.put("companyId", serviceContext.getCompanyId());
		jsonObject.put("currentURL", layoutURL);
		jsonObject.put("remoteAddr", serviceContext.getRemoteAddr());
		jsonObject.put("remoteHost", serviceContext.getRemoteHost());
		jsonObject.put("scopeGroupId", serviceContext.getScopeGroupId());
		
		jsonObject.put("resourceId", auditedResourceId);
		jsonObject.put("resourceAudited", auditedResource);
		
		return jsonObject.toString();
	}

}
