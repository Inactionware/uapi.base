/*
 * Copyright (C) 2017 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi;

/**
 * A general exception, all customized exception should extends it
 */
public class GeneralException extends RuntimeException {

    private final String    _msg;
    private final Object[]  _args;

    public GeneralException(
            final String message,
            final Object... arguments
    ) {
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
        return null;
    }
}
