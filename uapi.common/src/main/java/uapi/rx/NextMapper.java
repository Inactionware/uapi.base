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
import uapi.common.Functionals;

/**
 * A NextMapper will do specific action on each input item
 */
class NextMapper<T> extends Mapper<T> {

    private final Functionals.Action<T> _action;

    NextMapper(Mapper<T> previously, final Functionals.Action<T> action) {
        super(previously);
        ArgumentChecker.required(action, "action");
        this._action = action;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem() throws NoItemException {
        T item = ((Mapper<T>) getPreviously()).getItem();
        this._action.accept(item);
        return item;
    }
}
