package gov.hhs.fha.nhinc.docretrieve.nhin.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RespondingGatewayDeferredResponseRetrievePortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 11:16:36 PM
 */
public class NhinDocRetrieveDeferredRespProxyWebServiceSecuredImpl implements NhinDocRetrieveDeferredRespProxy {
    private static final Log logger = LogFactory.getLog(NhinDocRetrieveDeferredRespProxyWebServiceSecuredImpl.class);
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGatewayDeferredResponse_Retrieve_Service";
    private static final String PORT_LOCAL_PART = "RespondingGatewayDeferredResponse_Retrieve_Port_Soap";
    private static final String WSDL_FILE = "NhinDocRetrieveDeferredResp.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:xds-b:2007:Deferred:CrossGatewayRetrieveResponse_Message";
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    protected Log getLogger() {
        return logger;
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return oProxyHelper;
    }

    /**\
     *
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected RespondingGatewayDeferredResponseRetrievePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        RespondingGatewayDeferredResponseRetrievePortType port = null;
        Service service = getService();
        if (service != null) {
            getLogger().debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayDeferredResponseRetrievePortType.class);
            getWebServiceProxyHelper().initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            getLogger().error("Unable to obtain serivce - no port created.");
        }

        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                getLogger().error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetResponseType body, AssertionType assertion, NhinTargetSystemType target)
    {
         getLogger().debug("Begin sendToRespondingGateway");

        DocRetrieveAcknowledgementType response = null;


        try {
            String url = getWebServiceProxyHelper().getUrlFromTargetSystem(target, NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_RESPONSE);
            RespondingGatewayDeferredResponseRetrievePortType port = getPort(url, NhincConstants.DOC_RETRIEVE_ACTION, WS_ADDRESSING_ACTION, assertion);

            if (body == null) {
                getLogger().error("Message was null");
            } else if (port == null) {
                getLogger().error("port was null");
            } else {
                response = (DocRetrieveAcknowledgementType) getWebServiceProxyHelper().invokePort(port, RespondingGatewayDeferredResponseRetrievePortType.class, "respondingGatewayDeferredResponseCrossGatewayRetrieve", body);
            }
        } catch (Exception ex) {
            getLogger().error("Error calling respondingGatewayDeferredResponseCrossGatewayRetrieve: " + ex.getMessage(), ex);
            response = new DocRetrieveAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG);
            response.setMessage(regResp);
        }

        getLogger().debug("End sendToRespondingGateway");
        return response;
    }
}