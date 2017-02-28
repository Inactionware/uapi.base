/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.exception;

/**
 * The IndexedParameters hold all parameters which are sequent
 */
public abstract class IndexedParameters<T extends IndexedParameters> implements IParameters<Object[]> {

    private Object[] _vars;

    /**
     * Set one or more sequent parameters
     *
     * @param   vars
     *          The sequent parameters
     * @return  Itself
     */
    public T set(Object... vars) {
        this._vars = vars;
        return (T) this;
    }

    @Override
    public Object[] get() {
        return this._vars;
    }
}
