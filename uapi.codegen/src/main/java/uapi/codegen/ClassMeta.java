/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen;

import uapi.GeneralException;
import uapi.InvalidArgumentException;
import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClassMeta {

    private final Builder _builder;

    private ClassMeta(
            final Builder builder
    ) {
        this._builder = builder;
    }

    public String getPackageName() {
        return this._builder._pkgName;
    }

    public String getClassName() {
        return this._builder._className;
    }

    public String getGeneratedClassName() {
        return this._builder._generatedClassName;
    }

    public String getQualifiedClassName() {
        return this._builder.getQualifiedClassName();
    }

    public List<String> getImports() {
        return this._builder._imports;
    }

    public List<String> getImplements() {
        return this._builder._implements;
    }

    public List<AnnotationMeta> getAnnotations() {
        return this._builder._annotations;
    }

    public List<FieldMeta> getFields() {
        return this._builder._fields;
    }

    public List<MethodMeta> getMethods() {
        return this._builder._methods;
    }

    public List<PropertyMeta> getProperties() {
        return this._builder._properties;
    }

    @Override
    public String toString() {
        return this._builder.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(
            final Element classElement,
            final IBuilderContext builderContext
    ) throws GeneralException {
        ArgumentChecker.notNull(classElement, "classElement");
        ArgumentChecker.notNull(builderContext, "builderContext");
        if (classElement.getKind() != ElementKind.CLASS) {
            throw new GeneralException(
                    "The element is not a class element - {}",
                    classElement);
        }

        StringBuilder classNameBuilder = new StringBuilder(classElement.getSimpleName().toString());
        Element enclosingElemt = classElement.getEnclosingElement();
        while (enclosingElemt != null && enclosingElemt.getKind() == ElementKind.CLASS) {
            classNameBuilder.insert(0, ".").insert(0, enclosingElemt.getSimpleName().toString());
            enclosingElemt = enclosingElemt.getEnclosingElement();
        }

        PackageElement pkgElemt = builderContext.getElementUtils().getPackageOf(classElement);
        return builder()
                .setPackageName(pkgElemt.getQualifiedName().toString())
                .setClassName(classNameBuilder.toString())
                .setGeneratedClassName(classElement.getSimpleName().toString() + "_Generated");

    }

    public static class Builder extends CodegenBuilder<ClassMeta> {

        private String _pkgName;
        private String _className;
        private String _generatedClassName;
        private List<String> _imports = new ArrayList<>();
        private List<String> _implements = new ArrayList<>();
        private List<AnnotationMeta> _annotations = new ArrayList<>();
        private List<AnnotationMeta.Builder> _annoBuilders = new ArrayList<>();
        private List<FieldMeta> _fields = new ArrayList<>();
        private List<FieldMeta.Builder> _fieldBuilders = new ArrayList<>();
        private List<MethodMeta> _methods = new ArrayList<>();
        private List<MethodMeta.Builder> _methodBuilders = new ArrayList<>();
        private List<PropertyMeta> _properties = new ArrayList<>();
        private List<PropertyMeta.Builder> _propBuilders = new ArrayList<>();

        private Builder() { }

        public Builder setPackageName(
                final String packageName
        ) throws GeneralException {
            checkStatus();
            this._pkgName = packageName;
            return this;
        }

        public String getPackageName() {
            return this._pkgName;
        }

        public Builder setClassName(
                final String serviceClassName
        ) throws GeneralException {
            checkStatus();
            if (this._className != null) {
                throw new GeneralException(
                        "The class {} is already has super class - {}", this._generatedClassName, this._className);
            }
            this._className = serviceClassName;
            return this;
        }

        public String getClassName() {
            return this._className;
        }

        public String getQualifiedClassName() {
            return this._pkgName + "." + this._generatedClassName;
        }

        public Builder setGeneratedClassName(
                final String generatedClassName
        ) throws GeneralException {
            checkStatus();
            this._generatedClassName = generatedClassName;
            return this;
        }

        public Builder addImport(
                final String importClassName
        ) throws GeneralException {
            checkStatus();
            ArgumentChecker.notEmpty(importClassName, "importClassName");
            this._imports.add(importClassName);
            return this;
        }

        public Builder addImplement(
                final Class<?> clazz
        ) throws GeneralException {
            checkStatus();
            ArgumentChecker.notNull(clazz, "clazz");
            return addImplement(clazz.getCanonicalName());
        }

        public Builder addImplement(
                final String implement
        ) throws GeneralException {
            checkStatus();
            ArgumentChecker.notNull(implement, "implement");
            if (! this._implements.contains(implement)) {
                this._implements.add(implement);
            }
            return this;
        }

        public Builder addAnnotationBuilder(
                final AnnotationMeta.Builder annotationMetaBuilder
        ) throws GeneralException {
            checkStatus();
            ArgumentChecker.notNull(annotationMetaBuilder, "annotationMetaBuilder");
            this._annoBuilders.add(annotationMetaBuilder);
            return this;
        }

        public FieldMeta.Builder findFieldBuilder(
                final FieldMeta.Builder fieldMetaBuilder
        ) throws GeneralException {
            ArgumentChecker.notNull(fieldMetaBuilder, "fieldMetaBuilder");
            List<FieldMeta.Builder> matchedBuilders = this._fieldBuilders.parallelStream()
                    .filter(existing -> existing.equals(fieldMetaBuilder))
                    .collect(Collectors.toList());
            if (matchedBuilders.size() > 1) {
                throw new GeneralException(
                        "Found more than one method builder for {}", fieldMetaBuilder);
            }
            if (matchedBuilders.size() == 1) {
                return matchedBuilders.get(0);
            }
            return null;
        }

        public FieldMeta.Builder findFieldBuilder(String fieldName, String fieldType) {
            ArgumentChecker.required(fieldName, "fieldName");
            ArgumentChecker.required(fieldType, "fieldType");
            return Looper.on(this._fieldBuilders)
                    .filter(fieldBuilder -> fieldBuilder.getName().equals(fieldName))
                    .filter(fieldBuilder -> fieldBuilder.getTypeName().equals(fieldType))
                    .single(null);
        }

        public Builder addFieldBuilderIfAbsent(
                final FieldMeta.Builder fieldMetaBuilder
        ) throws GeneralException {
            checkStatus();
            FieldMeta.Builder fieldBuilder = findFieldBuilder(fieldMetaBuilder);
            if (fieldBuilder == null) {
                addFieldBuilder(fieldMetaBuilder);
            }
            return this;
        }

        public Builder addFieldBuilder(
                final FieldMeta.Builder fieldMetaBuilder
        ) throws GeneralException {
            checkStatus();
            this._fieldBuilders.add(fieldMetaBuilder);
            return this;
        }

        public Builder addMethodBuilder(
                final MethodMeta.Builder methodMetaBuilder
        ) throws GeneralException {
            checkStatus();
            ArgumentChecker.notNull(methodMetaBuilder, "methodMetaBuilder");
            if (! this._methodBuilders.contains(methodMetaBuilder)) {
                this._methodBuilders.add(methodMetaBuilder);
            }
            return this;
        }

        public Builder addPropertyBuilder(
                final PropertyMeta.Builder propertyMetaBuilder
        ) throws GeneralException {
            checkStatus();
            ArgumentChecker.notNull(propertyMetaBuilder, "propertyMetaBuilder");
            if (! this._propBuilders.contains(propertyMetaBuilder)) {
                this._propBuilders.add(propertyMetaBuilder);
            }
            return this;
        }

        public String getGeneratedClassName() {
            return this._generatedClassName;
        }

        public MethodMeta.Builder findMethodBuilder(
                final Element methodElement,
                final IBuilderContext builderContext
        ) throws GeneralException {
            ArgumentChecker.notNull(methodElement, "methodElement");
            MethodMeta.Builder methodBuilder = MethodMeta.builder(methodElement, builderContext);
            List<MethodMeta.Builder> matchedBuilders = this._methodBuilders.parallelStream()
                    .filter(existing -> existing.equals(methodBuilder))
                    .collect(Collectors.toList());
            if (matchedBuilders.size() > 1) {
                throw new GeneralException(
                        "Found more than one method builder for {}"
                        , methodBuilder);
            }
            if (matchedBuilders.size() == 1) {
                return matchedBuilders.get(0);
            }
            this._methodBuilders.add(methodBuilder);
            return methodBuilder;
        }

        public List<MethodMeta.Builder> findMethodBuilder(final String methodName) {
            ArgumentChecker.required(methodName, "methodName");
            return Looper.on(this._methodBuilders)
                    .filter(builder -> builder.getName().equals(methodName)).toList();
        }

        public List<MethodMeta.Builder> findSetterBuilders() {
            List<MethodMeta.Builder> setters = new ArrayList<>();
            this._methodBuilders.forEach(methodBuilder -> {
                if (methodBuilder.isSetter()) {
                    setters.add(methodBuilder);
                }
            });
            return setters;
        }

        public List<MethodMeta.Builder> findMethodBuilders(
                final String methodName
        ) throws InvalidArgumentException {
            ArgumentChecker.notEmpty(methodName, "methodName");
            return this._methodBuilders.parallelStream()
                    .filter(methodBuilder -> methodBuilder.getName().equals(methodName))
                    .collect(Collectors.toList());
        }

        public MethodMeta.Builder findMethodBuilder(
                final MethodMeta.Builder methodBuilder
        ) throws InvalidArgumentException {
            ArgumentChecker.notNull(methodBuilder, "methodBuilder");
            List<MethodMeta.Builder> matchedMethods = this._methodBuilders.parallelStream()
                    .filter(existing -> existing.equals(methodBuilder))
                    .collect(Collectors.toList());
            if (matchedMethods.size() == 0) {
                return null;
            }
            if (matchedMethods.size() == 1) {
                return matchedMethods.get(0);
            }
            throw new GeneralException("Found not only one method builder - {}" + matchedMethods);
        }

        public MethodMeta.Builder addMethodBuilderIfAbsent(
                final MethodMeta.Builder methodBuilder
        ) throws InvalidArgumentException {
            checkStatus();
            MethodMeta.Builder foundBuilder = findMethodBuilder(methodBuilder);
            if (foundBuilder == null) {
                addMethodBuilder(methodBuilder);
                return methodBuilder;
            } else {
                return foundBuilder;
            }
        }

        public MethodMeta.Builder overrideMethodBuilder(
                final MethodMeta.Builder methodBuilder
        ) throws InvalidArgumentException {
            checkStatus();
            boolean isFound = false;
            for (int i = 0; i < this._methodBuilders.size(); i++) {
                if (this._methodBuilders.get(i).equals(methodBuilder)) {
                    this._methodBuilders.set(i, methodBuilder);
                    isFound = true;
                }
            }
            if (! isFound) {
                this._methodBuilders.add(methodBuilder);
            }
            return methodBuilder;
        }

        @Override
        protected void validate() throws InvalidArgumentException {
            ArgumentChecker.notEmpty(this._pkgName, "packageName");
//            ArgumentChecker.notEmpty(this._className, "className");
            ArgumentChecker.notEmpty(this._generatedClassName, "generatedClassName");
            this._annoBuilders.forEach(AnnotationMeta.Builder::validate);
            this._fieldBuilders.forEach(FieldMeta.Builder::validate);
            this._methodBuilders.forEach(MethodMeta.Builder::validate);
            this._propBuilders.forEach(PropertyMeta.Builder::validate);
        }

        @Override
        protected void initProperties() {
            this._annoBuilders.forEach(annoBuilder -> {
                annoBuilder.initProperties();
                this._annotations.add(annoBuilder.createInstance());
            });
            this._fieldBuilders.forEach(fieldBuilder -> {
                fieldBuilder.initProperties();
                this._fields.add(fieldBuilder.createInstance());
            });
            this._methodBuilders.forEach(methodBuilder -> {
                methodBuilder.initProperties();
                this._methods.add(methodBuilder.createInstance());
            });
            this._propBuilders.forEach(propertyBuilder -> {
                propertyBuilder.initProperties();
                this._properties.add(propertyBuilder.createInstance());
            });
        }

        @Override
        protected ClassMeta createInstance() {
            return new ClassMeta(this);
        }

        @Override
        public String toString() {
            return StringHelper.makeString(
                    "ClassMeta[packageName={}, " +
                            "className={}, " +
                            "generatedClassName={}, " +
                            "implements={}, " +
                            "annotations={}, " +
                            "fields={}, " +
                            "fieldBuilders={}, " +
                            "methods={}, " +
                            "properties={}]",
                    this._pkgName,
                    this._className,
                    this._generatedClassName,
                    this._implements,
                    this._annoBuilders,
                    this._fields,
                    this._fieldBuilders,
                    this._methodBuilders,
                    this._propBuilders);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Builder builder = (Builder) o;
            return Objects.equals(this._pkgName, builder._pkgName) &&
                    Objects.equals(this._className, builder._className);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this._pkgName, this._className);
        }
    }
}
