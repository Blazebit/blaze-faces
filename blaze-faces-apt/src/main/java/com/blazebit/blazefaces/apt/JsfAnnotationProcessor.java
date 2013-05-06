package com.blazebit.blazefaces.apt;

import com.blazebit.blazefaces.apt.model.Attribute;
import com.blazebit.blazefaces.apt.model.Behavior;
import com.blazebit.blazefaces.apt.model.BehaviorRenderer;
import com.blazebit.blazefaces.apt.model.Component;
import com.blazebit.blazefaces.apt.model.Description;
import com.blazebit.blazefaces.apt.model.Function;
import com.blazebit.blazefaces.apt.model.Icon;
import com.blazebit.blazefaces.apt.model.Namespace;
import com.blazebit.blazefaces.apt.model.Renderer;
import com.blazebit.blazefaces.apt.model.Tag;
import com.blazebit.blazefaces.apt.model.TagHolder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Stack;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Creates taglib.xml, faces-config.xml and base classes for components and
 * behaviors.
 *
 * @author Christian Beikov
 * @since 0.1.3
 */
@SupportedAnnotationTypes("com.blazebit.blazefaces.apt.*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JsfAnnotationProcessor extends AbstractProcessor {
    
    public static final String BEHAVIOR_TEMPLATE = "templates/behavior.ftl";
    public static final String COMPONENT_TEMPLATE = "templates/component.ftl";
    public static final String TAGLIB_TEMPLATE = "templates/taglib.ftl";
    public static final String FACES_CONFIG_TEMPLATE = "templates/faces-config.ftl";
    private Configuration templateConfiguration;
    private Map<String, Namespace> namespaces = new HashMap<String, Namespace>();
    
    public JsfAnnotationProcessor() {
        this(getDefaultConfiguration());
    }
    
    public JsfAnnotationProcessor(Configuration configuration) {
        this.templateConfiguration = configuration;
    }
    
    public static Configuration getDefaultConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(new ClassPathTemplateLoader());
        configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        return configuration;
    }
    
    protected List<String> getImports(Template template) {
        if (BEHAVIOR_TEMPLATE.equals(template.getName())) {
            return Arrays.asList("java.util.HashMap",
                    "java.util.Map",
                    "java.util.Set",
                    "javax.el.ELContext",
                    "javax.el.ELException",
                    "javax.el.ValueExpression",
                    "javax.faces.FacesException",
                    "javax.faces.context.FacesContext",
                    "javax.faces.component.UIComponentBase");
        } else if (COMPONENT_TEMPLATE.equals(template.getName())) {
            return Arrays.asList("java.util.List",
                    "java.util.ArrayList",
                    "javax.el.ValueExpression");
        }
        
        return Collections.emptyList();
    }
    
    protected Map.Entry<String, Namespace> getNamespaceEntry(String packageName) {
        for (Map.Entry<String, Namespace> entry : namespaces.entrySet()) {
            if (packageName.startsWith(entry.getKey())) {
                return entry;
            }
        }
        
        return null;
    }
    
    protected Namespace getNamespace(String packageName) {
        for (Map.Entry<String, Namespace> entry : namespaces.entrySet()) {
            if (packageName.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    protected Behavior getBehavior(String behaviorClass) {
        for (Behavior behavior : getNamespace(behaviorClass).getBehaviors()) {
            if (behavior.getClazz().equals(behaviorClass)) {
                return behavior;
            }
        }
        
        return null;
    }
    
    protected Namespace processNamespace(PackageElement element) {
        Namespace namespace = new Namespace();
        AnnotationMirror annotation = AnnotationProcessingUtil
                .findAnnotationMirror(element, JsfNamespace.class);
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
            String memberName = entry.getKey().getSimpleName().toString();
            
            if ("name".equals(memberName)) {
                namespace.setName(entry.getValue().getValue().toString());
            } else if ("uri".equals(memberName)) {
                namespace.setNamespace(entry.getValue().getValue()
                        .toString());
            } else if ("shortName".equals(memberName)) {
                namespace.setShortName(entry.getValue().getValue()
                        .toString());
            }
        }
        
        if (namespace.getApplication().getResourceHandler() == null && namespace.getApplication().getSystemEventHandlers().isEmpty()) {
            namespace.setApplication(null);
        }
        
        return namespace;
    }
    
    protected Renderer processRenderer(TypeElement typeElement, String namespacePackage, Namespace namespace) {
        Renderer renderer = new Renderer();
        AnnotationMirror annotation = AnnotationProcessingUtil
                .findAnnotationMirror(typeElement, JsfRenderer.class);
        
        renderer.setClazz(typeElement.getQualifiedName().toString());
        renderer.setType(namespacePackage + ".renderer." + typeElement.getSimpleName().toString());
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
            String memberName = entry.getKey().getSimpleName().toString();
            Object defaultValue = entry.getKey().getDefaultValue()
                    .getValue();
            Object value = entry.getValue().getValue();
            boolean isDefault = defaultValue != null
                    && defaultValue.equals(value);
            
            if ("type".equals(memberName) && !isDefault) {
                renderer.setType(entry.getValue().getValue().toString());
            }
        }
        
        return renderer;
    }
    
    protected BehaviorRenderer processBehaviorRenderer(TypeElement typeElement, String namespacePackage, Namespace namespace) {
        BehaviorRenderer clientBehaviorRenderer = new BehaviorRenderer();
        AnnotationMirror annotation = AnnotationProcessingUtil
                .findAnnotationMirror(typeElement, JsfBehaviorRenderer.class);
        
        clientBehaviorRenderer.setClazz(typeElement.getQualifiedName()
                .toString());
        clientBehaviorRenderer.setType(namespacePackage + ".behavior.renderer." + typeElement.getSimpleName().toString());
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
            String memberName = entry.getKey().getSimpleName().toString();
            Object defaultValue = entry.getKey().getDefaultValue()
                    .getValue();
            Object value = entry.getValue().getValue();
            boolean isDefault = defaultValue != null
                    && defaultValue.equals(value);
            
            if ("type".equals(memberName) && !isDefault) {
                clientBehaviorRenderer.setType(entry.getValue().getValue()
                        .toString());
            }
        }
        
        return clientBehaviorRenderer;
    }
    
    @SuppressWarnings("unchecked")
	protected Behavior processBehavior(TypeElement typeElement, String namespacePackage, Namespace namespace) {
        Behavior behavior = new Behavior();
        AnnotationMirror annotation = AnnotationProcessingUtil
                .findAnnotationMirror(typeElement, JsfBehavior.class);
        
        behavior.getTag().setName(
                firstToLowerCase(typeElement.getSimpleName().toString()));
        behavior.setId(namespacePackage + ".behavior." + typeElement.getSimpleName().toString());
        behavior.setClazz(typeElement.getQualifiedName().toString());
        behavior.setAbstract(typeElement.getModifiers().contains(
                Modifier.ABSTRACT));
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
            String memberName = entry.getKey().getSimpleName().toString();
            Object defaultValue = entry.getKey().getDefaultValue()
                    .getValue();
            Object value = entry.getValue().getValue();
            boolean isDefault = defaultValue != null
                    && defaultValue.equals(value);
            
            if ("tag".equals(memberName) && !isDefault) {
                behavior.getTag().setName(
                        entry.getValue().getValue().toString());
            } else if ("id".equals(memberName) && !isDefault) {
                behavior.setId(entry.getValue().getValue().toString());
            } else if ("hints".equals(memberName) && !isDefault) {
                behavior.setHints(getHints((List<AnnotationValue>) entry.getValue().getValue()));
            } else if ("description".equals(memberName) && !isDefault) {
                behavior.getTag().setDescription(
                        getDescription((AnnotationMirror) entry.getValue()
                        .getValue()));
            } else if ("attributes".equals(memberName)) {
                mergeAttributes(behavior.getTag().getAttributes(), parseAttributes((List<AnnotationValue>) entry.getValue().getValue()));
            } else if ("parent".equals(memberName)) {
                behavior.setParent(entry.getValue().getValue().toString());
            } else if ("handler".equals(memberName) && !isDefault) {
                behavior.setHandler(
                        entry.getValue().getValue().toString());
            } else if ("renderer".equals(memberName) && !isDefault) {
                behavior.setRenderer(entry.getValue().getValue().toString());
            }
        }
        
        if (!behavior.getHints().isEmpty()) {
            Set<String> imports = new HashSet<String>(behavior.getImports());
            imports.add("java.util.Collections");
            imports.add("java.util.EnumSet");
            imports.add("javax.faces.component.behavior.ClientBehaviorHint");
            behavior.setImports(new ArrayList<String>(imports));
        }
        
        return behavior;
    }
    
    @SuppressWarnings("unchecked")
	protected Component processComponent(TypeElement typeElement, String namespacePackage, Namespace namespace) {
        Component component = new Component();
        AnnotationMirror annotation = AnnotationProcessingUtil
                .findAnnotationMirror(typeElement, JsfComponent.class);
        
        component.getTag().setName(
                firstToLowerCase(typeElement.getSimpleName().toString()));
        component.setClazz(typeElement.getQualifiedName().toString());
        component.setAbstract(typeElement.getModifiers().contains(
                Modifier.ABSTRACT));
        component.setType(namespacePackage + ".component." + typeElement.getSimpleName().toString());
        component.setFamily(namespacePackage + ".component");
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
            String memberName = entry.getKey().getSimpleName().toString();
            Object defaultValue = entry.getKey().getDefaultValue()
                    .getValue();
            Object value = entry.getValue().getValue();
            boolean isDefault = defaultValue != null
                    && defaultValue.equals(value);
            
            if ("tag".equals(memberName) && !isDefault) {
                component.getTag().setName(
                        entry.getValue().getValue().toString());
            } else if ("type".equals(memberName) && !isDefault) {
                component.setType(entry.getValue().getValue().toString());
            } else if ("family".equals(memberName) && !isDefault) {
                component.setFamily(entry.getValue().getValue().toString());
            } else if ("description".equals(memberName) && !isDefault) {
                component.getTag().setDescription(
                        getDescription((AnnotationMirror) entry.getValue()
                        .getValue()));
            } else if ("attributes".equals(memberName)) {
                mergeAttributes(component.getTag().getAttributes(), parseAttributes((List<AnnotationValue>) entry.getValue().getValue()));
            } else if ("parent".equals(memberName)) {
                component.setParent(entry.getValue().getValue().toString());
            } else if ("handler".equals(memberName) && !isDefault) {
                component.setHandler(
                        entry.getValue().getValue().toString());
            } else if ("behavior".equals(memberName) && !isDefault) {
                component.getTag()
                        .setBehavior(
                        getBehavior(entry.getValue().getValue()
                        .toString()));
            } else if ("renderer".equals(memberName) && !isDefault) {
                component.setRenderer(entry.getValue().getValue()
                        .toString());
            }
        }
        
        if (component.getRenderer() != null) {
            for (Renderer renderer : namespace.getRenderers()) {
                if (renderer.getClazz().equals(component.getRenderer())) {
                    component.setRendererType(renderer.getType());
                    
                    if (renderer.getComponentFamily() != null) {
                        // There may be only a 1:1 mapping between renderers
                        // and components
                    }
                    
                    renderer.setComponentFamily(component.getFamily());
                    
                    break;
                }
            }
        }
        return component;
    }
    
    protected Function processFunction(ExecutableElement executableElement, String namespacePackage, Namespace namespace) {
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        String methodName = executableElement.getSimpleName().toString();
        
        Function function = new Function();
        StringBuilder signatureSb = new StringBuilder();
        AnnotationMirror annotation = AnnotationProcessingUtil
                .findAnnotationMirror(executableElement, JsfFunction.class);
        
        signatureSb.append(executableElement.getReturnType().toString())
                .append(" ");
        signatureSb.append(executableElement.getSimpleName().toString())
                .append("(");
        
        List<? extends VariableElement> parameters = executableElement
                .getParameters();
        
        if (!parameters.isEmpty()) {
            for (int i = 0; i < parameters.size() - 1; i++) {
                signatureSb.append(parameters.get(i).asType().toString())
                        .append(", ");
            }
            signatureSb.append(parameters.get(parameters.size() - 1)
                    .asType().toString());
        }
        
        signatureSb.append(')');
        
        function.setName(methodName);
        function.setClazz(typeElement.getQualifiedName().toString());
        function.setSignature(signatureSb.toString());
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
            String memberName = entry.getKey().getSimpleName().toString();
            Object defaultValue = entry.getKey().getDefaultValue()
                    .getValue();
            Object value = entry.getValue().getValue();
            boolean isDefault = defaultValue != null
                    && defaultValue.equals(value);
            
            if ("name".equals(memberName) && !isDefault) {
                function.setName(entry.getValue().getValue().toString());
            } else if ("description".equals(memberName) && !isDefault) {
                function.setDescription(getDescription((AnnotationMirror) entry
                        .getValue().getValue()));
            }
        }
        return function;
    }
    
    protected Attribute processAttribute(Tag tag, Element e) {
        Attribute attribute = new Attribute();
        AnnotationMirror annotation = AnnotationProcessingUtil
                .findAnnotationMirror(e, JsfAttribute.class);
        
        switch (e.getKind()) {
            case METHOD:
                ExecutableElement executableElement = (ExecutableElement) e;
                attribute.setName(guessFieldName(executableElement
                        .getSimpleName().toString()));
                attribute.setType(executableElement.getReturnType().toString());
                attribute.setObjectType(getObjectType(executableElement.getReturnType().toString()));
                break;
            case FIELD:
                VariableElement variableElement = (VariableElement) e;
                attribute.setName(variableElement.getSimpleName().toString());
                attribute.setType(variableElement.asType().toString());
                attribute.setObjectType(getObjectType(variableElement.asType().toString()));
                break;
            default:
                // Should not happen
                break;
        }
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
            String memberName = entry.getKey().getSimpleName().toString();
            Object defaultValue = entry.getKey().getDefaultValue()
                    .getValue();
            Object value = entry.getValue().getValue();
            boolean isDefault = defaultValue != null
                    && defaultValue.equals(value);
            
            if ("name".equals(memberName) && !isDefault) {
                attribute.setName(entry.getValue().getValue().toString());
            } else if ("required".equals(memberName) && !isDefault) {
                attribute.setRequired(Boolean.valueOf(entry.getValue()
                        .getValue().toString()));
            } else if ("type".equals(memberName) && !isDefault) {
                attribute.setType(entry.getValue().getValue().toString());
                attribute.setObjectType(getObjectType(entry.getValue().getValue().toString()));
            } else if ("description".equals(memberName) && !isDefault) {
                attribute
                        .setDescription(getDescription((AnnotationMirror) entry
                        .getValue().getValue()));
            } else if ("ignore".equals(memberName)) {
                attribute.setIgnore(Boolean.valueOf(entry.getValue()
                        .getValue().toString()));
            } else if ("defaultValue".equals(memberName) && !isDefault) {
                attribute.setDefaultValue(entry.getValue().getValue()
                        .toString());
            }
        }
        return attribute;
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return true;
        }
        
        TypeElement typeElement;
        String typeElementPackage;
        Map.Entry<String, Namespace> namespaceEntry;
        String namespacePackage;
        Namespace namespace;

        // Phase 1: Create model for found elements
        for (Element e : roundEnv.getElementsAnnotatedWith(JsfNamespace.class)) {
            PackageElement packageElement = (PackageElement) e;
            namespacePackage = packageElement.getQualifiedName().toString();
            namespaces.put(namespacePackage, processNamespace(packageElement));
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(JsfRenderer.class)) {
            typeElement = (TypeElement) e;
            typeElementPackage = typeElement.getQualifiedName().toString();
            namespaceEntry = getNamespaceEntry(typeElementPackage);
            namespacePackage = namespaceEntry.getKey();
            namespace = namespaceEntry.getValue();
            
            namespace.getRenderers()
                    .add(processRenderer(typeElement, namespacePackage, namespace));
        }
        for (Element e : roundEnv
                .getElementsAnnotatedWith(JsfBehaviorRenderer.class)) {
            typeElement = (TypeElement) e;
            typeElementPackage = typeElement.getQualifiedName().toString();
            namespaceEntry = getNamespaceEntry(typeElementPackage);
            namespacePackage = namespaceEntry.getKey();
            namespace = namespaceEntry.getValue();
            
            namespace.getClientBehaviorRenderers()
                    .add(processBehaviorRenderer(typeElement, namespacePackage, namespace));
        }
        // Do behaviors before components since they can reference them
        for (Element e : roundEnv.getElementsAnnotatedWith(JsfBehavior.class)) {
            typeElement = (TypeElement) e;
            typeElementPackage = typeElement.getQualifiedName().toString();
            namespaceEntry = getNamespaceEntry(typeElementPackage);
            namespacePackage = namespaceEntry.getKey();
            namespace = namespaceEntry.getValue();
            
            namespace.getBehaviors()
                    .add(processBehavior(typeElement, namespacePackage, namespace));
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(JsfComponent.class)) {
            typeElement = (TypeElement) e;
            typeElementPackage = typeElement.getQualifiedName().toString();
            namespaceEntry = getNamespaceEntry(typeElementPackage);
            namespacePackage = namespaceEntry.getKey();
            namespace = namespaceEntry.getValue();
            
            namespace.getComponents()
                    .add(processComponent(typeElement, namespacePackage, namespace));
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(JsfFunction.class)) {
            ExecutableElement executableElement = (ExecutableElement) e;
            typeElement = (TypeElement) executableElement.getEnclosingElement();
            typeElementPackage = typeElement.getQualifiedName().toString();
            namespaceEntry = getNamespaceEntry(typeElementPackage);
            namespacePackage = namespaceEntry.getKey();
            namespace = namespaceEntry.getValue();
            
            namespace.getFunctions()
                    .add(processFunction(executableElement, namespacePackage, namespace));
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(JsfAttribute.class)) {
            Tag tag = getTag(e);
            
            mergeAttributes(tag.getAttributes(), processAttribute(tag, e));
        }

        // Phase 2: Apply inherited attributes to tags
        for (Namespace ns : namespaces.values()) {
            Map<String, Behavior> behaviorMap = new HashMap<String, Behavior>();
            
            for (Behavior behavior : ns.getBehaviors()) {
                behaviorMap.put(behavior.getClazz(), behavior);
                ns.getTags().add(behavior.getTag());
            }
            for (Behavior behavior : ns.getBehaviors()) {
                inheritAttributes(behavior, behaviorMap);
            }
            
            Map<String, Component> componentMap = new HashMap<String, Component>();
            for (Component component : ns.getComponents()) {
                componentMap.put(component.getClazz(), component);
                ns.getTags().add(component.getTag());
            }
            for (Component component : ns.getComponents()) {
                inheritAttributes(component, componentMap);
            }
        }

        // Phase 3: Generate files
        return generateFiles();
    }
    
    protected boolean generateFiles() {
        try {
            Template taglibTemplate = templateConfiguration
                    .getTemplate(TAGLIB_TEMPLATE);
            Template facesConfigTemplate = templateConfiguration
                    .getTemplate(FACES_CONFIG_TEMPLATE);
            Template behaviorTemplate = templateConfiguration
                    .getTemplate(BEHAVIOR_TEMPLATE);
            Template componentTemplate = templateConfiguration
                    .getTemplate(COMPONENT_TEMPLATE);
            List<String> componentImports = getImports(componentTemplate);
            List<String> behaviorImports = getImports(behaviorTemplate);
            
            for (Namespace namespace : namespaces.values()) {
                String taglibName = "META-INF/" + namespace.getName() + "-"
                        + namespace.getShortName() + ".taglib.xml";
                String facesConfigName = "META-INF/faces-config.xml";
                FileObject taglibFileObject = processingEnv.getFiler()
                        .createResource(StandardLocation.SOURCE_OUTPUT, "",
                        taglibName);
                FileObject facesConfigFileObject = processingEnv.getFiler()
                        .createResource(StandardLocation.SOURCE_OUTPUT, "",
                        facesConfigName);
                Writer writer = null;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("namespace", namespace);
                
                try {
                    writer = taglibFileObject.openWriter();
                    taglibTemplate.process(map, writer);
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
                try {
                    writer = facesConfigFileObject.openWriter();
                    facesConfigTemplate.process(map, writer);
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
                
                for (Behavior behavior : namespace.getBehaviors()) {
                    if (!behavior.isAbstract()) {
                        String packageName = behavior.getPackage();
                        String simpleName = behavior.getClazz().substring(
                                packageName.length() + 1)
                                + "Base";
                        String qualifiedName = packageName + "." + simpleName;
                        JavaFileObject jfo = processingEnv.getFiler()
                                .createSourceFile(qualifiedName);
                        map = new HashMap<String, Object>();

                        // We are generating the base class so we have to override
                        // the name
                        behavior.setClazz(qualifiedName);
                        map.put("behavior", behavior);

                        // Apply the imports for the template
                        Set<String> imports = new HashSet<String>();
                        imports.addAll(behaviorImports);
                        imports.addAll(behavior.getImports());
                        behavior.setImports(new ArrayList<String>(imports));
                        
                        try {
                            writer = jfo.openWriter();
                            behaviorTemplate.process(map, writer);
                        } finally {
                            if (writer != null) {
                                writer.close();
                            }
                        }
                    }
                }
                for (Component component : namespace.getComponents()) {
                    if (!component.isAbstract()) {
                        String packageName = component.getPackage();
                        String simpleName = component.getClazz().substring(
                                packageName.length() + 1)
                                + "Base";
                        String qualifiedName = packageName + "." + simpleName;
                        JavaFileObject jfo = processingEnv.getFiler()
                                .createSourceFile(qualifiedName);
                        map = new HashMap<String, Object>();

                        // We are generating the base class so we have to override
                        // the name
                        component.setClazz(qualifiedName);
                        map.put("component", component);

                        // Apply the imports for the template
                        Set<String> imports = new HashSet<String>();
                        imports.addAll(componentImports);
                        imports.addAll(component.getImports());
                        component.setImports(new ArrayList<String>(imports));
                        
                        try {
                            writer = jfo.openWriter();
                            componentTemplate.process(map, writer);
                        } finally {
                            if (writer != null) {
                                writer.close();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return false;
        }
        
        return true;
    }
    
    protected Description getDescription(AnnotationMirror descriptionValue) {
        Description description = new Description();
        
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : descriptionValue
                .getElementValues().entrySet()) {
            String memberName = entry.getKey().getSimpleName().toString();
            Object defaultValue = entry.getKey().getDefaultValue().getValue();
            Object value = entry.getValue().getValue();
            boolean isDefault = defaultValue != null
                    && defaultValue.equals(value);
            
            if ("displayName".equals(memberName) && !isDefault) {
                description.setDisplayName(entry.getValue().getValue()
                        .toString());
            } else if ("description".equals(memberName) && !isDefault) {
                description.setDescription(entry.getValue().getValue()
                        .toString());
            } else if ("smallIcon".equals(memberName) && !isDefault) {
                description.getIcon().setSmallIcon(
                        entry.getValue().getValue().toString());
            } else if ("largeIcon".equals(memberName) && !isDefault) {
                description.getIcon().setLargeIcon(
                        entry.getValue().getValue().toString());
            }
        }
        
        if (description.getIcon().getSmallIcon() == null
                && description.getIcon().getLargeIcon() == null) {
            description.setIcon(null);
        }
        if (description.getDisplayName() == null
                && description.getDescription() == null
                && description.getIcon() == null) {
            description = null;
        }
        
        return description;
    }
    
    protected String guessFieldName(String methodName) {
        int offset = 0;
        
        if (methodName.startsWith("get") || methodName.startsWith("set")) {
            offset = 3;
        } else if (methodName.startsWith("is")) {
            offset = 2;
        }
        
        return new StringBuilder(methodName.length() - offset)
                .append(Character.toLowerCase(methodName.charAt(offset)))
                .append(methodName, offset + 1, methodName.length())
                .toString();
    }
    
    protected Tag getTag(Element e) {
        while (e.getEnclosingElement() != null && !(e.getEnclosingElement() instanceof PackageElement)) {
            e = e.getEnclosingElement();
        }
        
        TypeElement typeElement = (TypeElement) e;
        String clazzName = typeElement.getQualifiedName().toString();
        Namespace namespace = getNamespace(clazzName);
        
        for (Behavior behavior : namespace.getBehaviors()) {
            if (behavior.getClazz().equals(clazzName)) {
                return behavior.getTag();
            }
        }
        for (Component component : namespace.getComponents()) {
            if (component.getClazz().equals(clazzName)) {
                return component.getTag();
            }
        }
        
        return null;
    }
    
    protected Set<String> getParentAttributeNames(TagHolder tagHolder, Map<String, ? extends TagHolder> tagHolderMap) {
        Elements elementUtils = processingEnv.getElementUtils();
        Set<String> list = new LinkedHashSet<String>();
        Stack<TypeElement> stack = new Stack<TypeElement>();
        TypeElement traverseElement;
        
        stack.add(elementUtils.getTypeElement(tagHolder.getParent()));
        
        for (TypeMirror typeMirror : stack.peek().getInterfaces()) {
            stack.add((TypeElement) ((DeclaredType) typeMirror).asElement());
        }
        
        while (!stack.isEmpty()) {
            traverseElement = stack.pop();
            TagHolder tempTagHolder = tagHolderMap.get(traverseElement.getQualifiedName().toString());
            
            if (tempTagHolder != null) {
                for (Attribute attribute : tempTagHolder.getTag().getAttributes()) {
                    list.add(attribute.getName());
                }
            }
            
            for (Element e : traverseElement.getEnclosedElements()) {
                if (e.getKind() == ElementKind.METHOD && e.getModifiers().contains(Modifier.PUBLIC) && !e.getModifiers().contains(Modifier.ABSTRACT)) {
                    ExecutableElement method = (ExecutableElement) e;
                    list.add(guessFieldName(method.getSimpleName().toString()));
                }
            }
            
            if (traverseElement.getSuperclass() instanceof DeclaredType) {
                stack.add((TypeElement) ((DeclaredType) traverseElement.getSuperclass()).asElement());
            }
            
            for (TypeMirror typeMirror : traverseElement.getInterfaces()) {
                stack.add((TypeElement) ((DeclaredType) typeMirror).asElement());
            }
        }
        
        return list;
    }
    
    protected Set<TagHolder> getParentTagHolders(TagHolder tagHolder, Map<String, ? extends TagHolder> tagHolderMap) {
        Elements elementUtils = processingEnv.getElementUtils();
        Set<TagHolder> list = new LinkedHashSet<TagHolder>();
        Stack<TypeElement> stack = new Stack<TypeElement>();
        TypeElement traverseElement;
        TagHolder traverseTagHolder;
        TagHolder parentTagHolder = tagHolder;
        
        do {
            stack.add(elementUtils.getTypeElement(parentTagHolder.getClazz()));
            
            while (!stack.isEmpty()) {
                traverseElement = stack.pop();
                traverseTagHolder = tagHolderMap.get(traverseElement.getQualifiedName().toString());
                
                if (traverseTagHolder != null) {
                    list.add(traverseTagHolder);
                }
                
                if (traverseElement.getSuperclass() instanceof DeclaredType) {
                    stack.add((TypeElement) ((DeclaredType) traverseElement.getSuperclass()).asElement());
                }
                
                for (TypeMirror typeMirror : traverseElement.getInterfaces()) {
                    stack.add((TypeElement) ((DeclaredType) typeMirror).asElement());
                }
            }
        } while ((parentTagHolder = tagHolderMap.get(parentTagHolder.getParent())) != null);
        
        list.remove(tagHolder);
        return list;
    }
    
    protected void inheritAttributes(TagHolder tagHolder, Map<String, ? extends TagHolder> tagHolderMap) {
        if (tagHolder.getParent() == null) {
            return;
        }
        
        List<Attribute> attributes = tagHolder.getTag().getAttributes();
        List<Attribute> inheritedAttributes = new ArrayList<Attribute>();
        Set<String> parentFields = getParentAttributeNames(tagHolder, tagHolderMap);
        
        for (TagHolder parent : getParentTagHolders(tagHolder, tagHolderMap)) {
            for (Attribute parentAttribute : parent.getTag().getAttributes()) {
                Attribute inheritedAttribute = new Attribute(
                        parentAttribute.getName(),
                        parentAttribute.getDescription(),
                        parentAttribute.isRequired(),
                        parentAttribute.getType(),
                        parentAttribute.getObjectType(),
                        parentAttribute.getDefaultValue());
                
                if (parentFields.contains(parentAttribute.getName())) {
                    // When a parent already has implemented the field, don't
                    // generate in in the component/renderer
                    inheritedAttribute.setIgnore(true);
                } else {
                    inheritedAttribute.setIgnore(parentAttribute.isIgnore());
                }
                
                inheritedAttributes.add(inheritedAttribute);
            }
        }
        
        mergeAttributes(attributes, inheritedAttributes);
    }
    
    protected List<String> getHints(List<AnnotationValue> value) {
        List<String> hints = new ArrayList<String>(0);
        
        for (int i = 0; i < value.size(); i++) {
            hints.add("ClientBehaviorHint." + value.get(i).getValue().toString());
        }
        
        return hints;
    }
    
    protected Collection<Attribute> parseAttributes(List<AnnotationValue> list) {
        List<Attribute> attributes = new ArrayList<Attribute>(list.size());
        
        for (AnnotationValue annotationValue : list) {
            AnnotationMirror annotation = (AnnotationMirror) annotationValue.getValue();
            Attribute attribute = new Attribute();
            
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
                String memberName = entry.getKey().getSimpleName().toString();
                Object defaultValue = entry.getKey().getDefaultValue()
                        .getValue();
                Object value = entry.getValue().getValue();
                boolean isDefault = defaultValue != null
                        && defaultValue.equals(value);
                
                if ("name".equals(memberName) && !isDefault) {
                    attribute.setName(entry.getValue().getValue().toString());
                } else if ("required".equals(memberName) && !isDefault) {
                    attribute.setRequired(Boolean.valueOf(entry.getValue()
                            .getValue().toString()));
                } else if ("type".equals(memberName)) {
                    attribute.setType(entry.getValue().getValue().toString());
                    attribute.setObjectType(getObjectType(entry.getValue().getValue().toString()));
                } else if ("description".equals(memberName) && !isDefault) {
                    attribute
                            .setDescription(getDescription((AnnotationMirror) entry
                            .getValue().getValue()));
                } else if ("ignore".equals(memberName)) {
                    attribute.setIgnore(Boolean.valueOf(entry.getValue()
                            .getValue().toString()));
                } else if ("defaultValue".equals(memberName) && !isDefault) {
                    attribute.setDefaultValue(entry.getValue().getValue()
                            .toString());
                }
            }
            
            attributes.add(attribute);
        }
        
        return attributes;
    }
    
    protected void mergeAttributes(List<Attribute> attributes, Collection<Attribute> inheritedAttributes) {
        for (Attribute inheritedAttribute : inheritedAttributes) {
            mergeAttributes(attributes, inheritedAttribute);
        }
    }
    
    protected void mergeAttributes(List<Attribute> attributes, Attribute inheritedAttribute) {
        // The attributes in the list are the most concrete ones, meaning that
        // these are defined in the most concrete component/renderer.
        // Something that is not defined in these attributes but offered by
        // the inherited attribute must be merged into the existing attributes
        boolean found = false;
        Attribute attribute;
        
        for (int i = 0; i < attributes.size(); i++) {
            attribute = attributes.get(i);
            
            if (attribute.getName().equals(inheritedAttribute.getName())) {
                found = true;
                
                attribute.setType(inheritedAttribute.getType());
                attribute.setObjectType(inheritedAttribute.getObjectType());
                attribute.setRequired(inheritedAttribute.isRequired());
                attribute.setIgnore(inheritedAttribute.isIgnore());
                
                if (attribute.getDefaultValue() == null) {
                    attribute.setDefaultValue(inheritedAttribute.getDefaultValue());
                }
                
                if (attribute.getDescription() == null) {
                    attribute.setDescription(inheritedAttribute.getDescription());
                } else {
                    Description description = attribute.getDescription();
                    Description inheritedDescription = inheritedAttribute.getDescription();
                    
                    if (description.getDisplayName() == null) {
                        description.setDisplayName(inheritedDescription.getDisplayName());
                    }
                    if (description.getDescription() == null) {
                        description.setDescription(inheritedDescription.getDescription());
                    }
                    if (description.getIcon() == null) {
                        description.setIcon(inheritedDescription.getIcon());
                    } else {
                        Icon icon = description.getIcon();
                        Icon inheritedIcon = inheritedDescription.getIcon();
                        
                        if (icon.getSmallIcon() == null) {
                            icon.setSmallIcon(inheritedIcon.getSmallIcon());
                        }
                        if (icon.getLargeIcon() == null) {
                            icon.setLargeIcon(inheritedIcon.getLargeIcon());
                        }
                    }
                }
                
                break;
            }
        }
        
        if (!found) {
            attributes.add(inheritedAttribute);
        }
    }
    
    protected static String firstToLowerCase(String string) {
        return new StringBuilder(string.length())
                .append(Character.toLowerCase(string.charAt(0)))
                .append(string, 1, string.length()).toString();
    }
    
    protected static String getObjectType(String string) {
        if ("byte".equals(string)) {
            return "java.lang.Byte";
        } else if ("short".equals(string)) {
            return "java.lang.Short";
        } else if ("int".equals(string)) {
            return "java.lang.Integer";
        } else if ("long".equals(string)) {
            return "java.lang.Long";
        } else if ("float".equals(string)) {
            return "java.lang.Float";
        } else if ("double".equals(string)) {
            return "java.lang.Double";
        } else if ("boolean".equals(string)) {
            return "java.lang.Boolean";
        } else if ("char".equals(string)) {
            return "java.lang.Character";
        }
        
        return string;
    }
}
