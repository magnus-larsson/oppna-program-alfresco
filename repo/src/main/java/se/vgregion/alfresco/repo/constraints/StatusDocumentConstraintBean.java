package se.vgregion.alfresco.repo.constraints;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.NodeRef;

import se.vgregion.alfresco.repo.model.VgrModel;

public class StatusDocumentConstraintBean extends ApelonNodeTypeConstraintBean {

  @Override
  protected String getValue(final NodeRef nodeRef) {
    final String value = super.getValue(nodeRef);

    final String internalId = getInternalId(nodeRef);

    return internalId + "|" + value;
  }

  private String getInternalId(final NodeRef nodeRef) {
    return AuthenticationUtil.runAs(new AuthenticationUtil.RunAsWork<String>() {

      @Override
      public String doWork() throws Exception {
        return (String) _nodeService.getProperty(nodeRef, VgrModel.PROP_APELON_INTERNALID);
      }

    }, AuthenticationUtil.getSystemUserName());
  }

}
