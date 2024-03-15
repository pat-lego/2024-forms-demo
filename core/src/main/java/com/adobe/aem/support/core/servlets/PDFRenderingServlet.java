package com.adobe.aem.support.core.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.output.api.OutputService;
import com.adobe.fd.output.api.OutputServiceException;
import com.adobe.fd.output.api.PDFOutputOptions;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "entforms/fd/output", methods = { "POST",
        "GET" }, extensions = "pdf", selectors = "output")
public class PDFRenderingServlet extends SlingAllMethodsServlet {

    private OutputService outputService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Activate
    public PDFRenderingServlet(@Reference OutputService outputService) {
        this.outputService = outputService;
    }

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        Optional<String> xdpPath = Optional.ofNullable(request.getParameter("xdpPath"));

        if (xdpPath.isPresent()) {
            try (OutputStream out = response.getOutputStream()) {
                PDFOutputOptions options = new PDFOutputOptions();
                Document pdf = this.outputService.generatePDFOutput("crx://" + xdpPath.get(), null, options);
                logger.info("Generated the pdf");
                response.setContentType("application/pdf");
                IOUtils.copy(pdf.getInputStream(), out);
                out.flush();
                response.setStatus(SlingHttpServletResponse.SC_OK);
            } catch (OutputServiceException e) {
                logger.error("Failed to generate PDF document from template {}", xdpPath.get());
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.error("Missing mandatory parameter xdpPath as a query parameter");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {
        Optional<String> xdpPath = Optional.ofNullable(request.getParameter("xdpPath"));

        if (xdpPath.isPresent()) {
            try (OutputStream out = response.getOutputStream()) {
                PDFOutputOptions options = new PDFOutputOptions();
                Document pdf = this.outputService.generatePDFOutput("crx://" + xdpPath.get(), null, options);
                logger.info("Generated the pdf");
                response.setContentType("application/pdf");
                IOUtils.copy(pdf.getInputStream(), out);
                out.flush();
                response.setStatus(SlingHttpServletResponse.SC_OK);
            } catch (OutputServiceException e) {
                logger.error("Failed to generate PDF document from template {}", xdpPath.get());
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.error("Missing mandatory parameter xdpPath as a query parameter");
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
