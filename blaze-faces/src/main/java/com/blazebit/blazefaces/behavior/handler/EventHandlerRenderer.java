/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.behavior.handler;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

import com.blazebit.blazefaces.renderkit.CoreRenderer;
import com.blazebit.blazefaces.util.ComponentUtil;
import com.blazebit.blazefaces.util.RendererUtil;

public class EventHandlerRenderer extends CoreRenderer {
    
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        EventHandler eh = (EventHandler) component;
        StringBuilder scriptBuilder = new StringBuilder();

        if (!eh.getChildren().isEmpty()) {
            String event = getEventName(eh);
            String domEvent = event;
            
            if("valueChange".equals(event)){
                domEvent = "change";
            }else if("action".equals(event)){
                domEvent = "click";
            }
            
            String target = (String) component.getAttributes().get("for");
            UIComponent source = null;
            
            if(target != null){
                source = ComponentUtil.findComponent(context.getViewRoot(), target);
            }else{
                source =  component.getParent();
            }
            
            scriptBuilder.append("BlazeJS.EventHandler.add('");
            scriptBuilder.append(ComponentUtil.escapeSelectorId(source.getClientId()));
            scriptBuilder.append("','");
            scriptBuilder.append(domEvent);
            scriptBuilder.append("',");
            
            encodeBehaviors(scriptBuilder, context, source, eh, event);
            
            scriptBuilder.append(");");
        }
        
        if(scriptBuilder.length() > 0)
            RendererUtil.addBodyBottomScript(context, scriptBuilder.toString());
    }
    
    private void encodeBehaviors(StringBuilder scriptBuilder, FacesContext context, UIComponent source, EventHandler component, String event){
        List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
        scriptBuilder.append("BlazeJS.EventHandler.create('");
        scriptBuilder.append("parallel".equals(component.getAttributes().get("type")) ? "parallel" : "sequential");
        scriptBuilder.append("',[");
            
        for (Iterator<UIComponent> childIterator = component.getChildren().iterator(); childIterator.hasNext();) {
            UIComponent current = childIterator.next();
            
            if(current instanceof EventHandler){
                encodeBehaviors(scriptBuilder, context, source, (EventHandler) current, event);
            }else if(current instanceof EventHandler.DefinitionOrderComponent){
                ClientBehavior behavior = ((EventHandler.DefinitionOrderComponent)current).getBehavior();
                ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, source, event, null, params);

                scriptBuilder.append("function(){");
                scriptBuilder.append(behavior.getScript(cbc));
                scriptBuilder.append("}");
            }else{
                continue;
            }

            if(childIterator.hasNext()){
                scriptBuilder.append(",");
            }
        }
        
        scriptBuilder.append("])");
    }

    private String getEventName(EventHandler eh) {
        String event = (String) eh.getAttributes().get("event");
        
        if(event != null){
            return event;
        }
        
        if(eh.getParent() instanceof UIInput)
            return "valueChange";
        else
            return "action";
    }
    
    
}
