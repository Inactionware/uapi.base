/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission on the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

import uapi.common.ArgumentChecker;
import uapi.common.Functionals;

/**
 * A ForeachReducer iterate all items by specific functionality
 */
class ForeachReducer<T> extends Reducer<T> {

    private final Functionals.Action _action;

    ForeachReducer(Mapper previously, final Functionals.Action action) {
        super(previously);
        ArgumentChecker.required(action, "action");
        this._action = action;
    }

    @Override
    public T getItem() {
        try {
            boolean hasItem = hasItem();
            while (hasItem) {
                Object item = getPreviously().getItem();
                this._action.accept(item);
                hasItem = hasItem();
            }
        } catch (NoItemException ex) {
            // do nothing
        }
        return null;
    }
}
