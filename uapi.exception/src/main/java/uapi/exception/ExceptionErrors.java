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
 * The ExceptionErrors is used to define various error which is related {@link ParameterizedException}
 */
public abstract class ExceptionErrors<E extends ParameterizedException> {

    /**
     * Get error message template string based on specific exception
     *
     * @param   exception
     *          The exception
     * @return  Return error message template or null if no such template
     */
    public abstract String getMessageTemplate(E exception);
}
