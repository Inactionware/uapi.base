package uapi.annotation.internal

import uapi.GeneralException
import uapi.annotation.IAnnotationsHandler
import uapi.annotation.IBuilderContext
import uapi.annotation.IHandlerHelper

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
