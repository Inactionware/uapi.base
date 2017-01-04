/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.state;

import uapi.state.internal.StateTracer;

/**
 * The utility class is used to create state instance
 */
public class StateCreator {

    public static <T> IStateTracer<T> createTracer(IShifter<T> shifter, T initState) {
        return new StateTracer<>(shifter, initState);
    }

    private StateCreator() { }
}
