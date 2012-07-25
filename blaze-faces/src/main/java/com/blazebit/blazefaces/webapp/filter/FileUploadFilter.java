/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.webapp.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.blazebit.blazefaces.webapp.MultipartRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUploadFilter implements Filter {

    private final static Logger logger = Logger.getLogger(FileUploadFilter.class.getName());
    private final static String THRESHOLD_SIZE_PARAM = "thresholdSize";
    private final static String UPLOAD_DIRECTORY_PARAM = "uploadDirectory";
    private final static String CHARACTER_ENCODING = "charEncoding";
    private String thresholdSize;
    private String uploadDir;
    private String charEncoding;

    public void init(FilterConfig filterConfig) throws ServletException {
        thresholdSize = filterConfig.getInitParameter(THRESHOLD_SIZE_PARAM);
        uploadDir = filterConfig.getInitParameter(UPLOAD_DIRECTORY_PARAM);
        charEncoding = filterConfig.getInitParameter(CHARACTER_ENCODING);
        File uploadDirFile = null;

        if (uploadDir != null) {
            uploadDirFile = new File(uploadDir);
        }

        if (uploadDirFile == null || !uploadDirFile.exists() || !uploadDirFile.canWrite()) {
            logger.log(Level.WARNING, "Using temporary directory because the given directory is not present, does not exist or is not writeable!");
            // Even if the temp dir is default, set it.
            uploadDir = System.getProperty("java.io.tmpdir");
        }
        
        if(charEncoding == null){
            logger.log(Level.WARNING, "No encoding for upload filter has been set, assuming system default encoding!");
        }

        logger.log(Level.FINE, "FileUploadFilter initiated successfully");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);

        if (isMultipart) {
            logger.log(Level.FINE, "Parsing file upload request");
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            
            if (thresholdSize != null) {
                diskFileItemFactory.setSizeThreshold(Integer.valueOf(thresholdSize));
            }
            
            if (uploadDir != null) {
                diskFileItemFactory.setRepository(new File(uploadDir));
            }

            ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
            MultipartRequest multipartRequest = new MultipartRequest(httpServletRequest, servletFileUpload, charEncoding);

            logger.log(Level.FINE, "File upload request parsed succesfully.");
            filterChain.doFilter(multipartRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    public void destroy() {
        logger.log(Level.FINE, "Destroying FileUploadFilter");
    }
}
