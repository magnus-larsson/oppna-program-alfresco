<#!-- @overridden projects/slingshot/config/alfresco/site-webscripts/org/alfresco/components/invite/reject-invite.get.html.ftl -->
<@markup id="vgr-html" target="html" action="replace">
   <@uniqueIdDiv>
      <div class="page-title theme-bg-color-1 theme-border-1">
         <div class="title">
            <h1 class="theme-color-3"><span>${msg("header.title")}</span></h1>
         </div>
      </div>
      <#if (error?? && error)>
         <div class="reject-invite-body">
            <h1>${msg("error.noinvitation.title")}</h1>
            <p>${msg("error.noinvitation.text")}</p>
         </div>
      <#else>
         <#assign inviter = invite.inviter.userName>
         <#if (invite.inviter.firstName?? || invite.inviter.lastName??)>
            <#assign inviter = (invite.inviter.firstName!'' + ' ' + invite.inviter.lastName!'')?html>
         </#if>
         <#assign siteName><#if (invite.site.title?? && invite.site.title?length > 0)>${invite.site.title}<#else>${invite.site.shortName}</#if></#assign>
         <#assign siteMarkup><span class="site-name">${siteName?html}</span></#assign>
         <div class="reject-invite-body">
            <div id="${args.htmlid}-confirm" class="main-content">
               <div class="question">${msg("reject.question", inviter, siteMarkup)}</div>
               <div class="actions">
                  <span id="${args.htmlid}-decline-button" class="yui-button yui-push-button"> 
                     <span class="first-child"> 
                        <input type="button" name="decline-button" value="${msg("action.reject")}"> 
                     </span> 
                  </span> 
                  <span id="${args.htmlid}-accept-button" class="yui-button yui-push-button"> 
                     <span class="first-child"> 
                        <input type="button" name="accept-button" value="${msg("action.accept")}">
                     </span>
                  </span>
               </div>
            </div>
            <div id="${args.htmlid}-declined" class="main-content hidden">
               <p>${msg("message.rejected", inviter, siteMarkup)}<p>
            </div>
            <div id="${args.htmlid}-learn-more" class="learn-more">
               <p>${msg("learn.more")} <a href="http://www.vgregion.se/alfresco">http://www.vgregion.se/alfresco</a>
            </div>
         </div>
      </#if>
   </@>
</@>

