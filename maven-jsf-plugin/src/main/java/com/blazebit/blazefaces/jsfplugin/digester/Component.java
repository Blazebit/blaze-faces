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
package com.blazebit.blazefaces.jsfplugin.digester;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Component {

    private String tag;
    private String componentClass;
    private String componentHandlerClass;
    private String parent;
    private String componentType;
    private String componentFamily;
    private String rendererType;
    private String rendererClass;
    private List<Attribute> attributes;
    private List<Resource> resources;
    private List<Interface> interfaces;
    private String description;

    public Component() {
        attributes = new ArrayList<Attribute>();
        resources = new ArrayList<Resource>();
        interfaces = new ArrayList<Interface>();
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void addInterface(Interface _interface) {
        interfaces.add(_interface);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentFamily() {
        return componentFamily;
    }

    public void setComponentFamily(String componentFamily) {
        this.componentFamily = componentFamily;
    }

    public String getRendererType() {
        return rendererType;
    }

    public void setRendererType(String rendererType) {
        this.rendererType = rendererType;
    }

    public String getRendererClass() {
        return rendererClass;
    }

    public void setRendererClass(String rendererClass) {
        this.rendererClass = rendererClass;
    }

    public String getComponentClass() {
        return componentClass;
    }

    public void setComponentClass(String componentClass) {
        this.componentClass = componentClass;
    }

    public String getComponentHandlerClass() {
        return componentHandlerClass;
    }

    public void setComponentHandlerClass(String componentHandlerClass) {
        this.componentHandlerClass = componentHandlerClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAjaxComponent() {
        for (Iterator<Interface> iterator = getInterfaces().iterator(); iterator.hasNext();) {
            Interface _interface = iterator.next();

            if (_interface.getName().equals("com.blazebit.blazefaces.component.AjaxComponent")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gives the short name of the component e.g.
     * net.sf.yui4jsf.component.Slider will return Slider
     */
    public String getComponentShortName() {
        String[] list = componentClass.split("\\.");
        return list[list.length - 1];
    }

    /**
     * Gives the short name of the parent e.g.
     * javax.faces.component.UIComponentBase will return UIComponentBase
     */
    public String getParentShortName() {
        String[] list = parent.split("\\.");
        return list[list.length - 1];
    }

    /**
     * Returns the parent package folder e.g.
     * net.sf.yui4jsf.component.tabview.Tab will return "tabview"
     */
    public String getParentPackagePath() {
        String[] list = getComponentClass().split("\\.");
        return list[list.length - 2];
    }

    /**
     * Returns the parent package folder e.g.
     * net.sf.yui4jsf.component.tabview.Tab will return
     * "net.sf.yui4jsf.component.tabview"
     */
    public String getPackage() {
        int pos = getComponentClass().lastIndexOf(".");

        if (pos == -1) {
            return getComponentClass();
        }
        return getComponentClass().substring(0, pos);
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<Interface> interfaces) {
        this.interfaces = interfaces;
    }

    public boolean isWidget() {
        for (Iterator<Interface> iterator = getInterfaces().iterator(); iterator.hasNext();) {
            Interface _interface = iterator.next();
            if (_interface.getName().equalsIgnoreCase("com.blazebit.blazefaces.component.Widget")) {
                return true;
            }
        }

        return false;
    }
}