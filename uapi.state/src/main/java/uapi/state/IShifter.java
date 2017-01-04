/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.state;

/**
 * A shifter is used to change state based on specific operation
 */
@FunctionalInterface
public interface IShifter<T> {

    /**
     * Apply operation and may change current state
     *
     * @param   state
     *          The current state
     * @param   operation
     *          The operation
     * @return  The new state
     */
    T shift(T state, IOperation operation);
}
