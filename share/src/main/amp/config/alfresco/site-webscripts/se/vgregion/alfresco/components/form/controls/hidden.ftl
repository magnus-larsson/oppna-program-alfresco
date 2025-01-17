<#-- @overridden projects/web-framework-commons/config/alfresco/site-webscripts/org/alfresco/components/form/controls/hidden.ftl -->

<#-- Renders a hidden form field for edit and create modes only -->
<#assign fieldValue = "">
<#if field.control.params.contextProperty??>
   <#if context.properties[field.control.params.contextProperty]??>
      <#assign fieldValue = context.properties[field.control.params.contextProperty]>
   <#elseif args[field.control.params.contextProperty]??>
      <#assign fieldValue = args[field.control.params.contextProperty]>
   </#if>
<#elseif context.properties[field.name]??>
   <#assign fieldValue = context.properties[field.name]>
<#else>
   <#assign fieldValue = field.value>
</#if>


<#-- always add a hidden field, even  in view mode since other fields presentation
     might be dependant on it, i.e. if this is a .id field it's probably needed
     for the presentation. -->
      
<input type="hidden" name="${field.name}" id="${fieldHtmlId}" 
      <#if field.value?is_number>value="${fieldValue?c}"<#else>value="${fieldValue?html}"</#if> />

