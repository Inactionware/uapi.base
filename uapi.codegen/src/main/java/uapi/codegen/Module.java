/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.codegen;

import uapi.GeneralException;
import uapi.common.ArgumentChecker;

import java.util.*;

public class Module {

    private boolean _enabled = false;
    private String _name;
    private List<String> _exports = new ArrayList<>();
    private List<String> _requires = new ArrayList<>();
    private List<String> _uses = new ArrayList<>();
    private Map<String /* service */, Set<String> /* implementations */> _provides = new HashMap<>();

    public boolean enabled() {
        return this._enabled;
    }

    /**
     * Return the name of this module.
     *
     * @return  The module name
     */
    public String name() {
        return this._name;
    }

    public void setName(final String name) {
        ArgumentChecker.required(name, "name");
        if (this._enabled) {
            throw new GeneralException("The module has been initialized more then one time - {}", this._name);
        }
        this._name = name;
        this._enabled = true;
    }

    /**
     * Get exported package list of this module.
     *
     * @return  Exported package list of this module
     */
    public String[] exports() {
        return this._exports.toArray(new String[0]);
    }

    public void addExport(final String export) {
        ArgumentChecker.required(export, "export");
        this._exports.add(export);
    }

    /**
     * Get required modules which are used in this module.
     *
     * @return  The required modules
     */
    public String[] requires() {
        return this._requires.toArray(new String[0]);
    }

    public void addRequire(final String require) {
        ArgumentChecker.required(require, "require");
        this._requires.add(require);
    }

    /**
     * Get used services of this module.
     *
     * @return  Used services of this module
     */
    public String[] uses() {
        return this._uses.toArray(new String[0]);
    }

    public void addUse(final String use) {
        ArgumentChecker.required(use, "use");
        this._uses.add(use);
    }

    /**
     * Get provides
     *
     * @return The provide list
     */
    public Map<String, Set<String>> provides() {
        return this._provides;
    }

    public void addProvide(
            final String service,
            final String implementation
    ) {
        ArgumentChecker.required(service, "service");
        ArgumentChecker.required(implementation, "implementation");
        var impls = this._provides.get(service);
        if (impls == null) {
            impls = new HashSet<>();
            this._provides.put(service, impls);
        }
        impls.add(implementation);
    }
}
