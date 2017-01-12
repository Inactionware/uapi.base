/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

import uapi.common.ArgumentChecker;

/**
 * The FlatMapMapper will generate more then one element on input element
 */
class FlatMapMapper<I, T> extends Mapper<T> {

    private final ConvertMore<I, T> _converter;
    private Mapper<T> _currently;

    FlatMapMapper(Mapper<I> previously, ConvertMore<I, T> converter) {
        super(previously);
        ArgumentChecker.required(converter, "converter");
        this._converter = converter;
    }

    @Override
    public boolean hasItem() {
        boolean hasItem = false;
        if (this._currently != null) {
            hasItem = this._currently.hasItem();
        }
        if (hasItem) {
            return true;
        }
        return super.hasItem();
    }

    @Override
    public T getItem() {
        if (! hasItem()) {
            return null;
        }
        if (this._currently != null && this._currently.hasItem()) {
            return this._currently.getItem();
        }
        while (super.hasItem()) {
            I item = (I) getPreviously().getItem();
            this._currently = (Mapper<T>) this._converter.accept(item);
            if (this._currently.hasItem()) {
                return this._currently.getItem();
            }
        }
        throw new NoItemException();
    }
}
