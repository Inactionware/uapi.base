/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen.internal

import uapi.GeneralException
import uapi.codegen.IAnnotationsHandler
import uapi.codegen.IBuilderContext
import uapi.codegen.IHandlerHelper

import java.lang.annotation.Annotation

/**
 * The mock for IAnnotationsHandler
 */
class MockAnnotationsHandler implements IAnnotationsHandler {

    @Override
    Class<? extends Annotation>[] getSupportedAnnotations() {
        return new Class<? extends Annotation>[0]
    }

    @Override
    IHandlerHelper getHelper() {
        return new MockHelper()
    }

    @Override
    void handle(IBuilderContext builderContext) throws GeneralException {
        // do nothing
    }

    private class MockHelper implements IHandlerHelper {

        @Override
        String getName() {
            return 'Test'
        }
    }
}
