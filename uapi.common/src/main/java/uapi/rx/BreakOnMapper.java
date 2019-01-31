/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.rx;

import uapi.common.ArgumentChecker;
import uapi.common.Functionals;

class BreakOnMapper<T> extends Mapper<T> {

    private final Functionals.Validator<T> _validator;
    private boolean _terminated = false;
    private boolean _returnFailedItem = false;

    BreakOnMapper(
            final Mapper<T> previously,
            final Functionals.Validator<T> validator
    ) {
        this(previously, validator, false);
    }

    BreakOnMapper(
            final Mapper<T> previously,
            final Functionals.Validator<T> validator,
            final boolean returnFailedItem
    ) {
        super(previously);
        ArgumentChecker.required(validator, "validator");
        this._validator = validator;
        this._returnFailedItem = returnFailedItem;
    }

    @Override
    public boolean hasItem() {
        if (this._terminated) {
            return false;
        }
        return super.hasItem();
    }

    @Override
    public T getItem() throws NoItemException {
        T item = ((Mapper<T>) getPreviously()).getItem();
        if (this._validator.accept(item)) {
            this._terminated = true;
            if (this._returnFailedItem) {
                return item;
            } else {
                throw new NoItemException();
            }
        } else {
            return item;
        }
    }
}
