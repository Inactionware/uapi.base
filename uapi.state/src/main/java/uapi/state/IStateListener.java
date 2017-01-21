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
 * The listener used to monitor specific state
 */
public interface IStateListener<T> {

    /**
     * Invoked when state changed
     *
     * @param   oldState
     *          The old state
     * @param   newState
     *          The new state
     */
    void stateChanged(T oldState, T newState);
}
