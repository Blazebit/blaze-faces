<#macro tagDescription description>
        <#if description.description??>
        <description>
                <![CDATA[
                        ${description.description}
                ]]>
        </description>
        </#if>
        <#if description.displayName??>
        <display-name>
                <![CDATA[
                        ${description.displayName}
                ]]>
        </display-name>
        </#if>
        <#if description.icon??>
        <icon>
            <#if description.icon.smallIcon??>
            <small-icon>${description.icon.smallIcon}</small-icon>
            </#if>
            <#if description.icon.largeIcon??>
            <large-icon>${description.icon.largeIcon}</large-icon>
            </#if>
        </icon>
        </#if>
</#macro>
<#macro attributeDescription description>
        <#if description.description??>
            <description>
                    <![CDATA[
                            ${description.description}
                    ]]>
            </description>
            </#if>
            <#if description.displayName??>
            <displayName>
                    <![CDATA[
                            ${description.displayName}
                    ]]>
            </displayName>
            </#if>
            <#if description.icon??>
            <icon>
                <#if description.icon.smallIcon??>
                <small-icon>${description.icon.smallIcon}</small-icon>
                </#if>
                <#if description.icon.largeIcon??>
                <large-icon>${description.icon.largeIcon}</large-icon>
                </#if>
            </icon>
        </#if>
</#macro>
<?xml version="1.0" encoding="UTF-8"?>
<facelet-taglib xmlns="http://java.sun.com/xml/ns/javaee"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facelettaglibrary_2_0.xsd"
		version="2.0">	
<!-- 
<facelet-taglib xmlns="http://xmlns.jcp.org/xml/ns/javaee"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-facelettaglibary_2_2.xsd"
		version="2.2">
-->
		
    <namespace>${namespace.namespace}</namespace>
	
<#list namespace.functions as function>
    <function>
	<#if function.description??>
    	<@tagDescription description=function.description/>
        </#if>
        <function-name>${function.name}</function-name>
        <function-class>${function.clazz}</function-class>
        <function-signature>${function.signature}</function-signature>
    </function>
    
</#list>
<#list namespace.tags as tag>
<#if (tag.component?? && !tag.component.abstract) || (tag.behavior?? && !tag.behavior.abstract)>
    <tag>
	<#if tag.description??>
    	<@tagDescription description=tag.description/>
        </#if>
        <tag-name>${tag.name}</tag-name>
        <#if tag.handler??>
        <handler-class>${tag.handler}</handler-class>
        </#if>
        <#if tag.behavior??>
        <#if tag.behavior.handler??>
        <behavior>
            <behavior-id>${tag.behavior.id}</behavior-id>
            <handler-class>${tag.behavior.handler}</handler-class>
        </behavior>
        </#if>
        </#if>
        <#if tag.component??>
        <component>
            <component-type>${tag.component.type}</component-type>
            <renderer-type>${tag.component.rendererType}</renderer-type>
            <#if tag.component.handler??>
            <handler-class>${tag.component.handler}</handler-class>
            </#if>
        </component>
        </#if>
        <#list tag.attributes as attribute>
        <attribute>
            <#if attribute.description??>
            <@attributeDescription attribute.description/>
            </#if>
            <name>${attribute.name}</name>
            <required><#if attribute.required>true<#else>false</#if></required>
            <type>${attribute.objectType}</type>
        </attribute>
        </#list>
    </tag>
    
</#if>
</#list>
</facelet-taglib>
