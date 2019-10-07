package uapi.codegen.internal;

import com.google.auto.service.AutoService;
import uapi.GeneralException;
import uapi.IModule;
import uapi.Provide;
import uapi.codegen.*;
import uapi.common.ArgumentChecker;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.*;

@AutoService(IAnnotationsHandler.class)
public class ModuleHandler extends AnnotationsHandler {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] orderedAnnotations = new Class[] { uapi.Module.class };

    private static final String TEMP_MODULE_INFO = "template/generated_module.ftl";

    private final Module _module = new Module();
    private final ModuleHelper _helper = new ModuleHelper();

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

        if (annotationType.equals(uapi.Module.class)) {
            parseModule(builderContext, elements);
        } else {
            throw new GeneralException("Unsupported annotation - {}", annotationType.getName());
        }
    }

    private void parseModule (
            final IBuilderContext builderContext,
            final Set<? extends Element> elements
    ) {
        if (elements.size() != 1) {
            throw new GeneralException("The Module annotation only allow used one time for a project, currently: ", elements.size());
        }
        Element moduleElement = Looper.on(elements).first();
        if (moduleElement.getKind() != ElementKind.CLASS) {
            throw new GeneralException(
                    "The Module annotation only can be applied on class - {}",
                    moduleElement.getSimpleName().toString());
        }
        var classNameBuilder = new StringBuilder(moduleElement.getSimpleName().toString());
        var enclosingElemt = moduleElement.getEnclosingElement();
        while (enclosingElemt != null && enclosingElemt.getKind() == ElementKind.CLASS) {
            classNameBuilder.insert(0, ".").insert(0, enclosingElemt.getSimpleName().toString());
            enclosingElemt = enclosingElemt.getEnclosingElement();
        }

        var pkgElemt = builderContext.getElementUtils().getPackageOf(moduleElement);
        classNameBuilder.append(pkgElemt.getSimpleName().toString());
        Class<IModule> moduleClass = null;
        try {
            moduleClass = (Class<IModule>) Class.forName(classNameBuilder.toString());
        } catch (ClassNotFoundException ex) {
            throw new GeneralException(ex, "Can't get class from {}", classNameBuilder.toString());
        } catch (ClassCastException ex) {
            throw new GeneralException(ex, "The class {} can't cast to IModule interface", classNameBuilder.toString());
        }
        IModule module;
        try {
            module = moduleClass.getConstructor().newInstance();
        } catch (NoSuchMethodException ex) {
            throw new GeneralException(ex, "The class {} does not define a non-parameter constructor", classNameBuilder.toString());
        } catch (Exception ex) {
            throw new GeneralException(ex, "The class {} can't be instanced, be ensure the constructor is public", classNameBuilder.toString());
        }

        this._module.setName(module.name());
        Looper.on(module.exports()).foreach(this._module::addExport);
        Looper.on(module.requires()).foreach(this._module::addRequire);
        Looper.on(module.uses()).foreach(this._module::addUse);
        Looper.on(module.provides()).foreach(this._module::addProvide);

        // Write to module-info file
        var model = new HashMap<String, Object>();
        model.put("module", this._module);
        var temp = builderContext.loadTemplate(TEMP_MODULE_INFO);

    }

    public IHandlerHelper getHelper() {
        return this._helper;
    }

    private final class ModuleHelper implements IModuleHandlerHelper {

        @Override
        public void setModuleName(String name) {
            ModuleHandler.this._module.setName(name);
        }

        @Override
        public void addRequire(String require) {
            ModuleHandler.this._module.addRequire(require);
        }

        @Override
        public void addUse(String use) {
            ModuleHandler.this._module.addUse(use);
        }

        @Override
        public void addExport(String export) {
            ModuleHandler.this._module.addExport(export);
        }

        @Override
        public void addProvide(String service, String implementation) {
            ArgumentChecker.required(service, "service");
            ArgumentChecker.required(implementation, "implementation");
            ModuleHandler.this._module.addProvide(new Provide(service, implementation));
        }
    }
}
