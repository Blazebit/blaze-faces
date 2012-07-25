/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.renderkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.blazebit.blazefaces.behavior.ajax.AjaxBehavior;
import com.blazebit.blazefaces.behavior.handler.EventHandler;
import com.blazebit.blazefaces.util.Constants;

public class CoreRenderer extends Renderer {

    protected void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
        for (Iterator<UIComponent> iterator = component.getChildren().iterator(); iterator.hasNext();) {
            UIComponent child = (UIComponent) iterator.next();
            renderChild(facesContext, child);
        }
    }

    protected void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }

        child.encodeBegin(facesContext);

        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }

    protected String getActionURL(FacesContext facesContext) {
        String actionURL = facesContext.getApplication().getViewHandler().getActionURL(facesContext, facesContext.getViewRoot().getViewId());

        return facesContext.getExternalContext().encodeActionURL(actionURL);
    }

    protected String getResourceURL(FacesContext facesContext, String value) {
        if (value.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
            return value;
        } else {
            String url = facesContext.getApplication().getViewHandler().getResourceURL(facesContext, value);

            return facesContext.getExternalContext().encodeResourceURL(url);
        }
    }

    protected String getResourceRequestPath(FacesContext facesContext, String resourceName) {
        Resource resource = facesContext.getApplication().getResourceHandler().createResource(resourceName, "blazefaces");

        return resource.getRequestPath();
    }

    public boolean isPostback(FacesContext facesContext) {
        return facesContext.getRenderKit().getResponseStateManager().isPostback(facesContext);
    }

    protected void renderDataMapAttributes(FacesContext facesContext, UIComponent component) throws IOException {
        renderDataMapAttributes(facesContext, component, null);
    }

    @SuppressWarnings("unchecked")
	protected void renderDataMapAttributes(FacesContext facesContext, UIComponent component, Map<String, String> extraDataMap) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        Map<String, String> map = new HashMap<String, String>();
        Object componentMap = component.getAttributes().get("dataMap");

        if (extraDataMap != null) {
            map.putAll(extraDataMap);
        }
        
        if (componentMap != null) {
            if(!(componentMap instanceof Map)){
                String mapText = componentMap.toString();
                String[] entries = mapText.split(",");
                
                for(String entry : entries){
                    String[] keyValuePair = entry.split("=");
                    
                    if(keyValuePair.length != 2)
                        throw new FacesException("Illegal dataMap String given on component with id: " + component.getId());
                    map.put(keyValuePair[0].trim(), keyValuePair[1].trim());
                }
            }else{
                map.putAll((Map<String, String>) componentMap);
            }
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (shouldRenderAttribute(entry.getValue())) {
                writer.writeAttribute("data-" + entry.getKey(), entry.getValue(), null);
            }
        }
    }

    protected void renderPassThruAttributes(FacesContext facesContext, UIComponent component, String var, String[] attrs) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        for (String event : attrs) {
            String eventHandler = (String) component.getAttributes().get(event);

            if (eventHandler != null) {
                writer.write(var + ".addListener(\"" + event.substring(2, event.length()) + "\", function(e){" + eventHandler + ";});\n");
            }
        }
    }

    protected void renderPassThruAttributes(FacesContext facesContext, UIComponent component, String[] attrs) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        for (String attribute : attrs) {
            Object value = component.getAttributes().get(attribute);

            if (shouldRenderAttribute(value)) {
                writer.writeAttribute(attribute, value.toString(), attribute);
            }
        }
    }

    protected void renderPassThruAttributes(FacesContext facesContext, UIComponent component, String[] attrs, String[] ignoredAttrs) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        for (String attribute : attrs) {
            if (isIgnoredAttribute(attribute, ignoredAttrs)) {
                continue;
            }

            Object value = component.getAttributes().get(attribute);

            if (shouldRenderAttribute(value)) {
                writer.writeAttribute(attribute, value.toString(), attribute);
            }
        }
    }

    private boolean isIgnoredAttribute(String attribute, String[] ignoredAttrs) {
        for (String ignoredAttribute : ignoredAttrs) {
            if (attribute.equals(ignoredAttribute)) {
                return true;
            }
        }

        return false;
    }

    protected boolean shouldRenderAttribute(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else if (value instanceof Number) {
            Number number = (Number) value;

            if (value instanceof Integer) {
                return number.intValue() != Integer.MIN_VALUE;
            } else if (value instanceof Double) {
                return number.doubleValue() != Double.MIN_VALUE;
            } else if (value instanceof Long) {
                return number.longValue() != Long.MIN_VALUE;
            } else if (value instanceof Byte) {
                return number.byteValue() != Byte.MIN_VALUE;
            } else if (value instanceof Float) {
                return number.floatValue() != Float.MIN_VALUE;
            } else if (value instanceof Short) {
                return number.shortValue() != Short.MIN_VALUE;
            }
        }

        return true;
    }

    protected boolean isPostBack() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getRenderKit().getResponseStateManager().isPostback(facesContext);
    }

    public String getEscapedClientId(String clientId) {
        return clientId.replaceAll(":", "\\\\\\\\:");
    }

    public boolean isValueEmpty(String value) {
        if (value == null || "".equals(value)) {
            return true;
        }

        return false;
    }

    public boolean isValueBlank(String value) {
        if (value == null) {
            return true;
        }

        return value.trim().equals("");
    }

    protected String addSubmitParam(String parent, String name, String value) {
        StringBuilder builder = new StringBuilder();

        builder.append(".addSubmitParam('").append(parent).append("','").append(name).append("','").append(value).append("')");

        return builder.toString();
    }

    protected String escapeText(String value) {
        return value == null ? "" : value.replaceAll("'", "\\\\'");
    }
    
    protected String decodeBehaviors(FacesContext context, UIComponent component)  {
        String clientId = component.getClientId();
        
        if(!(component instanceof ClientBehaviorHolder)) {
            return clientId;
        }

        Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();
        
        if(behaviors.isEmpty()) {
            return clientId;
        }

        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String behaviorEvent = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if(null != behaviorEvent) {
            List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if(behaviors.size() > 0) {
               String behaviorSource = params.get(Constants.PARTIAL_SOURCE_PARAM);
               
               if(behaviorSource != null && behaviorSource.equals(clientId)) {
                   for (ClientBehavior behavior: behaviorsForEvent) {
                       behavior.decode(context, component);
                   }
               }
            }
        }
        
        return clientId;
    }
    
    protected void encodeBehaviors(FacesContext context, ClientBehaviorHolder component) throws IOException {
        Map<String, List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();

        if (!behaviorEvents.isEmpty()) {
            for (Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
                String event = eventIterator.next();
                EventHandler eh = new EventHandler();                
                eh.getAttributes().put("event", event);
                eh.getAttributes().put("for", ((UIComponent)component).getId());
                
                for (Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                    eh.addClientBehavior(null, behaviorIter.next());
                }
                
                eh.encodeAll(context);
            }
        }
    }
    
    protected void encodeEventHandlers(FacesContext context, UIComponent component) throws IOException {
        for(UIComponent comp : component.getChildren()){
            if(comp instanceof EventHandler){
                comp.encodeAll(context);
            }
        }
    }

    protected boolean containsAjaxBehavior(FacesContext context, ClientBehaviorHolder comp) {
        Map<String, List<ClientBehavior>> map = comp.getClientBehaviors();
        
        if(map == null)
            return false;
        
        for(List<ClientBehavior> list : map.values()){
            for(ClientBehavior cb : list){
                if(cb instanceof AjaxBehavior || cb instanceof javax.faces.component.behavior.AjaxBehavior)
                    return true;
            }
        }
        
        return false;
    }
}