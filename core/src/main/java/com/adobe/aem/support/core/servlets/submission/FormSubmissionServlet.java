package com.adobe.aem.support.core.servlets.submission;

import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "entforms/fd/submit", 
methods = { "POST"}, extensions = "submit", selectors = "jcr")
public class FormSubmissionServlet extends SlingAllMethodsServlet {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException {
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            logger.info(params.nextElement());
        }

        response.setStatus(SlingHttpServletResponse.SC_OK);
    }
}
