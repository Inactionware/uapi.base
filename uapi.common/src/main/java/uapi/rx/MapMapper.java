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
 * A MapMapper convert a input item to output item by specific functionality
 */
class MapMapper<I, T> extends Mapper<T> {

    private final Functionals.Convert<I, T> _converter;

    MapMapper(Mapper<I> previously, Functionals.Convert<I, T> converter) {
        super(previously);
        ArgumentChecker.required(converter, "converter");
        this._converter = converter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem() {
        var item = ((Mapper<I>) getPreviously()).getItem();
        if (item == null) {
            return null;
        }
        return this._converter.accept(item);
    }
}
