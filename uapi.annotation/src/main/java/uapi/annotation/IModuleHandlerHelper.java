/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.annotation;

import uapi.codegen.IHandlerHelper;

/**
 * A Helper to maintain this module definition which will be used to build module-info file at build-time
 */
public interface IModuleHandlerHelper extends IHandlerHelper {

    /**
     * This helper's name
     */
    String name = "ModuleHelper";

    default String getName() {
        return name;
    }

    /**
     * Add required module for this module
     *
     * @param   require
     *          The required module
     */
    void addRequire(String require);

    /**
     * Add used service for this module
     *
     * @param   use
     *          The used service
     */
    void addUse(String use);

    /**
     * Add the exported package for this module
     *
     * @param   export
     *          The exported package
     */
    void addExport(String export);

    /**
     * Add provide service for this module
     *
     * @param   service
     *          The service interface
     * @param   implementation
     *          The service implementation
     */
    void addProvide(String service, String implementation);
}
