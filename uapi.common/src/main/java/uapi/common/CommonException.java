/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import uapi.PropertiedException;

/**
 * Created by xquan on 2/22/2017.
 */
public class CommonException extends PropertiedException {

    private CommonException(final ExceptionBuilder builder) {
        super(builder);
    }
}
