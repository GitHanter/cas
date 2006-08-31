/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.web.support;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.SimpleService;
import org.springframework.util.StringUtils;
import org.springframework.webflow.RequestContext;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 *
 */
public class CasArgumentExtractor extends AbstractArgumentExtractor {

    private static final String PARAM_SERVICE = "service";
    
    private static final String PARAM_TICKET = "ticket";

    protected String getArtifactParameterName() {
        return PARAM_TICKET;
    }

    protected String getServiceParameterName() {
        return PARAM_SERVICE;
    }
    
    public final Service extractService(final HttpServletRequest request) {
        final String service = request.getParameter(getServiceParameterName());
        
        if (!StringUtils.hasText(service)) {
            return null;
        }
        
        return new SimpleService(WebUtils.stripJsessionFromUrl(service));
    }

    public String constructUrlForRedirct(final RequestContext context) {
        final String service = context.getRequestParameters().get(getServiceParameterName());
        final String serviceTicket = WebUtils.getServiceTicketFromRequestScope(context);
        
        if (service == null) {
            return null;
        }
        
        final StringBuffer buffer = new StringBuffer();
        
        synchronized (buffer) {
            buffer.append(service);
            buffer.append(service.indexOf('?') != -1 ? "&" : "?");
            buffer.append(getArtifactParameterName());
            buffer.append("=");
            buffer.append(serviceTicket);
        }
        
        return buffer.toString();
    }    
}
