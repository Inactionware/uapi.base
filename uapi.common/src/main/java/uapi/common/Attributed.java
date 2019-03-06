/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.common;

import uapi.rx.Looper;

import java.util.HashMap;
import java.util.Map;

public class Attributed implements IAttributed {

    public static Attributed apply(Functionals.Apply apply) {
        Attributed attr = new Attributed();
        apply.accept(attr);
        return attr;
    }

    private final Map<Object, Object> _attributes;

    protected Attributed() {
        this._attributes = new HashMap<>();
    }

    /**
     * Set attribute by specified key, return old attribute when the key has associated with a attribute
     *
     * @param   key
     *          The attribute key
     * @param   attribute
     *          The attribute
     * @return  Old attribute or null
     */
    public Object set(Object key, Object attribute) {
        ArgumentChecker.required(key, "key");
        return this._attributes.put(key, attribute);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        ArgumentChecker.required(key, "key");
        return (T) this._attributes.get(key);
    }

    @Override
    public boolean contains(Object key, Object value) {
        Object thisValue = this._attributes.get(key);
        if (thisValue == null) {
            return false;
        }
        return thisValue.equals(value);
    }

    @Override
    public boolean contains(Map<Object, Object> map) {
        if (map == null || map.size() == 0) {
            return true;
        }
        int matchedCount = Looper.on(map.entrySet())
                .filter(entry -> this._attributes.containsKey(entry.getKey()))
                .filter(entry -> entry.getValue().equals(this._attributes.get(entry.getKey())))
                .count();
        return matchedCount == map.size();
    }

    @Override
    public int count() {
        return this._attributes.size();
    }
}
