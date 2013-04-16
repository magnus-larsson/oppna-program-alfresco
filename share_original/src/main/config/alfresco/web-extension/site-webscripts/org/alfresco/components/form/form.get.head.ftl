<#-- @overridden projects/web-framework-commons/config/alfresco/site-webscripts/org/alfresco/components/form/form.get.head.ftl -->

<#include "../component.head.inc">
<!-- Form Assets -->
<link rel="stylesheet" type="text/css" href="${page.url.context}/res/yui/calendar/assets/calendar.css" />
<@link rel="stylesheet" type="text/css" href="${page.url.context}/res/components/object-finder/object-finder.css" />
<@link rel="stylesheet" type="text/css" href="${page.url.context}/res/components/form/form.css" />

<#if config.global.forms?exists && config.global.forms.dependencies?exists && config.global.forms.dependencies.css?exists>
<#list config.global.forms.dependencies.css as cssFile>
<link rel="stylesheet" type="text/css" href="${page.url.context}/res${cssFile}" />
</#list>
</#if>

<@script type="text/javascript" src="${page.url.context}/res/components/form/form.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/components/form/date.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/components/form/date-picker.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/components/form/period.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/components/form/search.js"></@script>
<@link rel="stylesheet" type="text/css" href="${page.url.context}/res/components/form/search.css" />
<@script type="text/javascript" src="${page.url.context}/res/components/object-finder/object-finder.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/yui/calendar/calendar.js"></@script>

<@link rel="stylesheet" type="text/css" href="${page.url.context}/res/themes/default/accordion.css" />
<@script type="text/javascript" src="${page.url.context}/res/js/accordion/accordion.js"></@script>

<@script type="text/javascript" src="${page.url.context}/res/yui/datasource/datasource.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/yui/autocomplete/autocomplete.js"></@script>

<@script type="text/javascript" src="${page.url.context}/res/yui/treeview/treeview.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/components/form/tree-select.js"></@script>
<@link rel="stylesheet" type="text/css" href="${page.url.context}/res/components/form/tree-select.css" />

<@script type="text/javascript" src="${page.url.context}/res/components/form/ajax-selectone.js"></@script>
<@link rel="stylesheet" type="text/css" href="${page.url.context}/res/components/form/ajax-selectone.css" />

<@script type="text/javascript" src="${page.url.context}/res/components/form/selectstatus.js"></@script>

<script type="text/javascript" src="${page.url.context}/res/modules/editors/tiny_mce/tiny_mce${DEBUG?string("_src", "")}.js"></script>
<@script type="text/javascript" src="${page.url.context}/res/modules/editors/tiny_mce.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/components/form/rich-text.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/components/form/content.js"></@script>
<@script type="text/javascript" src="${page.url.context}/res/components/form/workflow/transitions.js"></@script>

<#if config.global.forms?exists && config.global.forms.dependencies?exists && config.global.forms.dependencies.js?exists>
<#list config.global.forms.dependencies.js as jsFile>
<script type="text/javascript" src="${page.url.context}/res${jsFile}"></script>
</#list>
</#if>