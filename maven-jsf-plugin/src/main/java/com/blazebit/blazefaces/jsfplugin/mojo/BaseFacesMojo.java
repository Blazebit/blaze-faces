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
package com.blazebit.blazefaces.jsfplugin.mojo;

import com.blazebit.blazefaces.jsfplugin.digester.Attribute;
import com.blazebit.blazefaces.jsfplugin.digester.Component;
import com.blazebit.blazefaces.jsfplugin.digester.Interface;
import com.blazebit.blazefaces.jsfplugin.digester.Resource;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.digester.Digester;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Base class for all the jsf mojos, parses the component config files and
 * generates output directories
 * 
 */
public abstract class BaseFacesMojo extends AbstractMojo {

	private static final String license;

	static {
		StringBuilder sb = new StringBuilder();
		sb.append("/*\n * Generated, Do Not Modify\n */\n")
				.append("/*\n")
				.append(" * Copyright 2011-2012 Blazebit\n")
				.append(" *\n")
				.append(" * Licensed under the Apache License, Version 2.0 (the \"License\");\n")
				.append(" * you may not use this file except in compliance with the License.\n")
				.append(" * You may obtain a copy of the License at\n")
				.append(" *\n")
				.append(" * http://www.apache.org/licenses/LICENSE-2.0\n")
				.append(" *\n")
				.append(" * Unless required by applicable law or agreed to in writing, software\n")
				.append(" * distributed under the License is distributed on an \"AS IS\" BASIS,\n")
				.append(" * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n")
				.append(" * See the License for the specific language governing permissions and\n")
				.append(" * limitations under the License.\n").append(" */\n");
		license = sb.toString();
	}

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;
	/**
	 * @parameter @required
	 */
	protected String componentConfigsDir;
	/**
	 * @parameter
	 */
	protected String templatesDir;
	/**
	 * @parameter @required
	 */
	protected String jsfVersion;
	protected String[] uicomponentAttributes = new String[] { "id", "rendered",
			"binding" };
	protected String[] specialAttributes = new String[] { "value", "converter",
			"validator", "valueChangeListener", "immediate", "required",
			"action", "actionListener" };

	protected File[] getResources() {
		return new File(project.getBasedir() + File.separator
				+ componentConfigsDir).listFiles();
	}

	protected Digester getDigester() {
        Digester digester = new Digester();
        digester.setValidating(false);
        
        digester.addObjectCreate("component", Component.class);
        digester.addBeanPropertySetter("component/tag", "tag");
        digester.addBeanPropertySetter("component/componentClass", "componentClass");
        digester.addBeanPropertySetter("component/componentHandlerClass", "componentHandlerClass");
        digester.addBeanPropertySetter("component/componentType", "componentType");
        digester.addBeanPropertySetter("component/componentFamily", "componentFamily");
        digester.addBeanPropertySetter("component/rendererType", "rendererType");
        digester.addBeanPropertySetter("component/rendererClass", "rendererClass");
        digester.addBeanPropertySetter("component/parent", "parent");
        digester.addBeanPropertySetter("component/description", "description");

        digester.addObjectCreate("component/attributes/attribute", Attribute.class);
        digester.addBeanPropertySetter("component/attributes/attribute/name", "name");
        digester.addBeanPropertySetter("component/attributes/attribute/required", "required");
        digester.addBeanPropertySetter("component/attributes/attribute/type", "type");
        digester.addBeanPropertySetter("component/attributes/attribute/defaultValue", "defaultValue");
        digester.addBeanPropertySetter("component/attributes/attribute/ignoreInComponent", "ignoreInComponent");
        digester.addBeanPropertySetter("component/attributes/attribute/method-signature", "methodSignature");
        digester.addBeanPropertySetter("component/attributes/attribute/literal", "literal");
        digester.addBeanPropertySetter("component/attributes/attribute/description", "description");
        digester.addSetNext("component/attributes/attribute", "addAttribute");

        digester.addObjectCreate("component/resources/resource", Resource.class);
        digester.addBeanPropertySetter("component/resources/resource/name", "name");
        digester.addBeanPropertySetter("component/resources/resource/target", "target");
        digester.addSetNext("component/resources/resource", "addResource");

        digester.addObjectCreate("component/interfaces/interface", Interface.class);
        digester.addBeanPropertySetter("component/interfaces/interface/name", "name");
        digester.addSetNext("component/interfaces/interface", "addInterface");

        return digester;
    }

	protected List<Component> getComponents() {
		File[] resources = getResources();
		Digester digester = getDigester();
		List<Component> components = new ArrayList<Component>();

		for (int i = 0; i < resources.length; i++) {
			try {
				File resource = resources[i];

				if (resource.getName().endsWith(".xml")) {
					components.add((Component) digester.parse(resources[i]));
				}

			} catch (Exception e) {
				e.printStackTrace(System.err);
				getLog().info(e.getMessage());
				getLog().info("Error in generation");
				return null;
			}
		}

		return components;
	}

	protected String getCreateOutputDirectory() {
		String outputPath = project.getBuild().getDirectory() + File.separator
				+ "generated-sources" + File.separator + "maven-jsf-plugin"
				+ File.separator;

		File componentsDirectory = new File(outputPath);

		if (!componentsDirectory.exists()) {
			componentsDirectory.mkdirs();
		}

		return outputPath;
	}

	protected String createPackageDirectory(String outputPath,
			Component component) {
		String packagePath = outputPath + File.separator;
		String[] packageFolders = component.getPackage().split("\\.");

		for (String folder : packageFolders) {
			packagePath = packagePath + File.separator + folder;
		}

		File packageDirectory = new File(packagePath);

		if (!packageDirectory.exists()) {
			packageDirectory.mkdirs();
		}

		return packagePath;
	}

	protected String getLicense() {
		return license;
	}

	protected void writeLicense(BufferedWriter writer) throws IOException {
		writer.write(getLicense());
	}

	protected void writeFacesContextGetter(BufferedWriter writer)
			throws IOException {
		writer.write("\n\tprotected FacesContext getFacesContext() {\n\t\treturn FacesContext.getCurrentInstance();\n\t}\n");
	}

	protected void writeResourceHolderGetter(BufferedWriter writer)
			throws IOException {
		writer.write("\n\tprotected ResourceHolder getResourceHolder() {\n");
		writer.write("\t\tFacesContext facesContext = getFacesContext();\n");
		writer.write("\t\tif(facesContext == null)\n");
		writer.write("\t\t\treturn null;\n\n");
		writer.write("\t\tValueExpression ve = facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), \"#{blazeFacesResourceHolder}\", ResourceHolder.class);\n");
		writer.write("\n\t\treturn (ResourceHolder) ve.getValue(facesContext.getELContext());");
		writer.write("\n\t}\n");
	}

	protected boolean isMethodExpression(Attribute attribute) {
		String type = attribute.getType();

		if (type.equals("javax.faces.validator.Validator")
				|| type.equals("javax.faces.event.ValueChangeListener")
				|| type.equals("javax.el.MethodExpression")
				|| type.equals("javax.faces.event.ActionListener")) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean isJSF2() {
		// Only support JSF2 for now
		return true; // this.jsfVersion.equalsIgnoreCase("2");
	}
}