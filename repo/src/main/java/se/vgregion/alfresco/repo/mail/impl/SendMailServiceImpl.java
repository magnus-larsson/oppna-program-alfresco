package se.vgregion.alfresco.repo.mail.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.repo.action.executer.MailActionExecuter;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ActionService;
import org.apache.log4j.Logger;

import se.vgregion.alfresco.repo.mail.SendMailService;

public class SendMailServiceImpl implements SendMailService {

  private static final Logger LOG = Logger.getLogger(SendMailServiceImpl.class);

  private ActionService actionService;

  @Override
  public void sendTextMail(String subject, String from, String to, String body) {
    List<String> toList = new ArrayList<String>();

    toList.add(to);

    sendTextMail(subject, from, toList, body);
  }

  @Override
  public void sendTextMail(String subject, String from, List<String> to, String body) {

    Action mailAction = actionService.createAction(MailActionExecuter.NAME);

    mailAction.setParameterValue(MailActionExecuter.PARAM_SUBJECT, subject);

    mailAction.setParameterValue(MailActionExecuter.PARAM_TO_MANY, (Serializable) to);

    mailAction.setParameterValue(MailActionExecuter.PARAM_FROM, from);

    mailAction.setParameterValue(MailActionExecuter.PARAM_TEXT, body);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Sending mail to " + to + " from " + from + " with subject " + subject);
    }

    actionService.executeAction(mailAction, null);

  }

  public void setActionService(ActionService actionService) {
    this.actionService = actionService;
  }

}
