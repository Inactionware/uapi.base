/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.state.internal;

import uapi.GeneralException;
import uapi.common.ArgumentChecker;
import uapi.rx.Looper;
import uapi.state.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The implementation of IStateTracer interface
 */
public final class StateTracer<T> implements IStateTracer<T> {

    private T _state;
    private final IChecker<T> _checker;
    private final IShifter<T> _shifter;

    private final List<IStateListener<T>> _listeners = new LinkedList<>();
    private final Lock _lock = new ReentrantLock();

    public StateTracer(final IShifter<T> shifter, final T initState) {
        this(shifter, initState, null);
    }

    public StateTracer(final IShifter<T> shifter, final T initState, final IChecker<T> checker) {
        ArgumentChecker.required(shifter, "shifter");
        ArgumentChecker.notNull(initState, "initState");

        this._checker = checker;
        this._state = initState;
        this._shifter = shifter;
    }

    @Override
    public void subscribe(final IStateListener<T> listener) {
        ArgumentChecker.required(listener, "listener");
        if (this._listeners.contains(listener)) {
            throw new GeneralException("The listener is registered - {}", listener);
        }
        this._listeners.add(listener);
    }

    @Override
    public void unsubscribe(IStateListener listener) {
        ArgumentChecker.required(listener, "listener");
        this._listeners.remove(listener);
    }

    @Override
    public T get() {
        return this._state;
    }

    @Override
    public void shift(String operationType) {
        SimpleOperation operation = new SimpleOperation(operationType);
        shift(operation);
    }

    @Override
    public void shift(IOperation operation) {
        ArgumentChecker.required(operation, "operation");
        this._lock.lock();
        T newState;
        T temporaryState;
        T oldState = this._state;
        try {
            if (this._checker != null) {
                temporaryState = this._checker.check(this._state, operation);
                if (temporaryState != null) {
                    this._state = temporaryState;
                } else {
                    throw new GeneralException(
                            "The operation {} cannot be applied on current state - {}",
                            operation.type(), this._state);
                }
            }

            newState = this._shifter.shift(this._state, operation);

            if (newState == null) {
                throw new GeneralException("The shifter does not return a valid state - {}", this._shifter);
            }

            this._state = newState;
        } catch (Exception ex) {
            this._state = oldState;
            throw ex;
        } finally {
            this._lock.unlock();
        }

        if (! oldState.equals(newState)) {
            Looper.on(this._listeners).foreach(listener -> listener.stateChanged(oldState, newState));
        }
    }
}