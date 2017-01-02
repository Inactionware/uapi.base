/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

/**
 * The interface indicate the class has one or more attributes and all
 * attribute is associated with a key.
 * Using key can receive the attribute.
 */
public interface IAttributed {

    /**
     * Get attribute by specific key
     *
     * @param   key
     *          The key which associated a attribute
     * @param   <T>
     *          The attribute type
     * @return  The attribute or null if no attribute is associated with the key
     */
    <T> T get(Object key);
}
