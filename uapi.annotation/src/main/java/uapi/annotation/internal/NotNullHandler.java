/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.annotation.internal;

import com.google.auto.service.AutoService;
import uapi.InvalidArgumentException;
import uapi.GeneralException;
import uapi.annotation.*;
import uapi.codegen.*;
import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * A Handler for handle NotNull annotation
 */
@AutoService(IAnnotationsHandler.class)
public final class NotNullHandler extends AnnotationsHandler {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] orderedAnnotations = new Class[] { NotNull.class };

    @Override
    public Class<? extends Annotation>[] getOrderedAnnotations() {
        return orderedAnnotations;
    }

    @Override
    public void handleAnnotatedElements(
            final IBuilderContext builderCtx,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> paramElements
    ) throws GeneralException {
        ArgumentChecker.equals(annotationType, NotNull.class, "annotationType");

        paramElements.forEach(paramElement -> {
            if (paramElement.getKind() != ElementKind.PARAMETER) {
                throw new GeneralException(
                        "The NotNull annotation only can be applied on method parameter element - {}",
                        paramElement.getSimpleName().toString());
            }
            Element methodElement = paramElement.getEnclosingElement();
            builderCtx.checkModifiers(methodElement, NotNull.class, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

            var classElement = methodElement.getEnclosingElement();
            builderCtx.checkModifiers(classElement, NotNull.class, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

            var clsBuilder = builderCtx.findClassBuilder(classElement);
            var methodBuilder = clsBuilder.findMethodBuilder(methodElement, builderCtx);
            var paramBuilder = methodBuilder.findParameterBuilder(paramElement, builderCtx);

            String codes = StringHelper.makeString(
                    "{}.notNull({}, \"{}\");\n",
                    ArgumentChecker.class.getName(),
                    paramBuilder.getName(),
                    paramBuilder.getName());

            methodBuilder
                    .addThrowTypeName(InvalidArgumentException.class.getName())
                    .setInvokeSuper(MethodMeta.InvokeSuper.AFTER)
                    .addCodeBuilder(CodeMeta.builder().addRawCode(codes));
        });
    }
}
