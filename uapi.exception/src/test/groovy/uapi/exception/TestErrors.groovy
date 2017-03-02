/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.exception

import uapi.common.MapHelper

/**
 * Created by xquan on 3/1/2017.
 */
class TestErrors extends FileBasedExceptionErrors<TestException> {

    public static final int CATEGORY   = 0x0001;

    public static final int INVALID_ARGUMENT    = 1;
//
//    static {
//        mapCodeKey(INVALID_ARGUMENT, "InvalidArgument");
//    }

    @Override
    protected String getFile(TestException exception) {
        if (exception.category() == CATEGORY) {
            return "/testErrors.properties";
        }
        return null;
    }

    @Override
    protected String getKey(TestException exception) {
        return 'InvalidArgument'
    }

    public static final class InvalidArgumentVariables extends NamedParameters {

        private static final String ARG_NAME    = "argumentName";

        private String _argName;

        public InvalidArgumentVariables argumentName(String argumentName) {
            this._argName = argumentName;
            return this;
        }

        @Override
        public Map<Object, Object> get() {
            return MapHelper.newMap().put(ARG_NAME, this._argName).get();
        }
    }
}
