<#import "site.lib.ftl" as siteLib/>

[
	<#list sites?sort_by("title") as site>
		<@siteLib.siteJSON site=site/>
		<#if site_has_next>,</#if>
	</#list>
]