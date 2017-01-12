/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

import uapi.InvalidArgumentException;

/**
 * The LimitMapper will limit data by specific count setting
 */
class LimitMapper<T> extends Mapper<T> {

    private final int _limitCount;
    private int _count = 0;

    LimitMapper(Mapper<T> previously, int limitCount) {
        super(previously);
        if (limitCount < 0) {
            throw new InvalidArgumentException("The argument limitCount must not be a negative");
        }
        this._limitCount = limitCount;
    }

    @Override
    public boolean hasItem() {
        if (this._count >= this._limitCount) {
            return false;
        }
        return super.hasItem();
    }

    @Override
    public T getItem() {
        if (! hasItem()) {
            return null;
        }
        this._count++;
        return (T) getPreviously().getItem();
    }

    @Override
    public void end() {
        this._count = 0;
    }
}
