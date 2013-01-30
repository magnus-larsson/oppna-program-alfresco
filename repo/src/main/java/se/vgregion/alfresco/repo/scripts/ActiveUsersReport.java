package se.vgregion.alfresco.repo.scripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.service.ServiceRegistry;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import se.vgregion.alfresco.repo.report.ActiveUsers;
import se.vgregion.alfresco.repo.report.UserLoginDetails;

public class ActiveUsersReport extends DeclarativeWebScript {

	private static ServiceRegistry serviceRegistry;
	
	@Override
	protected Map<String, Object> executeImpl(final WebScriptRequest req,
			final Status status, final Cache cache) {
		Map<String, Object> model = new HashMap<String, Object>();
		
		ActiveUsers au = new ActiveUsers();
		Map<String, List<UserLoginDetails>> activeUsersByZone;
		try {
			activeUsersByZone = au.getActiveUsersByZone();
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		int size = activeUsersByZone.get(ActiveUsers.INTERNAL_USERS).size()+activeUsersByZone.get(ActiveUsers.EXTERNAL_USERS).size();
		model.put("users", activeUsersByZone);
		model.put("recordsReturned", size);
		model.put("totalRecords", size);
		model.put("startIndex", 0);
		model.put("pageSize", size);
		/*
		String sites = req.getParameter("sites");

		ReportSiteUsage rsu = new ReportSiteUsage();
		SiteService siteService = serviceRegistry.getSiteService();
		List<SiteInfo> allSites = siteService.listSites(null, null);
		List<Map<String, Serializable>> sitesResult = new ArrayList<Map<String, Serializable>>();
		try {
			for (SiteInfo site : allSites) {
				NodeRef nodeRef = site.getNodeRef();
				long siteSize = rsu.getSiteSize(nodeRef);
				long siteMembers = rsu.getNumberOfSiteMembers(site);
				Date lastActivity = rsu.getLastActivityOnSite(site);
				Map<String, Serializable> siteMap = new HashMap<String, Serializable>();
				siteMap.put("shortName", site.getShortName());
				siteMap.put("title", site.getTitle());
				siteMap.put("size", siteSize);
				siteMap.put("members", siteMembers);
				DateFormat df = new SimpleDateFormat();
				if (lastActivity != null) {
					siteMap.put("lastActivity", df.format(lastActivity));
				} else {
					siteMap.put("lastActivity", "");
				}
				sitesResult.add(siteMap);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		model.put("recordsReturned", sitesResult.size());
		model.put("totalRecords", sitesResult.size());
		model.put("startIndex", 0);
		model.put("pageSize", sitesResult.size());
		model.put("sites", sitesResult);*/
		return model;
	}

	public ServiceRegistry getServiceRegistry() {
		return ActiveUsersReport.serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		ActiveUsersReport.serviceRegistry = serviceRegistry;
	}
}
