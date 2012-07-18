/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.webapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class MultipartRequest extends HttpServletRequestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(MultipartRequest.class);
    private Map<String, List<String>> formParams;
    private Map<String, List<FileItem>> fileParams;
    private String charEncoding;

    public MultipartRequest(HttpServletRequest request, ServletFileUpload servletFileUpload, String charEncoding) throws IOException {
        super(request);
        formParams = new LinkedHashMap<String, List<String>>();
        fileParams = new LinkedHashMap<String, List<FileItem>>();
        this.charEncoding = charEncoding;

        parseRequest(request, servletFileUpload);
    }

    @SuppressWarnings("unchecked")
    private void parseRequest(HttpServletRequest request, ServletFileUpload servletFileUpload) throws IOException {
        try {
            List<FileItem> fileItems = servletFileUpload.parseRequest(request);

            for (FileItem item : fileItems) {
                if (item.isFormField()) {
                    addFormParam(item);
                } else {
                    addFileParam(item);
                }
            }

        } catch (FileUploadException e) {
            logger.error("Error in parsing fileupload request", e);

            throw new IOException(e.getMessage());
        }
    }

    private void addFileParam(FileItem item) {
        if (fileParams.containsKey(item.getFieldName())) {
            fileParams.get(item.getFieldName()).add(item);
        } else {
            List<FileItem> items = new ArrayList<FileItem>();
            items.add(item);
            fileParams.put(item.getFieldName(), items);
        }
    }

    private void addFormParam(FileItem item) {
        if (formParams.containsKey(item.getFieldName())) {
            formParams.get(item.getFieldName()).add(item.getString());
        } else {
            List<String> items = new ArrayList<String>();
            
            try {
                if (charEncoding != null) {
                    items.add(item.getString(charEncoding));
                } else {
                    items.add(item.getString());
                }
            } catch (UnsupportedEncodingException ex) {
                logger.error("Unsupported encoding for file upload given! Assuming systems default encoding!", ex);
                charEncoding = null;
                items.add(item.getString());
            }
            
            formParams.put(item.getFieldName(), items);
        }
    }

    @Override
    public String getParameter(String name) {
        if (formParams.containsKey(name)) {
            List<String> values = formParams.get(name);
            if (values.isEmpty()) {
                return "";
            } else {
                return values.get(0);
            }
        } else {
            return null;
        }
    }

    @Override
    public Map getParameterMap() {
        return Collections.unmodifiableMap(formParams);
    }

    @Override
    public Enumeration getParameterNames() {
        Set<String> paramNames = new LinkedHashSet<String>();
        paramNames.addAll(formParams.keySet());
        paramNames.addAll(fileParams.keySet());

        return Collections.enumeration(paramNames);
    }

    @Override
    public String[] getParameterValues(String name) {
        if (formParams.containsKey(name)) {
            List<String> values = formParams.get(name);
            if (values.isEmpty()) {
                return new String[0];
            } else {
                return values.toArray(new String[values.size()]);
            }
        } else {
            return null;
        }
    }

    public FileItem getFileItem(String name) {
        if (fileParams.containsKey(name)) {
            return fileParams.get(name).get(0);
        } else {
            return null;
        }
    }

    public List<FileItem> getFileItems(String name) {
        if (fileParams.containsKey(name)) {
            return fileParams.get(name);
        } else {
            return Collections.emptyList();
        }
    }

    //Workaround to mimic ajax request since flash does not allow custom request headers
    @Override
    public String getHeader(String name) {
        if (name != null && name.equalsIgnoreCase("Faces-Request")) {
            return "partial/ajax";
        } else {
            return ((HttpServletRequest) getRequest()).getHeader(name);
        }
    }
}