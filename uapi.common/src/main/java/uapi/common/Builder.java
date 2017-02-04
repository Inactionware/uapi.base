/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import uapi.InvalidArgumentException;
import uapi.GeneralException;

import java.util.HashMap;
import java.util.Map;

/**
 * A Builder used to initialize some properties, validate properties and create an
 * instance
 *
 * @param   <T>
 *          The type of built instance
 */
public abstract class Builder<T> {

    private boolean _built = false;

    /**
     * Build instance by currently properties setting
     *
     * @return  The instance
     * @throws  GeneralException
     *          Validation failed
     */
    public T build() throws GeneralException {
        ensureNotBuilt();
        validate();
        beforeCreateInstance();
        T obj = createInstance();
        afterCreateInstance();
        this._built = true;
        return obj;
    }

    /**
     * Validate all properties of the builder to ensure they are valid
     *
     * @throws  InvalidArgumentException
     *          If one of properties is invalid
     */
    protected abstract void validate() throws InvalidArgumentException;

    /**
     * Invoke it before create instance
     */
    protected abstract void beforeCreateInstance();

    /**
     * Invoke it after create instance
     */
    protected abstract void afterCreateInstance();

    /**
     * Create instance based on properties settings
     *
     * @return  The instance
     */
    protected abstract T createInstance();

    /**
     * Ensure the instance is built, if the instance is not built then a GeneralException will be thrown.
     */
    protected void ensureBuilt() {
        if (! this._built) {
            throw new GeneralException("The builder is not built - {}", this);
        }
    }

    /**
     * Ensure the instance is not built, if the instance is built then a GeneralException will be thrown.
     */
    protected void ensureNotBuilt() {
        if (this._built) {
            throw new GeneralException("The builder is already built - {}", this);
        }
    }
}
