package uapi.annotation.internal;

import com.google.auto.service.AutoService;
import uapi.GeneralException;
import uapi.codegen.AnnotationsHandler;
import uapi.codegen.IAnnotationsHandler;
import uapi.codegen.IBuilderContext;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.StandardLocation;
import java.io.*;
import java.lang.annotation.Annotation;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

@AutoService(IAnnotationsHandler.class)
public class ModuleHandler extends AnnotationsHandler {

    private static final String MODULE_DATA_FILE    = "META-INF/uapi/module";

    @Override
    protected Class<? extends Annotation>[] getOrderedAnnotations() {
        return new Class[] { uapi.annotation.Module.class };
    }

    @Override
    protected void handleAnnotatedElements(
            final IBuilderContext builderContext,
            final Class<? extends Annotation> annotationType,
            final Set<? extends Element> elements
    ) throws GeneralException {
        // Scan annotation element to get module information
        ModuleInfo moduleInfo = Looper.on(elements).map(element -> {
            if (element.getKind() != ElementKind.MODULE) {
                throw new GeneralException(
                        "The Module annotation only support on module element - {}",
                        element.getSimpleName().toString());
            }
            ModuleInfo module = new ModuleInfo();
            module.name = element.getSimpleName().toString();
            return module;
        }).first();

         // Generate module data file
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        builderContext.getFiler()
                                .createResource(StandardLocation.CLASS_OUTPUT, "", MODULE_DATA_FILE)
                                .openOutputStream(), UTF_8))) {
            writer.write(moduleInfo.name);
            writer.flush();
        } catch (IOException ex) {
            throw new GeneralException(ex,"Generate module data file failed - {}", moduleInfo.name);
        }
    }

    private static final class ModuleInfo {

        private String name;
    }
}
