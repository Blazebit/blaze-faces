/*
 * Copyright 2011-2012 Blazebit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.blazefaces.mobile.component.page;

import java.io.IOException;
import java.util.ListIterator;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import com.blazebit.blazefaces.mobile.util.Constants;
import com.blazebit.blazefaces.renderkit.CoreRenderer;

public class PageRenderer extends CoreRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {        
        ResponseWriter writer = context.getResponseWriter();
        Page page = (Page) component;
        UIComponent meta = page.getFacet("meta");
        UIComponent config = page.getFacet("config");
        UIComponent preinit = page.getFacet("preinit");
        UIComponent postinit = page.getFacet("postinit");
        
        if(page.isMini()) {
            context.getAttributes().put(Constants.MINI_FORMS, true);
        }
        
        //Theme
        String theme = context.getExternalContext().getInitParameter(Constants.THEME_PARAM);

        writer.write("<!DOCTYPE html>\n");
        writer.startElement("html", page);
        if(page.getManifest() != null) {
            writer.writeAttribute("manifest", page.getManifest(), "manifest");
        }
        
        writer.startElement("head", page);
        
        //viewport meta
        writer.startElement("meta", page);
        writer.writeAttribute("name", "viewport", null);
        writer.writeAttribute("content", page.getViewport(), null);
        writer.endElement("meta");
        
        //user defined meta
        if(meta != null) {
            meta.encodeAll(context);
        }

        writer.startElement("title", page);
        writer.write(page.getTitle());
        writer.endElement("title");
        
        if(preinit != null) {
            preinit.encodeAll(context);
        }

        if(theme != null && theme.equals("none")) {
            renderResource(context, "structure.css", "javax.faces.resource.Stylesheet", "blazefaces-mobile");
        }
        else {
            renderResource(context, "mobile.css", "javax.faces.resource.Stylesheet", "blazefaces-mobile");
        }
        
        renderResource(context, "jquery/jquery.js", "javax.faces.resource.Script", "blazefaces");

        //config options
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        
        writer.write("$(document).bind('mobileinit', function(){");
        writer.write("$.mobile.ajaxEnabled = false;");
        writer.write("$.mobile.linkBindingEnabled = false;");
        writer.write("$.mobile.hashListeningEnabled = false;");
        writer.write("$.mobile.pushStateEnabled = false;");
        
        if(page.getLoadingMessage() != null) writer.write("$.mobile.loadingMessage = '" + page.getLoadingMessage() + "';");
        if(page.getDefaultPageTransition() != null) writer.write("$.mobile.defaultPageTransition = '" + page.getDefaultPageTransition() + "';");
        if(page.getDefaultDialogTransition() != null) writer.write("$.mobile.defaultDialogTransition = '" + page.getDefaultDialogTransition() + "';");
        
        if(config != null) {
            config.encodeAll(context);
        }
        
        writer.write("});");
        
        writer.endElement("script");

        renderResource(context, "mobile.js", "javax.faces.resource.Script", "blazefaces-mobile");
        renderResource(context, "blazefaces.js", "javax.faces.resource.Script", "blazefaces");
        renderResource(context, "blazefaces-mobile.js", "javax.faces.resource.Script", "blazefaces-mobile");

        if(postinit != null) {
            postinit.encodeAll(context);
        }

        //Registered resources
        UIViewRoot viewRoot = context.getViewRoot();
        ListIterator<UIComponent> iter = (viewRoot.getComponentResources(context, "head")).listIterator();
        while (iter.hasNext()) {
            writer.write("\n");
            UIComponent resource = (UIComponent) iter.next();
            resource.encodeAll(context);
        }

        writer.endElement("head");

        writer.startElement("body", page);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("body");
        writer.endElement("html");

    }

    protected void renderResource(FacesContext context, String resourceName, String renderer, String library) throws IOException {
        UIComponent resource = context.getApplication().createComponent("javax.faces.Output");
        resource.setRendererType(renderer);

        Map<String, Object> attrs = resource.getAttributes();
        attrs.put("name", resourceName);
        attrs.put("library", library);
        attrs.put("target", "head");

        resource.encodeAll(context);
    }
}
