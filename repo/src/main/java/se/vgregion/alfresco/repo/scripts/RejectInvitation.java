package se.vgregion.alfresco.repo.scripts;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.service.cmr.invitation.Invitation;
import org.alfresco.service.cmr.invitation.InvitationService;
import org.alfresco.service.cmr.invitation.NominatedInvitation;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class RejectInvitation extends DeclarativeWebScript {

  private AuthenticationService _authenticationService;
  private InvitationService _invitationService;

  public void setInvitationService(final InvitationService invitationService) {
    _invitationService = invitationService;
  }

  public void setAuthenticationService(final AuthenticationService authenticationService) {
    _authenticationService = authenticationService;
  }

  @Override
  protected Map<String, Object> executeImpl(final WebScriptRequest req, final Status status, final Cache cache) {
    String inviteId = req.getParameter("inviteId");

    if (inviteId != null) {
      Invitation i = _invitationService.getInvitation(inviteId);

      if (i instanceof NominatedInvitation) {
        //check that we are the invitee
        String approverUserName = _authenticationService.getCurrentUserName();

        if (approverUserName.equals(i.getInviteeUserName())) {
          _invitationService.reject(inviteId,"User rejects invitation");
        } else {
          status.setCode(Status.STATUS_FORBIDDEN,"You are not the invitee");
        }
      } else {
        status.setCode(Status.STATUS_INTERNAL_SERVER_ERROR,"Invitation is not nominated");
      }

    } else {
      status.setCode(Status.STATUS_BAD_REQUEST,"No inviteId specified");
    }

    Map<String,Object> model = new HashMap<String,Object>();
    model.put("status","OK");
    return model;
  }

}
