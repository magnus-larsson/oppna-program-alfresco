<#-- @overridden projects/slingshot/config/alfresco/site-webscripts/org/alfresco/components/upload/upload.get.html.ftl -->

<@markup id="css" >
   <#-- CSS Dependencies -->
   <@link href="${url.context}/res/components/upload/dnd-upload.css" group="upload"/>
</@>

<@markup id="js">
   <#-- JavaScript Dependencies -->
   <@script type="text/javascript" src="${url.context}/res/components/upload/dnd-upload.js" group="upload"/>
   <#-- 
   Fix a bug where clicking the upload link in the "help" dashlet within a site gives 
   javascript errors and makes the upload dialog pop up over and over again when closed. 
   Could not extend in the corect way so this had to go as a full override.   
   -->
   <@script type="text/javascript" src="${url.context}/res/components/upload/vgr-dnd-upload.js" group="upload"/>
</@>

<@markup id="widgets">
   <@createWidgets group="upload"/>
   <@inlineScript group="upload">
      dndUpload.setInMemoryLimit("${inMemoryLimit}");
      dndUpload.setMaximumFileSizeLimit(${fileUploadSizeLimit});
   </@>
</@>

<@markup id="html">
   <@uniqueIdDiv>
      <#assign el=args.htmlid?html>
      <div id="${el}-dialog" class="dnd-upload hidden">
         <div class="hd">
            <span id="${el}-title-span"></span>
         </div>
         <div class="bd">
            <div id="${el}-file-selection-controls" class="browse-wrapper">
               <div class="center dnd-file-selection-control">
                  <input id="${el}-file-selection-button-overlay" type="button" value="${msg("button.selectFiles")}" tabindex="0"/>
               </div>
            </div>
         
            <div id="${el}-filelist-table" class="fileUpload-filelist-table"></div>
      
            <div class="status-wrapper">
               <span id="${el}-status-span" class="status"></span>
            </div>
            <div id="${el}-aggregate-data-wrapper">
              <div class="status-wrapper">
                 <span id="${el}-aggregate-status-span" class="status"></span>
              </div>
              <div id="${el}-aggregate-progress-div" class="aggregate-progress-div">
                 <span id="${el}-aggregate-progress-span" class="aggregate-progressSuccess-span">&nbsp;</span>
              </div>
            </div>
      
            <div id="${el}-versionSection-div">
               <div class="yui-g">
                  <h2>${msg("section.version")}</h2>
               </div>
               <div class="yui-gd">
                  <div class="yui-u first">
                     <span>${msg("label.version")}</span>
                  </div>
                  <div class="yui-u">
                     <input id="${el}-minorVersion-radioButton" type="radio" name="majorVersion" checked="checked" tabindex="0"/>
                     <label for="${el}-minorVersion-radioButton" id="${el}-minorVersion">${msg("label.minorVersion")}</label>
                  </div>
               </div>
               <div class="yui-gd">
                  <div class="yui-u first">&nbsp;
                  </div>
                  <div class="yui-u">
                     <input id="${el}-majorVersion-radioButton" type="radio" name="majorVersion" tabindex="0"/>
                     <label for="${el}-majorVersion-radioButton" id="${el}-majorVersion">${msg("label.majorVersion")}</label>
                  </div>
               </div>
               <div class="yui-gd">
                  <div class="yui-u first">
                     <label for="${el}-description-textarea">${msg("label.comments")}</label>
                  </div>
                  <div class="yui-u">
                     <textarea id="${el}-description-textarea" name="description" cols="80" rows="4" tabindex="0"></textarea>
                  </div>
               </div>
            </div>
      
            <!-- Templates for a file row -->
            <div style="display:none">
               <div id="${el}-left-div" class="fileupload-left-div">
                  <span class="fileupload-percentage-span">0%</span>
                  <input class="fileupload-contentType-input" type="hidden" value="cm:content"/>
               </div>
               <div id="${el}-center-div" class="fileupload-center-div">
                  <span class="fileupload-progressSuccess-span">&nbsp;</span>
                  <img src="${url.context}/res/components/images/generic-file-32.png" class="fileupload-docImage-img" alt="file" />
                  <span class="fileupload-progressInfo-span"></span>
                  <span class="fileupload-filesize-span"></span>
               </div>
               <div id="${el}-right-div" class="fileupload-right-div">
                  <img src="${url.context}/res/components/images/job-running-16.png" class="fileupload-status-img" alt="status" />
                  <img src="${url.context}/res/components/images/complete-16.png" class="fileupload-status-img hidden" alt="status" />
                  <img src="${url.context}/res/components/images/job-failed-16.png" class="fileupload-status-img hidden" alt="status" />
               </div>
            </div>
               <div class="bdft">
                  <input id="${el}-upload-button" type="button" value="${msg("button.upload")}" tabindex="0"/>
                  <input id="${el}-cancelOk-button" type="button" value="${msg("button.cancel")}" tabindex="0"/>
               </div>
         </div>
      </div>
   </@>
</@>

