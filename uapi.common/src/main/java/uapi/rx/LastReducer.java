/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.rx;

public class LastReducer<T> extends Reducer<T> {

    private boolean _useDefault = false;
    private T _default = null;

    LastReducer(Mapper<T> previously) {
        super(previously);
    }

    LastReducer(Mapper<T> previously, T defaultValue) {
        super(previously);
        this._useDefault = true;
        this._default = defaultValue;
    }

    @Override
    public T getItem() {
        if (! hasItem()) {
            if (this._useDefault) {
                return this._default;
            } else {
                throw new NoItemException();
            }
        }
        boolean hasItem = hasItem();
        T item = null;
        try {
            while (hasItem) {
                item = (T) getPreviously().getItem();
                hasItem = hasItem();
            }
        } catch (NoItemException ex) {
            // do nothing
        }
        return item;
    }
}
