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
import uapi.common.ArgumentChecker;
import uapi.common.CollectionHelper;
import uapi.common.Pair;
import uapi.common.StringHelper;
import uapi.rx.Looper;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation for handle related annotations
 */
public abstract class AnnotationsHandler implements IAnnotationsHandler {

    @Override
    public Class<? extends Annotation>[] getSupportedAnnotations() {
        return getOrderedAnnotations();
    }

    protected void checkModifiers(
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

    protected void checkAnnotations(
            final Element element,
            final Class<? extends Annotation>... annotationTypes
    ) throws GeneralException {
        ArgumentChecker.notNull(element, "element");
//        List<Class<? extends Annotation>> unAnnotateds = Observable.on(annotationTypes)
//                .filter(annotationType -> element.getAnnotation(annotationType) == null)
//                .toList().toBlocking().single();
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

    protected String getTypeInAnnotation(
            final AnnotationMirror annotation,
            final String fieldName
    ) throws GeneralException {
        ArgumentChecker.notNull(annotation, "annotation");
        ArgumentChecker.notEmpty(fieldName, "fieldName");
//        List<String> types = Observable.on(annotation.getElementValues().entrySet())
//                .filter(entry -> fieldName.equals(entry.getKey().getSimpleName().toString()))
//                .map(Map.Entry::getValue)
//                .map(annoValue -> (DeclaredType) annoValue.getValue())
//                .map(declaredType -> (TypeElement) declaredType.asElement())
//                .map(typeElem -> typeElem.getQualifiedName().toString())
//                .toList().toBlocking().single();
        List<String> types = Looper.on(annotation.getElementValues().entrySet())
                .filter(entry -> fieldName.equals(entry.getKey().getSimpleName().toString()))
                .map(Map.Entry::getValue)
                .map(annoValue -> (DeclaredType) annoValue.getValue())
                .map(declaredType -> (TypeElement) declaredType.asElement())
                .map(typeElem -> typeElem.getQualifiedName().toString())
                .toList();
        if (types == null || types.size() == 0) {
            return null;
        } else if (types.size() == 1) {
            return types.get(0);
        } else {
            throw new GeneralException("Found more than one value are defined in annotation {} - {}",
                    annotation.getAnnotationType().toString(), types);
        }
    }

    /**
     * The utility method for getting class object which is defined in Annotation
     *
     * @param   annotation
     *          The annotation
     * @param   fieldName
     *          The field which hold class object
     * @return  The class object list
     */
    @SuppressWarnings("unchecked")
    protected List<String> getTypesInAnnotation(
            final AnnotationMirror annotation,
            final String fieldName
    ) {
        ArgumentChecker.notNull(annotation, "annotation");
        ArgumentChecker.notEmpty(fieldName, "fieldName");
        List<String> types = new ArrayList<>();
//        Observable.on(annotation.getElementValues().entrySet())
//                .filter(entry -> fieldName.equals(entry.getKey().getSimpleName().toString()))
//                .map(Map.Entry::getValue)
//                .flatMap(annoValue -> Observable.on((List<AnnotationValue>) annoValue.getValue()))
//                .map(annoValue -> (DeclaredType) annoValue.getValue())
//                .map(declaredType -> (TypeElement) declaredType.asElement())
//                .map(typeElem -> typeElem.getQualifiedName().toString())
//                .subscribe(types::add, logger::error);
//        try {
            Looper.on(annotation.getElementValues().entrySet())
                    .filter(entry -> fieldName.equals(entry.getKey().getSimpleName().toString()))
                    .map(Map.Entry::getValue)
                    .flatmap(annoValue -> Looper.on((List<AnnotationValue>) annoValue.getValue()))
                    .map(annoValue -> (DeclaredType) annoValue.getValue())
                    .map(declaredType -> (TypeElement) declaredType.asElement())
                    .map(typeElem -> typeElem.getQualifiedName().toString())
                    .foreach(types::add);
//        } catch (Exception ex) {
//            logger.error(ex);
//        }
        return types;
    }

    @Override
    public void handle(
            final IBuilderContext builderContext
    ) throws GeneralException {
//        Observable.on(getOrderedAnnotations())
//                .map((annotation) -> new Pair<>(annotation, builderContext.getElementsAnnotatedWith(annotation)))
//                .subscribe(pair -> handleAnnotatedElements(builderContext, pair.getLeftValue(), pair.getRightValue()),
//                        (t) -> builderContext.getLogger().error(t));
        Looper.on(getOrderedAnnotations())
                .map((annotation) -> new Pair<>(annotation, builderContext.getElementsAnnotatedWith(annotation)))
                .foreach(pair -> handleAnnotatedElements(builderContext, pair.getLeftValue(), pair.getRightValue()));
    }

    protected abstract Class<? extends Annotation>[] getOrderedAnnotations();

    public IHandlerHelper getHelper() {
        return null;
    }

    /**
     * Handle specified annotation on specified annotated elements
     *
     * @param   builderContext
     *          The context for building object
     * @param   annotationType
     *          The annotation type which is applied on the elements
     * @param   elements
     *          The elements which was annotated
     * @throws  GeneralException
     *          Handle elements failed
     */
    protected abstract void handleAnnotatedElements(
            final IBuilderContext builderContext,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> elements
    ) throws GeneralException;

    @Override
    public String toString() {
        return StringHelper.makeString("AnnotationsHandler[supportedAnnotations={}]",
                CollectionHelper.asString(getSupportedAnnotations()));
    }
}
