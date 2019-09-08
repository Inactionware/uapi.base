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
 * A IndexedForeachReducer iterate all items by specific functionality with item index
 */
public class IndexedForeachReducer<T> extends Reducer<T> {

    private final IndexedAction<T> _action;

    IndexedForeachReducer(Mapper<T> previously, final IndexedAction<T> action) {
        super(previously);
        ArgumentChecker.required(action, "action");
        this._action = action;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem() {
        var hasItem = hasItem();
        var index = 0;
        while (hasItem) {
            var item = (T) getPreviously().getItem();
            this._action.accept(index, item);
            hasItem = hasItem();
            index++;
        }
        return null;
    }
}
