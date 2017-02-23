/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import uapi.ExceptionErrors;

/**
 * The class define common errors
 */
public class CommonErrors extends ExceptionErrors<CommonException> {

    public static final int CATEGORY   = 0x0001;

    public static final int INVALID_ARGUMENT    = 1;

    static {
        mapCodeKey(INVALID_ARGUMENT, "InvalidArgument");
    }

    @Override
    protected String getPropertiesFile(CommonException exception) {
        if (exception.category() == CATEGORY) {
            return "commonErrors.properties";
        }
        return null;
    }
}
