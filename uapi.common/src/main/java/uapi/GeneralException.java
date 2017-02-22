/*
 * Copyright (C) 2017 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi;

import uapi.common.StringHelper;

/**
 * A general exception, all customized exception should extends it
 */
public class GeneralException extends UapiException {

    private final String    _msg;
    private final Object[]  _args;

    public GeneralException() {
        super();
        this._msg = "";
        this._args = new Object[0];
    }

    public GeneralException(
            final String message,
            final Object... arguments
    ) {
        super();
        this._msg = message;
        this._args = arguments;
    }

    public GeneralException(
            final Throwable t
    ) {
        super(t);
        this._msg = t.getMessage();
        this._args = new Object[] {};
    }

    public GeneralException(
            final Throwable t,
            final String message,
            final Object... arguments
    ) {
        super(t);
        this._msg = message;
        this._args = arguments;
    }

    @Override
    public String getMessage() {
        return StringHelper.makeString(this._msg, this._args);
    }
}
