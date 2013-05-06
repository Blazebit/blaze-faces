<?xml version="1.0" encoding="utf-8"?>
<faces-config version="2.0" xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2.0.xsd">
    <name>${namespace.name}</name>
    
    <#if namespace.application??>
    <application>
        <#if namespace.application.resourceHandler??>
        <resource-handler>${namespace.application.resourceHandler}</resource-handler>
        </#if>
        <#list namespace.application.systemEventHandlers as systemEventHandler>
    	<system-event-listener>
            <source-class>${systemEventHandler.sourceClass}</source-class>
            <system-event-class>${systemEventHandler.systemEventClass}</system-event-class>
            <system-event-listener-class>${systemEventHandler.systemEventListenerClass}</system-event-listener-class>
        </system-event-listener>
        </#list>
    </application>
    
    </#if>
    <#if namespace.facotry??>
    <factory>
        <#if namespace.facotry.partialViewContextFactory??>
        <partial-view-context-factory>${namespace.facotry.partialViewContextFactory}</partial-view-context-factory>
        </#if>
    </factory>
    
    </#if>
    <#list namespace.behaviors as behavior>
    <#if !behavior.abstract>
    <behavior>
        <behavior-id>${behavior.id}</behavior-id>
        <behavior-class>${behavior.clazz}</behavior-class>
    </behavior>
    
    </#if>
    </#list>
    <#list namespace.components as component>
    <#if !component.abstract>
    <component>
        <component-type>${component.type}</component-type>
        <component-class>${component.clazz}</component-class>
    </component>
    
    </#if>
    </#list>
    <render-kit>
    <#list namespace.clientBehaviorRenderers as clientBehaviorRenderer>
        <client-behavior-renderer>
            <client-behavior-renderer-type>${clientBehaviorRenderer.type}</client-behavior-renderer-type>
            <client-behavior-renderer-class>${clientBehaviorRenderer.clazz}</client-behavior-renderer-class>
        </client-behavior-renderer>
    </#list>

    <#list namespace.renderers as renderer>
        <renderer>
            <component-family>${renderer.componentFamily}</component-family>
            <renderer-type>${renderer.type}</renderer-type>
            <renderer-class>${renderer.clazz}</renderer-class>
        </renderer>
    </#list>

    </render-kit>
</faces-config>