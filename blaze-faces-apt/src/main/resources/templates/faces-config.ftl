<?xml version="1.0" encoding="UTF-8"?>
<faces-config xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd"
	version="2.0">

<!--
<faces-config xmlns="http://xmlns.jcp.org/xml/ns/javaee"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-facesconfig_2_2.xsd"
              version="2.2">
-->
	
    <name>${namespace.name}</name>
    
    <#if namespace.application??>
    <application>
        <#if namespace.application.resourceHandler??>
        <resource-handler>${namespace.application.resourceHandler}</resource-handler>
        </#if>
        <#list namespace.application.systemEventListeners as systemEventListener>
    	<system-event-listener>
            <system-event-listener-class>${systemEventListener.systemEventListenerClass}</system-event-listener-class>
            <system-event-class>${systemEventListener.systemEventClass}</system-event-class>
            <source-class>${systemEventListener.sourceClass}</source-class>
        </system-event-listener>
        </#list>
    </application>
    
    </#if>
    <#if namespace.phaseListeners?has_content>
    <lifecycle>
    <#list namespace.phaseListeners as phaseListener>
    	<phase-listener>${phaseListener.phaseListenerClass}</phase-listener>
    </#list>
    </lifecycle>
    
    </#if>
    <#if namespace.factory??>
    <factory>
        <#if namespace.factory.partialViewContextFactory??>
        <partial-view-context-factory>${namespace.factory.partialViewContextFactory}</partial-view-context-factory>
        </#if>
        <#if namespace.factory.exceptionHandlerFactory??>
        <exception-handler-factory>${namespace.factory.exceptionHandlerFactory}</exception-handler-factory>
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
    <#list namespace.renderers as renderer>
        <renderer>
            <component-family>${renderer.componentFamily}</component-family>
            <renderer-type>${renderer.type}</renderer-type>
            <renderer-class>${renderer.clazz}</renderer-class>
        </renderer>
    </#list>

    <#list namespace.clientBehaviorRenderers as clientBehaviorRenderer>
        <client-behavior-renderer>
            <client-behavior-renderer-type>${clientBehaviorRenderer.type}</client-behavior-renderer-type>
            <client-behavior-renderer-class>${clientBehaviorRenderer.clazz}</client-behavior-renderer-class>
        </client-behavior-renderer>
    </#list>
    
    </render-kit>
</faces-config>