package uapi.codegen.internal;

import uapi.IModule;
import uapi.Provide;
import uapi.common.ArgumentChecker;

import java.util.ArrayList;
import java.util.List;

public final class Module {

    private String _name;
    private List<String> _exports;
    private List<String> _requires;
    private List<String> _uses;
    private List<Provide> _provides;

    Module() {
        this._exports = new ArrayList<>();
        this._requires = new ArrayList<>();
        this._uses = new ArrayList<>();
        this._provides = new ArrayList<>();
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        ArgumentChecker.required(name, "name");
        this._name = name;
    }

    public String[] getExports() {
        return this._exports.toArray(new String[0]);
    }

    public void addExport(String export) {
        ArgumentChecker.required(export, "export");
        this._exports.add(export);
    }

    public String[] getRequires() {
        return this._requires.toArray(new String[0]);
    }

    public void addRequire(String require) {
        ArgumentChecker.required(require, "require");
        this._requires.add(require);
    }

    public String[] getUses() {
        return this._uses.toArray(new String[0]);
    }

    public void addUse(String use) {
        ArgumentChecker.required(use, "use");
        this._uses.add(use);
    }

    public Provide[] getProvides() {
        return this._provides.toArray(new Provide[0]);
    }

    public void addProvide(Provide provide) {
        ArgumentChecker.required(provide, "provide");
        this._provides.add(provide);
    }
}
