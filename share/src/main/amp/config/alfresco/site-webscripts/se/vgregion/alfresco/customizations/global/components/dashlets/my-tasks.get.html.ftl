<#-- @overridden projects/slingshot/config/alfresco/site-webscripts/org/alfresco/components/dashlets/my-tasks.get.html.ftl -->

<@markup id="vgr-html" target="html" action="replace">
   <@uniqueIdDiv>
      <#assign id = args.htmlid?html>
      <div class="dashlet my-tasks">
         <div class="title">${msg("header")}</div>
         <div class="toolbar flat-button">
            <div class="hidden">
               <span class="align-left yui-button yui-menu-button" id="${id}-filters">
                  <span class="first-child">
                     <button type="button" tabindex="0"></button>
                  </span>
               </span>
               <select id="${id}-filters-menu">
               <#list filters as filter>
                  <option value="${filter.type?html}">${msg("filter." + filter.type)}</option>
               </#list>
               </select>
               <#-- until VGR has workflow support, this is commented out
               <span class="align-right yui-button-align">
                  <span class="first-child">
                     <a href="${url.context}/page/start-workflow?referrer=tasks" class="theme-color-1">
                        <img src="${url.context}/res/components/images/workflow-16.png" style="vertical-align: text-bottom" width="16" />
                        ${msg("link.startWorkflow")}</a>
                  </span>
               </span>
               -->
               <div class="clear"></div>
            </div>
         </div>
         <#-- until VGR has workflow support, this is commented out
         <div class="toolbar flat-button">
            <div class="align-left" id="${id}-paginator">&nbsp;</div>
            <span class="align-right yui-button-align">
               <span class="first-child">
                  <a href="${url.context}/page/my-tasks#filter=workflows|active" class="theme-color-1">${msg("link.activeTasks")}</a>
               </span>
               <span>&#124;</span>
               <span class="first-child">
                  <a href="${url.context}/page/my-tasks#filter=workflows|completed" class="theme-color-1">${msg("link.completedTasks")}</a>
               </span>
            </span>
            <div class="clear"></div>
         </div>
         -->
         <div class="body scrollableList" <#if args.height??>style="height: ${args.height}px;"</#if>>
            <div id="${id}-tasks"></div>
         </div>
      </div>
   </@>
</@>