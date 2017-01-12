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
 * The FilterMapper will filter out specific value which are not match specified logic
 */
class FilterMapper<T> extends Mapper<T> {

    private final Functionals.Filter<T> _filter;

    FilterMapper(Mapper<T> previously, final Functionals.Filter<T> filter) {
        super(previously);
        ArgumentChecker.required(filter, "filter");
        this._filter = filter;
    }

    @Override
    public T getItem() {
        if (! hasItem()) {
            return null;
        }
        T item = (T) getPreviously().getItem();
        while (true) {
            boolean matched = this._filter.accept(item);
            if (matched) {
                break;
            } else {
                if (! hasItem()) {
                    throw new NoItemException();
                }
                item = (T) getPreviously().getItem();
            }
        }
        return item;
    }
}
