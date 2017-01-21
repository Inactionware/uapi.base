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

import java.lang.annotation.Annotation;

/**
 * A handler for handle one or more annotations which has relationship
 */
public interface IAnnotationsHandler {

    /**
     * Return a annotation type array which can be handled by this handler
     *
     * @return  A annotation type array which can be handled by this handler
     */
    Class<? extends Annotation>[] getSupportedAnnotations();


    /**
     * Return the specific helper for this handler
     *
     * @return  The handler helper or null if no helper for this handler
     */
    IHandlerHelper getHelper();

    /**
     * Handle all annotated element with context
     *
     * @param   builderContext
     *          The context
     * @throws  GeneralException
     *          Handle annotation failed
     */
    void handle(final IBuilderContext builderContext) throws GeneralException;
}
