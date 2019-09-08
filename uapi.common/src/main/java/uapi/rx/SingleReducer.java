/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

/**
 * The SingleReducer return only one item or throw an exception if no item can be returned
 */
class SingleReducer<T> extends Reducer<T> {

    private boolean _useDefault = false;
    private T _default = null;

    SingleReducer(Mapper previously) {
        super(previously);
    }

    SingleReducer(Mapper<T> previously, T defaultValue) {
        super(previously);
        this._useDefault = true;
        this._default = defaultValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem() throws NoItemException {
        var hasItem = hasItem();
        T item = null;
        var itemSet = false;
        while (hasItem) {
            if (itemSet) {
                throw new MoreItemException();
            }
            try {
                item = (T) getPreviously().getItem();
                itemSet = true;
            } catch (NoItemException ex) {
                if (this._useDefault) {
                    return this._default;
                } else {
                    throw ex;
                }
            }
            hasItem = hasItem();
        }

        if (itemSet) {
            return item;
        }
        if (this._useDefault) {
            return this._default;
        } else {
            throw new NoItemException();
        }
    }
}
