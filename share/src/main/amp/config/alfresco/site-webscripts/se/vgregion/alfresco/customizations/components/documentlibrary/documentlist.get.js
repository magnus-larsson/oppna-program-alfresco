<import resource="classpath:/alfresco/templates/org/alfresco/import/alfresco-util.js">
<import resource="classpath:/alfresco/site-webscripts/org/alfresco/components/documentlibrary/include/documentlist.lib.js">

function main()
{
   AlfrescoUtil.param('site', null);
   AlfrescoUtil.param('container', 'documentLibrary');

   var connector = remote.connect("alfresco");

   var result = connector.get("/slingshot/doclib/container/" + model.site + "/" + model.container);

   var containerType = "";

   if (result.status == 200) {
      var data = eval('(' + result + ')');
       containerType = data.container.type;
   }

   model.containerType = containerType;
}

main();