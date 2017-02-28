/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.exception;

/**
 * The IParameters represent one or more parameters which is referenced in error message template string.
 *
 * @param   <T>
 *          What's the templates presented type
 */
public interface IParameters<T> {

    /**
     * Get the result which hold all parameters
     *
     * @return  The object which hold all parameters
     */
    T get();
}
