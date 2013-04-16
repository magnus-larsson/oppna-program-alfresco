function main() {
   var filter = args.filter;
   
   var userPagesNode = search.findNode('avm://sitestore/-1|alfresco|site-data|pages|user');
   
   var userNodes = userPagesNode.children;
   
   var users = new Array();
   
   for (var x = 0; x < userNodes.length; x++) {
      var userNode = userNodes[x];
      
      var userName = userNode.properties['cm:owner'];
      
      if (filter && filter.length > 0) {
         if (userName.indexOf(filter) === 0) {
            users.push(userName);
         }
      } else {
         users.push(userName);
      }
   }
   
   model.users = users;
}

main();