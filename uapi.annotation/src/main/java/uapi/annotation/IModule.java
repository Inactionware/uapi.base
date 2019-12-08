/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.annotation;

import uapi.codegen.Provide;

/**
 * A Helper to maintain this module definition which will be used to build module-info file at build-time
 */
public interface IModule {

    /**
     * Return the name of this module.
     *
     * @return  The module name
     */
    default String name() {
        return null;
    }

    /**
     * Get exported package list of this module.
     *
     * @return  Exported package list of this module
     */
    default String[] exports() {
        return new String[0];
    }

    /**
     * Get required modules which are used in this module.
     *
     * @return  The required modules
     */
    default String[] requires() {
        return new String[0];
    }

    /**
     * Get used services of this module.
     *
     * @return  Used services of this module
     */
    default String[] uses() {
        return new String[0];
    }

    /**
     * Get provides
     *
     * @return The provide list
     */
    default Provide[] provides() {
        return new Provide[0];
    }
}
