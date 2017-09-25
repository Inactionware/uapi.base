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

import java.util.Iterator;

/**
 * The data source which can iterate item on Iterator
 */
class IteratorMapper<T> extends Mapper<T> {

    private final Iterator<T> _itemsIte;

    IteratorMapper(Iterator<T> iterator) {
        ArgumentChecker.required(iterator, "iterator");
        this._itemsIte = iterator;
    }

    @Override
    public boolean hasItem() {
        return this._itemsIte.hasNext();
    }

    @Override
    public T getItem() {
        if (hasItem()) {
            return this._itemsIte.next();
        }
        throw new NoItemException();
    }

    @Override
    public void end() {
        // do nothing
    }
}
