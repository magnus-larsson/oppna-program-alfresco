package se.vgregion.alfresco.repo.node.version.common.versionlabel;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.repo.version.VersionModel;
import org.alfresco.repo.version.VersionServicePolicies.BeforeCreateVersionPolicy;
import org.alfresco.repo.version.common.versionlabel.SerialVersionLabelPolicy;
import org.alfresco.service.cmr.lock.LockService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.version.Version;
import org.alfresco.service.cmr.version.VersionType;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.VersionNumber;
import org.apache.log4j.Logger;

import se.vgregion.alfresco.repo.model.VgrModel;
import se.vgregion.alfresco.repo.utils.ServiceUtils;

public class ConfigurableSerialVersionLabelPolicy extends SerialVersionLabelPolicy implements BeforeCreateVersionPolicy {

  private final static Logger LOG = Logger.getLogger(ConfigurableSerialVersionLabelPolicy.class);

  private final static QName POLICY_BEFORE_CREATE_VERSION = QName.createQName(NamespaceService.ALFRESCO_URI, "beforeCreateVersion");

  private final static String DELIMITER_KEY = "delimiter";
  private final static String START_MAJOR_KEY = "startmajor";
  private final static String START_MINOR_KEY = "startminor";

  private final static String DEFAULT_DELIMITER = ".";
  private final static int DEFAULT_START_MAJOR = 1;
  private final static int DEFAULT_START_MINOR = 0;

  private final String delimiter;
  private final int startMinor;
  private final int startMajor;

  private PolicyComponent _policyComponent;

  private NodeService _nodeService;

  private ServiceUtils _serviceUtils;

  private final ThreadLocal<NodeRef> _savedNodeRef = new ThreadLocal<NodeRef>();

  public void setPolicyComponent(final PolicyComponent policyComponent) {
    _policyComponent = policyComponent;
  }

  public void setNodeService(final NodeService nodeService) {
    _nodeService = nodeService;
  }

  public void setLockService(final LockService lockService) {
  }

  public void setServiceUtils(final ServiceUtils serviceUtils) {
    _serviceUtils = serviceUtils;
  }

  public ConfigurableSerialVersionLabelPolicy(final Properties properties) {
    delimiter = properties.getProperty(DELIMITER_KEY) != null ? properties.getProperty(DELIMITER_KEY) : DEFAULT_DELIMITER;
    startMajor = properties.getProperty(START_MAJOR_KEY) != null ? Integer.parseInt(properties.getProperty(START_MAJOR_KEY)) : DEFAULT_START_MAJOR;
    startMinor = properties.getProperty(START_MINOR_KEY) != null ? Integer.parseInt(properties.getProperty(START_MINOR_KEY)) : DEFAULT_START_MINOR;

    if (LOG.isDebugEnabled()) {
      LOG.debug("Initialized ConfigurableSerialVersionLabelPolicy with delimiter = " + delimiter + ", major start = " + startMajor
          + ", minor start =  " + startMinor);
    }
  }

  /**
   * Get the version label value base on the data provided.
   *
   * @param preceedingVersion
   *          the preceding version, null if none
   * @param versionNumber
   *          the new version number
   * @param versionProperties
   *          the version property values
   * @return the version label
   *
   * @Override
   */
  @Override
  public String calculateVersionLabel(final QName classRef, final Version preceedingVersion, final int versionNumber,
      final Map<String, Serializable> versionProperties) {
    SerialVersionLabel serialVersionNumber = null;

    if (preceedingVersion != null) {
      serialVersionNumber = new SerialVersionLabel(preceedingVersion.getVersionLabel());

      VersionType versionType = null;

      if (versionProperties != null) {
        versionType = (VersionType) versionProperties.get(VersionModel.PROP_VERSION_TYPE);
      }

      if (VersionType.MAJOR.equals(versionType) == true) {
        serialVersionNumber.majorIncrement();
      } else {
        serialVersionNumber.minorIncrement();
      }
    } else {
      final NodeRef nodeRef = _savedNodeRef.get();

      if (nodeRef != null) {
        final String baseVersion = (String) _nodeService.getProperty(nodeRef, VgrModel.PROP_VGR_DOK_VERSION);

        if (baseVersion != null && baseVersion.trim().length() > 0) {
          serialVersionNumber = new SerialVersionLabel(baseVersion);
          _savedNodeRef.remove();
        }
      }
    }

    if (serialVersionNumber == null) {
      serialVersionNumber = new SerialVersionLabel(null);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Calculated version label: " + serialVersionNumber.toString());
    }

    final String newVersion = serialVersionNumber.toString();

    if (preceedingVersion != null) {
      // set the new version
      _serviceUtils.replicateVersion(preceedingVersion.getVersionedNodeRef(), newVersion);
    }

    return newVersion;
  }

  @Override
  public void beforeCreateVersion(final NodeRef nodeRef) {
    // This is a horribly nasty hack, but until ETHREEOH-3183 is fixed there's
    // no real way around it.
    _savedNodeRef.set(nodeRef);
  }

  public void init() {
    _policyComponent.bindClassBehaviour(POLICY_BEFORE_CREATE_VERSION, VgrModel.TYPE_VGR_DOCUMENT, new JavaBehaviour(this, "beforeCreateVersion"));
  }

  private class SerialVersionLabel {

    /**
     * The major revision number
     */
    private int majorRevisionNumber;

    /**
     * The minor revision number
     */
    private int minorRevisionNumber;

    /**
     * Constructor
     *
     * @param version
     *          the version to take the version from
     */
    public SerialVersionLabel(final String versionLabel) {
      if (versionLabel != null && versionLabel.length() != 0) {
        final VersionNumber versionNumber = new VersionNumber(versionLabel);
        majorRevisionNumber = versionNumber.getPart(0);
        minorRevisionNumber = versionNumber.getPart(1);
      } else {
        majorRevisionNumber = startMajor;
        minorRevisionNumber = startMinor;
      }

      if (LOG.isDebugEnabled()) {
        LOG.debug("Extracted major = " + majorRevisionNumber + " and minor = " + minorRevisionNumber + " from " + versionLabel);
      }
    }

    /**
     * Increments the major revision number and sets the minor to zero.
     */
    public void majorIncrement() {
      this.majorRevisionNumber += 1;
      this.minorRevisionNumber = 0;
    }

    /**
     * Increments only the minor revision number
     */
    public void minorIncrement() {
      this.minorRevisionNumber += 1;
    }

    /**
     * Converts the serial version number into a string
     */
    @Override
    public String toString() {
      return this.majorRevisionNumber + delimiter + this.minorRevisionNumber;
    }
  }

}
