/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.owasp.validator.html.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blazebit.blazefaces.context.BlazeEncoder;
import com.blazebit.blazefaces.context.BlazeResponseStream;
import com.blazebit.blazefaces.context.BlazeResponseWriter;

/**
 *
 * @author Christian Beikov
 */
public class RendererUtil {

    private static final Logger log = LoggerFactory.getLogger(RendererUtil.class);
    private static final Map<String, String> cssMap = new HashMap<String, String>();
    public static final String BODY_BOTTOM_SCRIPT_KEY = "com.blazebit.blazefaces.BODY_BOTTOM_SCRIPT_KEY";

    static {
        cssMap.put("rel", "stylesheet");
    }

    public static void encodeCss(FacesContext context, String library, String resource) throws IOException {
        encodeResource(context, library, resource, "link", "text/css", false, cssMap, null);
    }

    public static void encodeCss(FacesContext context, String library, String resource, String condition) throws IOException {
        encodeResource(context, library, resource, "link", "text/css", false, cssMap, condition);
    }

    public static void encodeExternalCss(FacesContext context, String href) throws IOException {
        encodeExternalResource(context, href, "link", "text/css", false, cssMap, null);
    }

    public static void encodeExternalCss(FacesContext context, String href, String condition) throws IOException {
        encodeExternalResource(context, href, "link", "text/css", false, cssMap, condition);
    }

    public static void encodeScript(FacesContext context, String library, String resource) throws IOException {
        encodeResource(context, library, resource, "script", "text/javascript", true, null, null);
    }

    public static void encodeScript(FacesContext context, String library, String resource, String condition) throws IOException {
        encodeResource(context, library, resource, "script", "text/javascript", true, null, condition);
    }

    public static void encodeExternalScript(FacesContext context, String src) throws IOException {
        encodeExternalResource(context, src, "script", "text/javascript", true, null, null);
    }

    public static void encodeExternalScript(FacesContext context, String src, String condition) throws IOException {
        encodeExternalResource(context, src, "script", "text/javascript", true, null, condition);
    }

    public static Policy getPolicy() {
        try {
            return Policy.getInstance(RendererUtil.class.getClassLoader().getResource("META-INF/antisamy-tinymce-1.4.4.xml"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void encodeAttribute(ResponseWriter writer, String attribute, Object value, String defaultValue) throws IOException {
        if (value == null) {
            if (defaultValue != null) {
                writer.writeAttribute(attribute, defaultValue, null);
            }

            return;
        }

        writer.writeAttribute(attribute, value, null);
    }

    public static String encodeToString(FacesContext facesContext, BlazeEncoder encoder) throws IOException {
        return encodeToString(facesContext, new HashMap<String, Object>(), encoder);
    }

    public static String encodeToString(FacesContext facesContext, Map<String, Object> attributes, BlazeEncoder encoder) throws IOException {
        if (attributes == null) {
            throw new NullPointerException();
        }

        BlazeResponseStream responseStream = new BlazeResponseStream();
        BlazeResponseWriter responseWriter = new BlazeResponseWriter();
        ResponseStream rs = facesContext.getResponseStream();
        ResponseWriter rw = facesContext.getResponseWriter();

        facesContext.setResponseStream(responseStream);
        facesContext.setResponseWriter(rw.cloneWithWriter(responseWriter));

        try {
            encoder.encode(facesContext, attributes);
        } finally {
            if (rs != null) {
                facesContext.setResponseStream(rs);
            }
            if (rw != null) {
                facesContext.setResponseWriter(rw);
            }
        }

        String content = responseStream.getContent();

        if (content == null || content.isEmpty()) {
            content = responseWriter.getContent();
        }
        return content.trim();
    }

    public static String encodeComponent(FacesContext facesContext, UIComponent component) throws IOException {
        BlazeResponseStream responseStream = new BlazeResponseStream();
        BlazeResponseWriter responseWriter = new BlazeResponseWriter();
        ResponseStream rs = facesContext.getResponseStream();
        ResponseWriter rw = facesContext.getResponseWriter();

        facesContext.setResponseStream(responseStream);
        facesContext.setResponseWriter(rw.cloneWithWriter(responseWriter));

        try {
            component.encodeAll(facesContext);
        } finally {
            if (rs != null) {
                facesContext.setResponseStream(rs);
            }
            if (rw != null) {
                facesContext.setResponseWriter(rw);
            }
        }

        String content = responseStream.getContent();

        if (content == null || content.isEmpty()) {
            content = responseWriter.getContent();
        }
        return content.trim();
    }

    private static void encodeResource(FacesContext context, String library, String resource, String element, String type, boolean srcAttribute, Map<String, String> attributes, String condition) throws IOException {
        Resource res = context.getApplication().getResourceHandler().createResource(resource, library);

        if (res == null) {
            throw new FacesException("Cannot find \"" + resource + "\" resource of \"" + library + "\" library");
        } else {
            encodeExternalResource(context, res.getRequestPath(), element, type, srcAttribute, attributes, condition);
        }
    }

    private static void encodeExternalResource(FacesContext context, String srcOrHref, String element, String type, boolean srcAttribute, Map<String, String> attributes, String condition) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        if (condition != null) {
            writer.write("<!--[if ");
            writer.write(condition);
            writer.write("]>");
        }

        writer.startElement(element, null);
        writer.writeAttribute("type", type, null);

        if (attributes != null) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                writer.writeAttribute(entry.getKey(), entry.getValue(), null);
            }
        }

        srcOrHref = context.getExternalContext().encodeResourceURL(srcOrHref);

        if (srcAttribute) {
            writer.writeURIAttribute("src", srcOrHref, null);
        } else {
            writer.writeURIAttribute("href", srcOrHref, null);
        }

        writer.endElement(element);

        if (condition != null) {
            writer.write("<![endif]-->");
        }

    }

    @SuppressWarnings("unchecked")
	public static void addBodyBottomScript(FacesContext ctx, String script) {
        List<String> scripts = (List<String>) ctx.getAttributes().get(BODY_BOTTOM_SCRIPT_KEY);

        if (scripts == null) {
            scripts = new ArrayList<String>();
        }

        scripts.add(script);
        ctx.getAttributes().put(BODY_BOTTOM_SCRIPT_KEY, scripts);
    }

    @SuppressWarnings("unchecked")
	public static List<String> getBodyBottomScripts(FacesContext ctx) {
        return (List<String>) ctx.getAttributes().get(BODY_BOTTOM_SCRIPT_KEY);
    }

    public static String getEventHandlerScript(FacesContext context, String targetClientId, String event, String code) {
        StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("BlazeJS.EventHandler.add('");
        scriptBuilder.append(ComponentUtil.escapeSelectorId(targetClientId));
        scriptBuilder.append("','");
        scriptBuilder.append(event);
        scriptBuilder.append("',function(){");
        scriptBuilder.append(code);
        scriptBuilder.append("});");
        return scriptBuilder.toString();
    }

    /**
     * Originally taken from mojarra implementation
     */
    public static void renderUnhandledMessages(FacesContext ctx) {
        if (ctx.isProjectStage(ProjectStage.Development)) {
            Application app = ctx.getApplication();
            HtmlMessages messages = (HtmlMessages) app.createComponent(HtmlMessages.COMPONENT_TYPE);
            messages.setId("javax_faces_developmentstage_messages");
            Renderer messagesRenderer = ctx.getRenderKit().getRenderer(HtmlMessages.COMPONENT_FAMILY, "javax.faces.Messages");
            messages.setErrorStyle("Color: red");
            messages.setWarnStyle("Color: orange");
            messages.setInfoStyle("Color: blue");
            messages.setFatalStyle("Color: red");
            messages.setTooltip(true);
            messages.setTitle("Project Stage[Development]: Unhandled Messages");
            messages.setRedisplay(false);

            try {
                messagesRenderer.encodeBegin(ctx, messages);
                messagesRenderer.encodeEnd(ctx, messages);
            } catch (IOException ioe) {
                log.error(ioe.toString(), ioe);
            }
        } else {
            Iterator<String> clientIds = ctx.getClientIdsWithMessages();
            int messageCount = 0;
            if (clientIds.hasNext()) {
                //Display each message possibly not displayed.
                StringBuilder builder = new StringBuilder();
                while (clientIds.hasNext()) {
                    String clientId = clientIds.next();
                    Iterator<FacesMessage> messages =
                            ctx.getMessages(clientId);
                    while (messages.hasNext()) {
                        FacesMessage message = messages.next();

                        if (message.isRendered()) {
                            continue;
                        }

                        messageCount++;
                        builder.append("\n");
                        builder.append("sourceId=").append(clientId);
                        builder.append("[severity=(").append(message.getSeverity());
                        builder.append("), summary=(").append(message.getSummary());
                        builder.append("), detail=(").append(message.getDetail()).append(")]");
                    }
                }
                if (messageCount > 0) {
                    log.info("jsf.non_displayed_message", builder.toString());
                }
            }
        }
    }

    public static boolean isPartialOrBehaviorAction(FacesContext context, String clientId) {
        if ((clientId == null) || (clientId.length() == 0)) {
            return false;
        }

        ExternalContext external = context.getExternalContext();
        Map<String, String> params = external.getRequestParameterMap();
        String source = params.get("javax.faces.source");

        if (!clientId.equals(source)) {
            return false;
        }

        // First check for a Behavior action event.
        String behaviorEvent = params.get("javax.faces.behavior.event");

        if (null != behaviorEvent) {
            return ("action".equals(behaviorEvent));
        }

        // Not a Behavior-related request.  Check for jsf.ajax.request()
        // request params.
        String partialEvent = params.get("javax.faces.partial.event");
        return ("click".equals(partialEvent));
    }
}
