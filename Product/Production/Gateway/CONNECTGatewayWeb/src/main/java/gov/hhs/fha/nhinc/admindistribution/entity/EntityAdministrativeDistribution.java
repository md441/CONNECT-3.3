/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdministrativeDistribution_Service", portName = "AdministrativeDistribution_PortType", endpointInterface = "gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityadmindistribution", wsdlLocation = "WEB-INF/wsdl/EntityAdministrativeDistribution/EntityAdminDist.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityAdministrativeDistribution {

    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType body) {
        //TODO implement this method
        new EntityAdminDistributionImpl().sendAlertMessage(body, body.getAssertion(), body.getNhinTargetCommunities());
    }

}