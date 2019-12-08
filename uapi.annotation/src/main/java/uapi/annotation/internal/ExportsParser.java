package uapi.annotation.internal;

import uapi.GeneralException;
import uapi.rx.Looper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.util.Set;

final class ExportsParser {

    void parse(
            final ModuleHelper helper,
            final Set<? extends Element> elements
    ) throws GeneralException {
        Looper.on(elements).foreach(element -> {
            var elementKind = element.getKind();
            if (elementKind != ElementKind.PACKAGE) {
                throw new GeneralException(
                        "The Exports annotation only can be applied on package element - {}",
                        element.getSimpleName().toString()
                );
            }
            var pkgName = ((PackageElement) element).getQualifiedName().toString();
            helper.addExport(pkgName);
        });
    }
}
