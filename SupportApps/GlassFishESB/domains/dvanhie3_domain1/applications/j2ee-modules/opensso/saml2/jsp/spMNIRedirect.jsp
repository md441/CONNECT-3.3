<%--
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2006 Sun Microsystems Inc. All Rights Reserved
  
   The contents of this file are subject to the terms
   of the Common Development and Distribution License
   (the License). You may not use this file except in
   compliance with the License.

   You can obtain a copy of the License at
   https://opensso.dev.java.net/public/CDDLv1.0.html or
   opensso/legal/CDDLv1.0.txt
   See the License for the specific language governing
   permission and limitations under the License.

   When distributing Covered Code, include this CDDL
   Header Notice in each file and include the License file
   at opensso/legal/CDDLv1.0.txt.
   If applicable, add the following below the CDDL Header,
   with the fields enclosed by brackets [] replaced by
   your own identifying information:
   "Portions Copyrighted [year] [name of copyright owner]"

   $Id: spMNIRedirect.jsp,v 1.4 2009/03/03 01:54:13 qcheng Exp $

--%>




<%@ page import="com.sun.identity.shared.debug.Debug" %>
<%@ page import="com.sun.identity.saml2.common.SAML2Constants" %>
<%@ page import="com.sun.identity.saml2.common.SAML2Utils" %>
<%@ page import="com.sun.identity.saml2.common.SAML2Exception" %>
<%@ page import="com.sun.identity.saml2.profile.DoManageNameID" %>
<%@ page import="java.util.HashMap" %>

<%--
    spMNIHTTPRedirect.jsp processes the ManageNameIDRequest from
    the Service Provider with HttpRedirect binding.
    Required parameters to this jsp are : NONE
--%>
<html>

<head>
<title>SAMLv2 SP ManageNameIDRequest Process JSP with HttpRedirect binding</title>
</head>
<body bgcolor="#FFFFFF" text="#000000">
<%
    try {
        HashMap paramsMap = new HashMap();
        paramsMap.put(SAML2Constants.ROLE, SAML2Constants.SP_ROLE);
        String samlRequest =
                request.getParameter(SAML2Constants.SAML_REQUEST);

        String samlResponse =
                request.getParameter(SAML2Constants.SAML_RESPONSE);

        if (samlRequest != null) {
            DoManageNameID.processHttpRequest(request, response, paramsMap);
        }

        if (samlResponse != null) {
            boolean success = DoManageNameID.processManageNameIDResponse(
                                              request, response, paramsMap);
            if (success == true) {
                String relayState =
                    request.getParameter(SAML2Constants.RELAY_STATE);
                if (relayState != null) {
                    response.sendRedirect(relayState);
                } else {
                    %>
                    <jsp:forward
                        page="/saml2/jsp/default.jsp?message=mniSuccess" />
                    <%
                }
            } else {
                SAML2Utils.sendError(request, response, response.SC_BAD_REQUEST,
                    "requestProcessingMNIError",
                    SAML2Utils.bundle.getString("requestProcessingMNIError"));
            }
        }
    } catch (SAML2Exception e) {
        SAML2Utils.debug.error("Error processing ManageNameIDRequest " , e);
        SAML2Utils.sendError(request, response, response.SC_BAD_REQUEST,
            "requestProcessingMNIError",
            SAML2Utils.bundle.getString("requestProcessingMNIError") + " " +
            e.getMessage());
    }
%>

</body>
</html>
