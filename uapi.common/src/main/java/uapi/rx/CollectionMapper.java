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

import java.util.*;

/**
 * A CollectionMapper hold data source locally, unlike OrderedMapper, it does not guarantee the items are ordered
 * which is depends on underlay implementation
 */
class CollectionMapper<T> extends Mapper<T> {

    private final Collection<T> _items;
    private Iterator<T> _itemsIte;

    CollectionMapper(Collection<T> items) {
        ArgumentChecker.required(items, "items");
        this._items = items;
        this._itemsIte = items.iterator();
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
        this._itemsIte = this._items.iterator();
    }
}
