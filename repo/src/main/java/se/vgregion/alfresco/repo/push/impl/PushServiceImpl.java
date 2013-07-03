package se.vgregion.alfresco.repo.push.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import se.vgregion.alfresco.repo.push.PushService;
import se.vgregion.alfresco.repo.utils.ServiceUtils;

public class PushServiceImpl implements PushService, InitializingBean {

  private static final Logger LOG = Logger.getLogger(PushServiceImpl.class);

  private boolean _dumpFeed;

  private String _pushServerUrl;

  private String _feedUrl;

  private ServiceUtils _serviceUtils;

  private SearchService _searchService;

  public void setDumpFeed(final boolean dumpFeed) {
    _dumpFeed = dumpFeed;
  }

  public void setPushServerUrl(final String pushServerUrl) {
    _pushServerUrl = pushServerUrl;
  }

  public void setFeedUrl(final String feedUrl) {
    _feedUrl = feedUrl;
  }

  public void setServiceUtils(final ServiceUtils serviceUtils) {
    _serviceUtils = serviceUtils;
  }

  public void setSearchService(SearchService _searchService) {
    this._searchService = _searchService;
  }

  @Override
  public boolean pushFile(final NodeRef nodeRef) {
    final List<NodeRef> nodeRefs = new ArrayList<NodeRef>();

    nodeRefs.add(nodeRef);

    return pushFiles(nodeRefs);
  }

  @Override
  public boolean pushFiles(final List<NodeRef> nodeRefs) {
    if (_dumpFeed) {
      dumpFeed();
    }
    if (!pingPush()) {
      return false;
    }
    final HttpClient client = new HttpClient();

    final PostMethod post = new UTF8PostMethod(_pushServerUrl);
    post.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");

    post.addParameter("hub.mode", "publish");
    post.addParameter("hub.url", _feedUrl);

    try {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Posting files to PuSH server");
      }
      final int response = client.executeMethod(post);

      final ByteArrayOutputStream baos = new ByteArrayOutputStream();

      post.getRequestEntity().writeRequest(baos);

      if (LOG.isDebugEnabled()) {
        LOG.debug(post.getResponseBodyAsString());

        for (final NodeRef nodeRef : nodeRefs) {
          LOG.debug("File '" + nodeRef + "' published to '" + _pushServerUrl + "' with feed URL '" + _feedUrl + "', response code " + response);
        }
      }

      return true;
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private void dumpFeed() {
    InputStream inputStream = null;
    OutputStream outputStream = null;

    try {
      final HttpClient client = new HttpClient();

      final GetMethod get = new GetMethod(_feedUrl);

      client.executeMethod(get);

      inputStream = get.getResponseBodyAsStream();

      final File file = File.createTempFile("alfresco_feed_", ".xml");

      outputStream = new FileOutputStream(file);

      IOUtils.copy(inputStream, outputStream);

      if (LOG.isDebugEnabled()) {
        LOG.debug("Dumped the feed '" + _feedUrl + "' to the file '" + file.getAbsolutePath() + "'.");
      }
    } catch (final Exception ex) {
      LOG.warn("Although dumpFeed is configured, the feed could not be dumped.", ex);
    } finally {
      IOUtils.closeQuietly(inputStream);
      IOUtils.closeQuietly(outputStream);
    }
  }

  private class UTF8PostMethod extends PostMethod {

    public UTF8PostMethod(final String url) {
      super(url);
    }

    @Override
    public String getRequestCharSet() {
      return "UTF-8";
    }
  }

  @Override
  public List<NodeRef> findPushedFiles(String publishStatus, String unpublishStatus, Date startTime, Date endTime) {

    String query = findPublishedDocumentsByStatusQuery(formatDate(startTime), formatDate(endTime), publishStatus, unpublishStatus);
    ResultSet result = findDocuments(query);
    try {
      return result.getNodeRefs();
    } finally {
      ServiceUtils.closeQuietly(result);
    }
  }

  private String formatDate(Date date) {
    if (date == null) {
      return "";
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    return sdf.format(date);
  }

  private String findPublishedDocumentsByStatusQuery(String startDate, String endDate, String publishStatus, String unpublishStatus) {
    StringBuffer query = new StringBuffer();

    query.append("TYPE:\"vgr:document\" AND ");
    query.append("ASPECT:\"vgr:published\" AND ");
    // query.append("vgr:dc\\.date\\.availablefrom:[MIN TO \"" + startDate +
    // "\"] AND ");
    // query.append("(ISNULL:\"vgr:dc.date.availableto\" OR vgr:dc\\.date\\.availableto:[\""
    // + startDate + "\" TO MAX]) AND ");
    String queryStartDate = "MIN";
    if (startDate != "") {
      queryStartDate = "\"" + startDate + "\"";
    }

    String queryEndDate = "MAX";
    if (endDate != "") {
      queryEndDate = "\"" + endDate + "\"";
    }

    if (startDate == "" && endDate == "") {
      // Include records not pushed yet in the result
      query.append("(ISNULL:\"vgr:pushed-for-publish\" OR ");
      query.append("ISNULL:\"vgr:pushed-for-unpublish\") ");
    } else {
      // When we have a start or end date, show only documents scheduled for publish/unpublish
      query.append("(vgr\\:pushed\\-for\\-publish:[" + queryStartDate + " TO " + queryEndDate + "] OR ");
      query.append("vgr\\:pushed\\-for\\-unpublish:[" + queryStartDate + " TO " + queryEndDate + "]) ");
    }
    // query.append("vgr\\:pushed\\-for\\-unpublish:[" + queryStartDate +
    // " TO "+ queryEndDate +"] AND ");
    if (publishStatus == null) {
      query.append("AND ISNULL:\"vgr:publish-status\" ");
    } else if (publishStatus.length() > 0 && !"any".equalsIgnoreCase(publishStatus)){
      query.append("AND vgr\\:publish\\-status: \"" + publishStatus + "\" ");
    }

    if (unpublishStatus == null) {
      query.append("AND ISNULL:\"vgr:unpublish-status\" ");
    } else if (unpublishStatus.length() > 0 && !"any".equalsIgnoreCase(unpublishStatus)){
      query.append("AND vgr\\:unpublish\\-status: \"" + unpublishStatus + "\" ");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Query for finding documents scheduled for publishing/unpublishing: " + query.toString());
    }

    return query.toString();
  }

  private ResultSet findDocuments(String query) {
    SearchParameters searchParameters = new SearchParameters();

    searchParameters.addStore(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);
    searchParameters.setLanguage(SearchService.LANGUAGE_FTS_ALFRESCO);
    searchParameters.setQuery(query.toString());

    ResultSet result = _searchService.query(searchParameters);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Documents found for query: " + query.toString());
      LOG.debug("Count: " + result.length());
      LOG.debug("");
    }

    return result;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(_serviceUtils);
    Assert.hasText(_feedUrl);
    Assert.hasText(_pushServerUrl);
    Assert.notNull(_searchService);
  }

  @Override
  public boolean pingPush() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Pinging PuSH server");
    }

    if (!_serviceUtils.pingServer(_pushServerUrl)) {
      LOG.warn("Can't contact PuSH server for feed '" + _feedUrl + "', exiting...");

      return false;
    } else {
      return true;
    }
  }

}
