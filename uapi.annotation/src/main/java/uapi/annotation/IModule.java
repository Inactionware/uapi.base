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
    String name();

    /**
     * Get exported package list of this module.
     *
     * @return  Exported package list of this module
     */
    String[] exports();

    /**
     * Get required modules which are used in this module.
     *
     * @return  The required modules
     */
    String[] requires();

    /**
     * Get used services of this module.
     *
     * @return  Used services of this module
     */
    String[] uses();

    /**
     * Get provides
     *
     * @return The provide list
     */
    Provide[] provides();
}
