/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.exception;

import uapi.GeneralException;
import uapi.InvalidArgumentException;
import uapi.common.Builder;

import java.util.Map;

/**
 * The builder is used to create initialize and create exception instance
 */
public abstract class ExceptionBuilder<E extends ParameterizedException, B extends ExceptionBuilder>
        extends Builder<E> {

    private int _errCode = -1;
    private int _category = -1;
    private final ExceptionErrors<E> _errors;
    private Object[] _indexedParams;
    private Map _namedParams;

    public ExceptionBuilder(final int category, final ExceptionErrors<E> errors) {
        if (category < 0) {
            throw new GeneralException("The exception category cant be negative");
        }
        if (errors == null) {
            throw new GeneralException("The ExceptionErrors is not specified");
        }
        this._category = category;
        this._errors = errors;
    }

    public int category() {
        return this._category;
    }

    public ExceptionErrors<E> errors() {
        return this._errors;
    }

    public B errorCode(int errorCode) {
        this._errCode = errorCode;
        return (B) this;
    }

    public int errorCode() {
        return this._errCode;
    }

    public B variables(Object... vars) {
        this._indexedParams = vars;
        return (B) this;
    }

    public B variables(Map vars) {
        this._namedParams = vars;
        return (B) this;
    }

    public B variables(IParameters vars) {
        Object v = vars.get();
        if (v instanceof Object[]) {
            this._indexedParams = (Object[]) v;
        } else if (v instanceof Map) {
            this._namedParams = (Map) v;
        } else {
            throw new GeneralException("Unsupported variables type - {}", v.getClass().getCanonicalName());
        }
        return (B) this;
    }

    public Object[] indexedParameters() {
        return this._indexedParams;
    }

    public Map namedParameters() {
        return this._namedParams;
    }

    @Override
    protected void validate() throws InvalidArgumentException {
        if (this._category == -1) {
            throw new InvalidArgumentException("The category must be provider");
        }
        if (this._errCode == -1) {
            throw new InvalidArgumentException("The error code must be provider");
        }
    }

    @Override
    protected void beforeCreateInstance() {
        // do nothing
    }

    @Override
    protected void afterCreateInstance() {
        // do nothing
    }
}
