package uapi.annotation.internal;

import uapi.GeneralException;
import uapi.annotation.IModuleHandlerHelper;
import uapi.codegen.IModuleProvider;
import uapi.codegen.Module;
import uapi.common.ArgumentChecker;

final class ModuleHelper implements IModuleHandlerHelper, IModuleProvider {

    private Module _module;

    void createModule(final String name) {
        if (this._module != null) {
            throw new GeneralException("An module [{}] exists, can't add another module [{}]",
                    this._module.name(),
                    name);
        }
        ArgumentChecker.required(name, "name");
        this._module = new Module();
        this._module.setName(name);
    }

    @Override
    public void addRequire(String require) {
        this._module.addRequire(require);
    }

    @Override
    public void addUse(String use) {
        this._module.addUse(use);
    }

    @Override
    public void addExport(String export) {
        this._module.addExport(export);
    }

    @Override
    public void addProvide(String service, String implementation) {
        this._module.addProvide(service, implementation);
    }

    @Override
    public boolean hasModule() {
        return false;
    }

    @Override
    public Module getModule() {
        return this._module;
    }
}
