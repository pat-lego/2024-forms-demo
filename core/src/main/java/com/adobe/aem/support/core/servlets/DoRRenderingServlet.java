package com.adobe.aem.support.core.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.util.fst.Outputs;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemds.guide.addon.dor.DoRGenerationException;
import com.adobe.aemds.guide.addon.dor.DoROptions;
import com.adobe.aemds.guide.addon.dor.DoRResult;
import com.adobe.aemds.guide.addon.dor.DoRService;;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "entforms/fd/output", methods = { "POST",
        "GET" }, extensions = "dor", selectors = "output")
public class DoRRenderingServlet extends SlingAllMethodsServlet {

    private DoRService doRService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Activate
    public DoRRenderingServlet(@Reference DoRService doRService) {
        this.doRService = doRService;
    }

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        Optional<String> formResource = Optional.ofNullable(request.getParameter("formResource"));

        if (formResource.isPresent()) {
            Resource form = request.getResourceResolver().getResource(formResource.get());
            try (OutputStream out = response.getOutputStream()) {
                DoRResult doRResult = generateDoR(form);
                response.setContentType("application/pdf");
                out.write(doRResult.getContent());
                out.flush();
                response.setStatus(SlingHttpServletResponse.SC_OK);
            } catch (DoRGenerationException e) {
                logger.error("Failed to generate DoR", e);
            }
        } else {
            logger.error("Missing mandatory parameter xdpPath as a query parameter");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        Optional<String> formResource = Optional.ofNullable(request.getParameter("formResource"));

        if (formResource.isPresent()) {
            Resource form = request.getResourceResolver().getResource(formResource.get());
            try (OutputStream out = response.getOutputStream()) {
                DoRResult doRResult = generateDoR(form);
                response.setContentType("application/pdf");
                out.write(doRResult.getContent());
                out.flush();
                response.setStatus(SlingHttpServletResponse.SC_OK);
            } catch (DoRGenerationException e) {
                logger.error("Failed to generate DoR", e);
            }
        } else {
            logger.error("Missing mandatory parameter xdpPath as a query parameter");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
    }

    protected DoRResult generateDoR(Resource formResource) throws DoRGenerationException {
        DoROptions doROptions = new DoROptions();
        doROptions.setData(
                "{\"afData\":{\"afUnboundData\":{\"data\":{\"floating_field_currentpagenumber\":1,\"floating_field_numberofpages\":1,\"floating_field_currentdate\":\"2024-03-13\",\"floating_field_currenttime\":\"13:58:12\"}},\"afBoundData\":{\"data\":{\"stateOverrides\":{\"element\":[{\"options\":{\"option\":[{}]}}]},\"Lead\":{\"FirstName\":\"Patrique\",\"LastName\":\"Legault\",\"email\":\"plegault@adobe.com\",\"MobilePhone\":\"16139142477\"}}},\"afSubmissionInfo\":{\"lastFocusItem\":\"guide[0].guide1[0].guideRootPanel[0].main[0]\",\"stateOverrides\":{},\"signers\":{}}}}");
        doROptions.setFormResource(formResource);
        java.util.Locale en = new java.util.Locale("en");
        doROptions.setLocale(en);
        return this.doRService.render(doROptions);

    }

}
