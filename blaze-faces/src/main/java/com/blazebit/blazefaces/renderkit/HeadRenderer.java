/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.renderkit;

import com.blazebit.blazefaces.util.AgentUtil;
import com.blazebit.blazefaces.util.FeatureDetectionUtil;
import com.blazebit.blazefaces.util.RendererUtil;
import java.io.IOException;
import java.util.Iterator;
import java.util.ListIterator;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class HeadRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("head", component);

        //Theme
        String theme = null;
        String themeParamValue = context.getExternalContext().getInitParameter("blazefaces.THEME");
        String featureDetection = context.getExternalContext().getInitParameter("blazefaces.FEATURE_DETECTION");

        if (themeParamValue != null) {
            ELContext elContext = context.getELContext();
            ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
            ValueExpression ve = expressionFactory.createValueExpression(elContext, themeParamValue, String.class);

            theme = (String) ve.getValue(elContext);
        }

        if (theme == null || theme.equalsIgnoreCase("blazing")) {
            RendererUtil.encodeCss(context, "blazefaces", "themes/blazing/theme.css");
        } else if (!theme.equalsIgnoreCase("none")) {
            RendererUtil.encodeCss(context, "blazefaces-" + theme, "theme.css");
        }

        // Normal resources =D
        encodeComponentResources(context, "head", null);
        
        // IE specific resources...
        if(AgentUtil.isIE(context)){
            // Conditional scripts are used for IE
            encodeComponentResources(context, "head_ie", "IE");
            
            for(int i = 6; i < 10; i++){
                encodeComponentResources(context, "head_ie" + i, "IE " + i);
                encodeComponentResources(context, "head_lt_ie" + i, "lt IE " + i);
                encodeComponentResources(context, "head_lte_ie" + i, "lte IE " + i);
                encodeComponentResources(context, "head_gt_ie" + i, "gt IE " + i);
                encodeComponentResources(context, "head_gte_ie" + i, "gte IE " + i);
            }
        }
        
        if(!"false".equalsIgnoreCase(featureDetection)){
            // Feature detection script will only be rendered once
            FeatureDetectionUtil.encodeFeatures(context);
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //no-op
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("head");
    }
    
    private void encodeComponentResources(FacesContext context, String target, String condition) throws IOException{
        ResponseWriter writer = context.getResponseWriter();
        UIViewRoot viewRoot = context.getViewRoot();
        Iterator<UIComponent> iter = (viewRoot.getComponentResources(context, target)).listIterator();
        boolean hasResources = iter.hasNext();
        
        if(hasResources && condition != null){
            writer.write("<!--[if ");
            writer.write(condition);
            writer.write("]>");
        }
        
        while (iter.hasNext()) {
            iter.next().encodeAll(context);
        }
        
        if(hasResources && condition != null){
            writer.write("<![endif]-->");
        }
    }
}
