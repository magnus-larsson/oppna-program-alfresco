/**
 * @overridden projects/slingshot/source/web/components/dashlets/my-documents.js
 */
(function(onReady) {

   Alfresco.dashlet.MyDocuments.prototype.renderCellAction = function MyDocuments_renderCellAction(elCell, oRecord, oColumn, oData) {
      var me = this;

      var username = Alfresco.constants.USERNAME;

      // is this a working copy?
      var data = oRecord.getData();
      
      var nodeRef = data.nodeRef;

      // check that it's a working copy
      // and that whe have editing rights
      if (data.custom && data.custom.isWorkingCopy && data.custom.isWorkingCopy === true && data.permissions && data.permissions.userAccess) {
         Dom.setStyle(elCell, "width", oColumn.width + "px");

         Dom.setStyle(elCell.parentNode, "width", oColumn.width + "px");

         var title = this.msg('checkin.title.cancel.editing').replace('{0}', data.lockedBy);

         // if locked by user offer check in
         if (data.lockedByUser === username && data.permissions.userAccess.edit) {
            elCell.innerHTML = '<button>' + this.msg('checkin.label.checkin') + '</button>';

            // make a check in button
            Alfresco.thirdparty.createCheckInButton(Dom.getFirstChild(elCell), data, function() {
               me.reloadDataTable();

               YAHOO.Bubbling.fire('checkin.document', nodeRef);
            }, me);
         } else if (data.permissions.userAccess['cancel-checkout']) {
            // if locked by other we might have the right to cancel
            elCell.innerHTML = '<button title="' + title + '">' + this.msg('checkin.label.cancel.editing') + '</button>';

            // make a check in button
            Alfresco.thirdparty.createCancelEditingButton(Dom.getFirstChild(elCell), data.nodeRef, function() {
               me.reloadDataTable();
            }, me);
         } else {
            elCell.innerHTML = title;
         }
      }
   };

   Alfresco.dashlet.MyDocuments.prototype.onReady = function() {
      onReady.call(this);

      var dataTable = this.widgets.alfrescoDataTable.getDataTable();

      dataTable.insertColumn({
         key : "action",
         sortable : false,
         formatter : this.bind(this.renderCellAction),
         width : 120
      });
   };

   Alfresco.dashlet.MyDocuments.prototype.getWebscriptUrl = function SimpleDocList_getWebscriptUrl()
   {
      return Alfresco.constants.PROXY_URI + "slingshot/doclib/doclist/documents/node/alfresco/sites/home?max=50";
   };

}(Alfresco.dashlet.MyDocuments.prototype.onReady));
