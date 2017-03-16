/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import java.util.Iterator;

/**
 * Define a range on integer
 */
public class Range implements Iterable {

    public static Range from(int start) {
        return new Range(start);
    }

    private final int _start;
    private int _end;

    private Range(int start) {
        this._start = start;
    }

    public Range to(int end) {
        this._end = end;
        return this;
    }

    public int start() {
        return this._start;
    }

    public int end() {
        return this._end;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            private final int _end = Range.this._end;

            private int _pos = Range.this._start;

            @Override
            public boolean hasNext() {
                return this._pos <= this._end;
            }

            @Override
            public Integer next() {
                return this._pos++;
            }
        };
    }
}
