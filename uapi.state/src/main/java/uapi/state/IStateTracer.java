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
 * The state hold state data and logic which used to change state based on specific operation
 */
public interface IStateTracer<T> {

    /**
     * Subscribe this state on specific listener
     *
     * @param   listener
     *          The listener which will be notified when state changes
     */
    void subscribe(IStateListener<T> listener);

    /**
     * Unsubscribe this state on specific listener
     *
     * @param   listener
     *          The listener
     */
    void unsubscribe(IStateListener<T> listener);

    /**
     * Receive current state
     *
     * @return  The current state
     */
    T get();

    /**
     * Apply operation on this state which may change current state
     *
     * @param   operation
     *          The operation
     */
    void shift(IOperation operation);

    /**
     * Apply operation which has no attached data on this state
     *
     * @param   operationType
     *          The operation type
     */
    void shift(String operationType);
}
