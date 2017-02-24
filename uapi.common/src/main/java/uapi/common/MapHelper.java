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

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for Map
 */
public class MapHelper {

    private MapHelper() { }

    /**
     * Find the specified map is contains anyone of specified keys
     *
     * @param   map
     *          The specified map
     * @param   keys
     *          The keys
     * @param   <T>
     *          The key type
     * @return  The first matched key will be returned otherwise return null
     */
    public static <T> T findKey(Map map, T... keys) {
        ArgumentChecker.required(map, "map");
        ArgumentChecker.required(keys, "keys");

        return Looper.on(keys)
                .filter(map::containsKey)
                .first(null);
    }

    /**
     * Convert a map to a string presentation
     *
     * @param   map
     *          The map
     * @return  String presentation for the map
     */
    public static String asString(Map<?, ?> map) {
        ArgumentChecker.required(map, "map");

        StringBuilder buffer = new StringBuilder();
        buffer.append("{");
        Looper.on(map.entrySet())
                .foreach(entry -> {
                    buffer.append(entry.getKey().toString()).append("=");
                    if (entry.getValue() instanceof Map) {
                        buffer.append(asString((Map) entry.getValue())).append(",");
                    } else {
                        buffer.append(entry.getValue()).append(",");
                    }
                });
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.lastIndexOf(","));
        }
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * Create new map builder
     *
     * @return  A map builder
     */
    public static MapBuilder newMap() {
        return new MapBuilder();
    }

    /**
     * A map builder
     */
    public static class MapBuilder {

        private final Map<Object, Object> _map = new HashMap();

        MapBuilder put(Object key, Object value) {
            this._map.put(key, value);
            return this;
        }

        Map<Object, Object> get() {
            return this._map;
        }
    }
}
