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

import com.blazebit.blazefaces.jsfplugin.digester.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.digester.Digester;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal generate-faces-config
 */
public class FacesConfigMojo extends BaseFacesMojo {

    /**
     * @parameter
     */
    protected String standardFacesConfig;
    /**
     * @parameter
     */
    protected String standardRenderersConfig;
    /**
     * @parameter
     */
    protected String renderKitId;
    /**
     * @parameter
     */
    protected String renderKitClass;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Generating faces-config.xml");

        try {
            writeFacesConfig(getAllComponents());
            getLog().info("faces-config.xml generated successfully");
        } catch (Exception e) {
            getLog().info("Error occured in generating faces-config.xml");
            getLog().info(e.toString());
        }
    }

    private void writeFacesConfig(List<Component> components) {
        FileWriter fileWriter = null;
        BufferedWriter writer = null;
        String outputPath = project.getBuild().getOutputDirectory() + File.separator + "META-INF";
        String outputFile = "faces-config.xml";

        try {
            fileWriter = new FileWriter(outputPath + File.separator + outputFile);
            writer = new BufferedWriter(fileWriter);

            writeFacesConfigBegin(writer, components);
            writeStandardConfig(writer);
            writeComponents(writer, components);
            writeRenderers(writer, components);
            writeFacesConfigEnd(writer, components);
        } catch (Exception exception) {
            getLog().error(exception.getMessage());
        } finally {
            try{
                if(writer != null){
                    writer.close();
                }
            }catch(IOException ex){
                // Ignore
            }
            
            try{
                if(fileWriter != null){
                    fileWriter.close();
                }
            }catch(IOException ex){
                // Ignore
            }
        }
    }

    private void writeFacesConfigBegin(BufferedWriter writer, List<Component> components) throws IOException {
        String version = "2.0"; //isJSF2() ? "2.0" : "1.2";
        String xsdVersion = "2.0"; //isJSF2() ? "2_0" : "1_2";

        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.write("<faces-config version=\"" + version + "\" xmlns=\"http://java.sun.com/xml/ns/javaee\"\n");
        writer.write("\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        writer.write("\txsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_" + xsdVersion + ".xsd\">\n");
    }

    private void writeStandardConfig(BufferedWriter writer) throws IOException {
        try {
            File template = new File(project.getBasedir() + File.separator + standardFacesConfig);
            FileReader fileReader = new FileReader(template);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;

            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
            }
        } catch (FileNotFoundException fileNotFoundException) {
            return;
        }
    }

    private void writeComponents(BufferedWriter writer, List<Component> components) throws IOException {
        for (Iterator<Component> iterator = components.iterator(); iterator.hasNext();) {
            Component component = iterator.next();

            writer.write("\t<component>\n");
            writer.write("\t\t<component-type>" + component.getComponentType() + "</component-type>\n");
            writer.write("\t\t<component-class>" + component.getComponentClass() + "</component-class>\n");
            writer.write("\t</component>\n");
            writer.write("\n");
        }
    }

    private void writeRenderers(BufferedWriter writer, List<Component> components) throws IOException {
        writer.write("\t<render-kit>\n");

        if (renderKitId != null) {
            writer.write("\t\t<render-kit-id>" + renderKitId + "</render-kit-id>\n");
            writer.write("\t\t<render-kit-class>" + renderKitClass + "</render-kit-class>\n");
        }

        //Standard Renderers
        try {
            File template = new File(project.getBasedir() + File.separator + standardRenderersConfig);
            FileReader fileReader = new FileReader(template);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;

            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
            }
        } catch (FileNotFoundException fileNotFoundException) {
            return;
        }

        for (Iterator<Component> iterator = components.iterator(); iterator.hasNext();) {
            Component component = iterator.next();
            
            if (component.getRendererType() == null) {
                continue;
            }

            writer.write("\t\t<renderer>\n");
            writer.write("\t\t\t<component-family>" + component.getComponentFamily() + "</component-family>\n");
            writer.write("\t\t\t<renderer-type>" + component.getRendererType() + "</renderer-type>\n");
            writer.write("\t\t\t<renderer-class>" + component.getRendererClass() + "</renderer-class>\n");
            writer.write("\t\t</renderer>\n");
        }

        writer.write("\t</render-kit>\n\n");
    }

    private void writeFacesConfigEnd(BufferedWriter writer, List<Component> components) throws IOException {
        writer.write("</faces-config>");
    }

    protected List<Component> getAllComponents() {
        String[] metaFolders = componentConfigsDir.split(",");
        File[] resources = new File[0];
        
        for (String path : metaFolders) {
            File[] files = new File(project.getBasedir() + File.separator + path).listFiles();
            resources = concat(resources, files);
        }

        Digester digester = getDigester();
        List<Component> components = new ArrayList<Component>();

        for (int i = 0; i < resources.length; i++) {
            try {
                File resource = resources[i];
                
                if (resource.getName().endsWith(".xml")) {
                    components.add((Component) digester.parse(resources[i]));
                }

            } catch (Exception e) {
                getLog().info(e.getMessage());
                getLog().info("Error in generation");
                return null;
            }
        }

        return components;
    }

    public File[] concat(File[]... arrays) {
        int destSize = 0;
        
        for (int i = 0; i < arrays.length; i++) {
            destSize += arrays[i].length;
        }
        
        File[] dest = new File[destSize];
        int lastIndex = 0;
        
        for (int i = 0; i < arrays.length; i++) {
            File[] array = arrays[i];
            System.arraycopy(array, 0, dest, lastIndex, array.length);
            lastIndex += array.length;
        }

        return dest;
    }
}