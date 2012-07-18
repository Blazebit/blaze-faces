/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.component.include;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

/**
 *
 * @author Christian Beikov
 */
public class IncludeHandler extends ComponentHandler {

    public IncludeHandler(ComponentConfig config) {
        super(config);
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent component, UIComponent parent) {
        Include di = (Include) component;
        di.setFaceletContext(ctx);
        TagAttribute[] vars = tag.getAttributes().getAll();
        
        for (TagAttribute var : vars) {
            String name = var.getQName();
            String value = var.getValue();
            final ValueExpression valueExpression =
                    ctx.getExpressionFactory().createValueExpression(
                    ctx.getFacesContext().getELContext(), value, Object.class);
            di.addValueExpression(name, valueExpression);
        }
    }

    public boolean apply(FaceletContext ctx, UIComponent parent, String name)
            throws IOException, FacesException, ELException {
        if (name == null) {
            this.nextHandler.apply(ctx, parent);
            return true;
        }
        return false;
    }
}
