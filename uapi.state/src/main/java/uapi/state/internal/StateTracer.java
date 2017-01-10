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
import uapi.state.IOperation;
import uapi.state.IShifter;
import uapi.state.IStateTracer;
import uapi.state.IStateListener;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The implementation of IStateTracer interface
 */
public final class StateTracer<T> implements IStateTracer<T> {

    private T _state;
    private IShifter<T> _shifter;

    private final List<IStateListener<T>> _listeners = new LinkedList<>();
    private final Lock _lock = new ReentrantLock();

    public StateTracer(final IShifter<T> shifter, final T initState) {
        ArgumentChecker.required(shifter, "shifter");
        ArgumentChecker.notNull(initState, "initState");

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
        SimpleOperation operatioin = new SimpleOperation(operationType);
        shift(operatioin);
    }

    @Override
    public void shift(IOperation operation) {
        ArgumentChecker.required(operation, "operation");
        this._lock.lock();
        T newState;
        try {
            newState = this._shifter.shift(this._state, operation);
        } finally {
            this._lock.unlock();
        }
        if (newState == null) {
            throw new GeneralException("The shifter does not return a valid state - {}", this._shifter);
        }
        T oldState = this._state;
        this._state = newState;
        if (! newState.equals(oldState)) {
            Looper.on(this._listeners).foreach(listener -> listener.stateChanged(oldState, newState));
        }
    }
}