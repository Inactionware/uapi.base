/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import uapi.rx.Looper;

/**
 * The Multivariate hold multiple variables
 */
public class Multivariate {

    private final int _count;

    private final Object[] _items;

    public Multivariate(final int count) {
        ArgumentChecker.checkInt(count, "count", 1, Integer.MAX_VALUE);
        this._count = count;
        this._items = new Object[count];
    }

    public void put(final int index, final Object item) {
        ArgumentChecker.checkInt(index, "index", 0, this._count - 1);
        this._items[index] = item;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final int index) {
        ArgumentChecker.checkInt(index, "index", 0, this._count - 1);
        return (T) this._items[index];
    }

    @SuppressWarnings("unchecked")
    public <T> T remove(final int index) {
        ArgumentChecker.checkInt(index, "index", 0, this._count - 1);
        T item = (T) this._items[index];
        this._items[index] = null;
        return item;
    }

    public void clear() {
        Looper.on(this._items).foreachWithIndex((index, item) -> this._items[index] = null);
    }
}
