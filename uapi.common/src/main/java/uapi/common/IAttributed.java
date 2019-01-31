/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import java.util.Map;

/**
 * An class implement this interface will have one or more attributes
 */
public interface IAttributed {

    /**
     * Get attribute value by specific attribute key
     *
     * @param   key
     *          The attribute key which associated with attribute value
     * @return  The attribute value or null if no attribute value associated with the key
     */
    <T> T get(Object key);

    /**
     * Check this object contains specific attribute key and value
     *
     * @param   key
     *          The attribute key which will be checked
     * @param   value
     *          The attribute value which will be checked
     * @return  True means this object contains specific attribute otherwise return false
     */
    boolean contains(Object key, Object value);

    /**
     * Check this object contains specific attributes
     *
     * @param   attributes
     *          The attributes map
     * @return  True means this object contains specific attributes otherwise return false
     */
    boolean contains(Map<Object, Object> attributes);

    int count();
}
