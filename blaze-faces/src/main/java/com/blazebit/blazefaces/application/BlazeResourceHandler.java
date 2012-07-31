/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.application;

import com.blazebit.blazefaces.model.StreamedContent;
import com.blazebit.blazefaces.util.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class BlazeResourceHandler extends ResourceHandlerWrapper {

    private static final Logger logger = Logger.getLogger(BlazeResourceHandler.class.getName());
    private ResourceHandler wrapped;

    public BlazeResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return this.wrapped;
    }
    
    @Override
    public void handleResourceRequest(FacesContext context) throws IOException {
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String library = params.get("ln");
        String dynamicContentId = params.get(Constants.DYNAMIC_CONTENT_PARAM);
        
        if(dynamicContentId != null && library != null && library.equals(Constants.LIBRARY)) {
            Map<String,Object> session = context.getExternalContext().getSessionMap();
            StreamedContent streamedContent = null;
            
            try {
                String dynamicContentEL = (String) session.get(dynamicContentId);
                ELContext eLContext = context.getELContext();
                ValueExpression ve = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), dynamicContentEL, StreamedContent.class);
                streamedContent = (StreamedContent) ve.getValue(eLContext);

                ExternalContext externalContext = context.getExternalContext();
                externalContext.setResponseStatus(200);
                externalContext.setResponseContentType(streamedContent.getContentType());

                byte[] buffer = new byte[2048];

                int length;
                InputStream inputStream = streamedContent.getStream();
                while ((length = (inputStream.read(buffer))) >= 0) {
                	externalContext.getResponseOutputStream().write(buffer, 0, length);
                }

                externalContext.responseFlushBuffer();
                context.responseComplete();

            } catch(Exception e) {
                logger.log(Level.SEVERE, "Error in streaming dynamic resource. {0}", new Object[]{e.getMessage()});
            }
            finally {
                //cleanup
                session.remove(dynamicContentId);
                
                if(streamedContent != null) {
                    streamedContent.getStream().close();
                }
            }
        }
        else {
           super.handleResourceRequest(context); 
        }
    }
}