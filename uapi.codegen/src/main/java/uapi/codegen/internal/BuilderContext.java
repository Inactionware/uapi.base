/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen.internal;

import freemarker.template.Configuration;
import freemarker.template.Template;
import uapi.GeneralException;
import uapi.codegen.ClassMeta;
import uapi.codegen.IBuilderContext;
import uapi.codegen.IHandlerHelper;
import uapi.codegen.LogSupport;
import uapi.common.ArgumentChecker;
import uapi.common.CollectionHelper;
import uapi.common.StringHelper;
import uapi.rx.Looper;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A context for building class builder
 */
public class BuilderContext implements IBuilderContext {

    private final LogSupport _logger;
    private final ProcessingEnvironment _procEnv;
    private final RoundEnvironment _roundEnv;
    private final List<ClassMeta.Builder> _clsBuilders = new ArrayList<>();
    private final Configuration _tempConf;
    private final Map<String, IHandlerHelper> _helpers = new HashMap<>();

    public BuilderContext(
            final ProcessingEnvironment processingEnvironment,
            final RoundEnvironment roundEnvironment) {
        ArgumentChecker.notNull(processingEnvironment, "processingEnvironment");
        ArgumentChecker.notNull(roundEnvironment, "roundEnvironment");
        this._procEnv = processingEnvironment;
        this._roundEnv = roundEnvironment;
        this._logger = new LogSupport(processingEnvironment);
        // Initialize freemarker template configuration
        this._tempConf = new Configuration(Configuration.VERSION_2_3_22);
        this._tempConf.setDefaultEncoding("UTF-8");
        this._tempConf.setLocalizedLookup(false);
        this._tempConf.setTemplateLoader(
                new CompileTimeTemplateLoader(this, StringHelper.EMPTY));
    }

    @Override
    public ProcessingEnvironment getProcessingEnvironment() {
        return this._procEnv;
    }

    @Override
    public RoundEnvironment getRoundEnvironment() {
        return this._roundEnv;
    }

    @Override
    public LogSupport getLogger() {
        return this._logger;
    }

    @Override
    public Elements getElementUtils() {
        return this._procEnv.getElementUtils();
    }

    @Override
    public Types getTypeUtils() {
        return this._procEnv.getTypeUtils();
    }

    @Override
    public Filer getFiler() {
        return this._procEnv.getFiler();
    }

    @Override
    public Set<? extends Element> getElementsAnnotatedWith (
            final Class<? extends Annotation> annotationType) {
        ArgumentChecker.notNull(annotationType, "annotationType");
        return this._roundEnv.getElementsAnnotatedWith(annotationType);
    }

    @Override
    public List<ClassMeta.Builder> getBuilders() {
        return this._clsBuilders;
    }

    @Override
    public void clearBuilders() {
        this._clsBuilders.clear();
    }

    @Override
    public Template loadTemplate(String templatePath) {
        ArgumentChecker.notEmpty(templatePath, "templatePath");
        Template temp;
        try {
            temp = this._tempConf.getTemplate(templatePath);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
        return temp;
    }

    @Override
    public ClassMeta.Builder findClassBuilder(Element classElement) {
        ArgumentChecker.notNull(classElement, "classElement");
        final ClassMeta.Builder expectedBuilder = ClassMeta.builder(classElement, this);
        List<ClassMeta.Builder> matchedClassBuilders = this._clsBuilders.parallelStream()
                .filter(existing -> existing.equals(expectedBuilder))
                .collect(Collectors.toList());
        ClassMeta.Builder clsBuilder;
        if (matchedClassBuilders.size() == 0) {
            this._clsBuilders.add(expectedBuilder);
            clsBuilder = expectedBuilder;
        } else if (matchedClassBuilders.size() == 1) {
            clsBuilder = matchedClassBuilders.get(0);
        } else {
            throw new GeneralException(
                    "Expect found only 1 class builder for {}, but found {}",
                    expectedBuilder.getPackageName() + "." + expectedBuilder.getClassName(),
                    matchedClassBuilders.size());
        }
        return clsBuilder;
    }

    @Override
    public ClassMeta.Builder findClassBuilder(String packageName, String className, boolean isCreate) {
        List<ClassMeta.Builder> matchedClassBuilders = Looper.on(this._clsBuilders)
                .filter(clsBuilder -> clsBuilder.getPackageName().equals(packageName))
                .filter(clsBuilder -> clsBuilder.getGeneratedClassName().equals(className))
                .toList();
        if (matchedClassBuilders.size() == 0) {
            if (isCreate) {
                return newClassBuilder(packageName, className);
            } else {
                return null;
            }
        } else if (matchedClassBuilders.size() == 1) {
            return matchedClassBuilders.get(0);
        } else {
            throw new GeneralException(
                    "Expect found only 1 class builder for {}, but found {}",
                    packageName + "." + className, matchedClassBuilders.size());
        }
    }

    @Override
    public ClassMeta.Builder newClassBuilder(final String classPackage, final String className) {
        ArgumentChecker.required(classPackage, "classPackage");
        ArgumentChecker.required(className, "className");
        ClassMeta.Builder duplicatedCls = Looper.on(this._clsBuilders)
                .filter(clsBuilder -> clsBuilder.getPackageName().equals(classPackage))
                .filter(clsBuilder -> clsBuilder.getGeneratedClassName().equals(className))
                .first(null);
        if (duplicatedCls != null) {
            throw new GeneralException("Found duplicated class {} was defined under package {}",
                    classPackage, className);
        }
        ClassMeta.Builder newBuilder = ClassMeta.builder();
        this._clsBuilders.add(newBuilder);
        return newBuilder.setPackageName(classPackage).setGeneratedClassName(className);
    }

    @Override
    public void checkModifiers(
            final Element element,
            final Class<? extends Annotation> annotation,
            final Modifier... unexpectedModifiers
    ) throws GeneralException {
        Set<Modifier> existingModifiers = element.getModifiers();
        Modifier unsupportedModifier = CollectionHelper.contains(existingModifiers, unexpectedModifiers);
        if (unsupportedModifier != null) {
            throw new GeneralException(
                    "The {} element [{}.{}] with {} annotation must not be {}",
                    element.getKind(),
                    element.getEnclosingElement().getSimpleName().toString(),
                    element.getSimpleName().toString(),
                    annotation.getName(),
                    unsupportedModifier);
        }
    }

    @Override
    public void checkAnnotations(
            final Element element,
            final Class<? extends Annotation>... annotationTypes
    ) throws GeneralException {
        ArgumentChecker.notNull(element, "element");
        List<Class<? extends Annotation>> unAnnotateds = Looper.on(annotationTypes)
                .filter(annotationType -> element.getAnnotation(annotationType) == null)
                .toList();
        if (unAnnotateds == null || unAnnotateds.size() > 0) {
            throw new GeneralException("The {} element [{}] does not annotated with {}.",
                    element.getKind(),
                    element.getSimpleName().toString(),
                    CollectionHelper.asString(annotationTypes));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Element findFieldWith(
            final Element classElement,
            final Class<?> fieldType,
            final Class annotationType) {
        ArgumentChecker.notNull(classElement, "classElement");
        ArgumentChecker.notNull(fieldType, "fieldType");
        ArgumentChecker.notNull(annotationType, "annotationType");
        List<Element> elems = (List<Element>) Looper.on(classElement.getEnclosedElements())
                .filter(element -> element.getKind() == ElementKind.FIELD)
                .filter(fieldElement -> fieldElement.asType().toString().equals(fieldType.getCanonicalName()))
                .filter(fieldElement -> fieldElement.getAnnotation(annotationType) != null)
                .toList();
        if (elems == null || elems.size() == 0) {
            return null;
        }
        return elems.get(0);
    }

    @Override
    public void putHelper(IHandlerHelper helper) {
        ArgumentChecker.required(helper, "helper");
        this._helpers.put(helper.getName(), helper);
    }

    @Override
    public IHandlerHelper getHelper(String name) {
        ArgumentChecker.required(name, "name");
        return this._helpers.get(name);
    }

    @Override
    public boolean isAssignable(final Element classElement, final Class type) {
        Elements elemtUtils = getElementUtils();
        Types typeUtils = getTypeUtils();
        TypeElement typeElemt = elemtUtils.getTypeElement(type.getCanonicalName());
        DeclaredType elemtType = typeUtils.getDeclaredType(typeElemt);
        return typeUtils.isAssignable(classElement.asType(), elemtType);
    }

    @Override
    public boolean isAssignable(final String type1, final Class type2) {
        return isAssignable(type1, type2.getCanonicalName());
    }

    @Override
    public boolean isAssignable(final String type1, final String type2) {
        Elements elemtUtils = getElementUtils();
        Types typeUtils = getTypeUtils();
        TypeElement typeElemt1 = elemtUtils.getTypeElement(type1);
        DeclaredType elemtType1 = typeUtils.getDeclaredType(typeElemt1);
        TypeElement typeElemt2 = elemtUtils.getTypeElement(type2);
        DeclaredType elemtType2 = typeUtils.getDeclaredType(typeElemt2);
        return typeUtils.isAssignable(elemtType1, elemtType2);
    }
}
