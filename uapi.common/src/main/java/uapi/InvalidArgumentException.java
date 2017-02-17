/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi;

/**
 * The exception indicate the input argument is invalid
 */
public class InvalidArgumentException extends GeneralException {

    public InvalidArgumentException(String argumentName, InvalidArgumentType type) {
        this("The argument is invalid - {}, cause - {}", argumentName, type.name());
    }

    public InvalidArgumentException(String message, Object... args) {
        super(message, args);
    }

    public InvalidArgumentException(Throwable t) {
        super(t);
    }

    public enum InvalidArgumentType {

        EMPTY, FORMAT
    }
}
