package uapi.codegen.internal;

import com.google.auto.service.AutoService;
import uapi.GeneralException;
import uapi.Module;
import uapi.codegen.AnnotationsHandler;
import uapi.codegen.IAnnotationsHandler;
import uapi.codegen.IBuilderContext;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

@AutoService(IAnnotationsHandler.class)
public class ModuleHandler extends AnnotationsHandler {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] orderedAnnotations = new Class[] { Module.class };

    private static final String TEMP_MODULE_INFO = "template/generated_module.ftl";

    @Override
    protected Class<? extends Annotation>[] getOrderedAnnotations() {
        return orderedAnnotations;
    }

    @Override
    protected void handleAnnotatedElements(
            final IBuilderContext builderContext,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> elements) throws GeneralException {

    }
}
