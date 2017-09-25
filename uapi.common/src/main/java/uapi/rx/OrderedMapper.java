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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The source hold item list and a position to point to last read position.
 */
class OrderedMapper<T> extends Mapper<T> {

    private final List<T> _items;
    private int _pos = -1;

    OrderedMapper(final List<T> items) {
        ArgumentChecker.required(items, "items");
        this._items = items;
    }

    OrderedMapper(final T item) {
        ArgumentChecker.required(item, "items");
        this._items = new ArrayList<>(1);
        this._items.add(item);
    }

    OrderedMapper(final T... items) {
        ArgumentChecker.required(items, "items");
        this._items = Arrays.asList(items);
    }

    @Override
    public boolean hasItem() {
        return this._pos < this._items.size() - 1;
    }

    @Override
    public T getItem() {
        if (hasItem()) {
            this._pos++;
            return this._items.get(this._pos);
        }
        throw new NoItemException();
    }

    @Override
    public void end() {
        this._pos = -1;
    }
}
