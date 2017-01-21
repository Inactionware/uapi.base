/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

import uapi.common.ArgumentChecker;

/**
 * Implementation of IStream interface
 */
abstract class Stream<T> implements IStream<T> {

    private final IStream<?> _previously;

    Stream() {
        this._previously = null;
    }

    Stream(IStream<?> previously) {
        ArgumentChecker.required(previously, "previously");
        this._previously = previously;
    }

    IStream<?> getPreviously() {
        return this._previously;
    }

    @Override
    public boolean hasItem() {
        return this._previously != null && this._previously.hasItem();
    }

//    @Override
//    abstract T getItem() throws NoItemException;

    public void end() {
        getPreviously().end();
    }
}
