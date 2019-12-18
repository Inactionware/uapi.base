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
        } else {
            throw new GeneralException("Unsupported annotation - {}", annotationType.getName());
        }
    }

    public IHandlerHelper getHelper() {
        return this._helper;
    }

}
