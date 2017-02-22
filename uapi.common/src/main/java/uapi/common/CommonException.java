/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import uapi.ExceptionCategory;
import uapi.PropertiedException;

/**
 * Root exception for common package
 */
public class CommonException extends PropertiedException {

    public static CommonExceptionBuilder builder() {
        return new CommonExceptionBuilder();
    }

    private CommonException(final ExceptionBuilder builder) {
        super(builder);
    }

    public static class CommonExceptionBuilder extends ExceptionBuilder<CommonException, CommonExceptionBuilder> {

        CommonExceptionBuilder() {
            super(ExceptionCategory.COMMON);
        }

        @Override
        protected CommonException createInstance() {
            return new CommonException(this);
        }
    }
}
