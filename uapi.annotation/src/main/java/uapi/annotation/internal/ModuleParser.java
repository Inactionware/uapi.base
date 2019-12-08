package uapi.annotation.internal;

import uapi.GeneralException;
import uapi.annotation.Module;
import uapi.common.StringHelper;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import java.util.Set;

final class ModuleParser {

    void parse(
            final ModuleHelper helper,
            final Set<? extends Element> elements
    ) throws GeneralException {
        if (elements.size() > 1) {
            throw new GeneralException("Only allow one Module annotation for a project");
        }
        if (elements.size() == 0) {
            return;
        }
        var element = (PackageElement) elements.iterator().next();
        var module = element.getAnnotation(Module.class);
        String moduleName = module.name();
        if (StringHelper.isNullOrEmpty(moduleName)) {
            moduleName = element.getQualifiedName().toString();
        }
        helper.createModule(moduleName);
        Looper.on(module.requires()).foreach(helper::addRequire);
    }
}
