/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.annotation.internal;

import com.google.auto.service.AutoService;
import uapi.GeneralException;
import uapi.annotation.Exports;
import uapi.annotation.Uses;
import uapi.codegen.*;
import uapi.common.ArgumentChecker;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

@AutoService(IAnnotationsHandler.class)
public final class ModuleHandler extends AnnotationsHandler {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] orderedAnnotations = new Class[] {
            uapi.annotation.Module.class, Exports.class, Uses.class
    };

    private final ModuleHelper _helper = new ModuleHelper();
    private final ModuleParser _moduleParser = new ModuleParser();
    private final ExportsParser _exportsParser = new ExportsParser();
    private final UsesParser _usesParser = new UsesParser();

    @Override
    protected Class<? extends Annotation>[] getOrderedAnnotations() {
        return orderedAnnotations;
    }

    @Override
    protected void handleAnnotatedElements(
            final IBuilderContext builderContext,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> elements
    ) throws GeneralException {
        ArgumentChecker.required(annotationType, "annotationType");

        if (annotationType.equals(uapi.annotation.Module.class)) {
            this._moduleParser.parse(this._helper, elements);
        } else if (annotationType.equals(Exports.class)) {
            this._exportsParser.parse(this._helper, elements);
        } else if (annotationType.equals(Uses.class)) {
            this._usesParser.parse(this._helper, elements);
        } else {
            throw new GeneralException("Unsupported annotation - {}", annotationType.getName());
        }
    }

//    private void parseModule (
//            final IBuilderContext builderContext,
//            final Set<? extends Element> elements
//    ) {
//        if (elements.size() != 1) {
//            throw new GeneralException("The Module annotation only allow used one time for a project, currently: ", elements.size());
//        }
//        Element moduleElement = Looper.on(elements).first();
//        if (moduleElement.getKind() != ElementKind.CLASS) {
//            throw new GeneralException(
//                    "The Module annotation only can be applied on class - {}",
//                    moduleElement.getSimpleName().toString());
//        }
////        var classNameBuilder = new StringBuilder(moduleElement.getSimpleName().toString());
////        var enclosingElemt = moduleElement.getEnclosingElement();
////        while (enclosingElemt != null && enclosingElemt.getKind() == ElementKind.CLASS) {
////            classNameBuilder.insert(0, ".").insert(0, enclosingElemt.getSimpleName().toString());
////            enclosingElemt = enclosingElemt.getEnclosingElement();
////        }
//        String moduleClassName = StringHelper.makeString("{}.{}",
//                builderContext.getElementUtils().getPackageOf(moduleElement).getQualifiedName().toString(),
//                moduleElement.getSimpleName().toString());
//
////        var pkgElemt = builderContext.getElementUtils().getPackageOf(moduleElement);
////        classNameBuilder.append(pkgElemt.getSimpleName().toString());
//        Class<IModule> moduleClass = null;
//        try {
//            moduleClass = (Class<IModule>) Class.forName(moduleClassName);
//        } catch (ClassNotFoundException ex) {
//            throw new GeneralException(ex, "Can't get class from {}", moduleClassName);
//        } catch (ClassCastException ex) {
//            throw new GeneralException(ex, "The class {} can't cast to IModule interface", moduleClassName);
//        }
//        IModule module;
//        try {
//            module = moduleClass.getConstructor().newInstance();
//        } catch (NoSuchMethodException ex) {
//            throw new GeneralException(ex, "The class {} does not define a non-parameter constructor", moduleClassName);
//        } catch (Exception ex) {
//            throw new GeneralException(ex, "The class {} can't be instanced, be ensure the constructor is public", moduleClassName);
//        }
//
//        this._helper._module.setName(module.name());
//        Looper.on(module.exports()).foreach(this._helper._module::addExport);
//        Looper.on(module.requires()).foreach(this._helper._module::addRequire);
//        Looper.on(module.uses()).foreach(this._helper._module::addUse);
//        Looper.on(module.provides()).foreach(this._helper._module::addProvide);
//    }

    public IHandlerHelper getHelper() {
        return this._helper;
    }

}
