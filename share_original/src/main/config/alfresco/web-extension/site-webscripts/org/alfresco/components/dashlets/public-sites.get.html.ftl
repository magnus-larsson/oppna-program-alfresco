<script type="text/javascript">//<![CDATA[
   new Alfresco.dashlet.PublicSites("${args.htmlid}").setOptions(
   {
      imapEnabled: ${imapServerEnabled?string},
      sites: [
<#if sites??>
   <#list sites as site>
      {
         shortName: '${site.shortName?js_string}',
         title: '${site.title?js_string}',
         description: '${site.description?js_string}',
         isFavourite: ${site.isFavourite?string},
         <#if imapServerEnabled>isIMAPFavourite: ${site.isIMAPFavourite?string},</#if>
         isSiteManager: ${site.isSiteManager?string}
      }<#if (site_has_next)>,</#if>
   </#list>
</#if>
      ]
   }).setMessages(
      ${messages}
   );
   new Alfresco.widget.DashletResizer("${args.htmlid}", "${instance.object.id}");
//]]></script>

<div class="dashlet public-sites">
   <div class="title">${msg("header.publicSites")}</div>
<#if sites??>
   <div id="${args.htmlid}-sites" class="body scrollableList" <#if args.height??>style="height: ${args.height}px;"</#if>>
<#else>
   <div class="body scrollableList" <#if args.height??>style="height: ${args.height}px;"</#if>>
      <div class="detail-list-item first-item last-item">
         <span>${msg("label.noSites")}</span>
      </div>
</#if>
   </div>
</div>