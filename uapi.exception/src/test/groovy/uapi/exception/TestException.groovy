/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.exception

/**
 * Created by xquan on 3/1/2017.
 */
class TestException extends ParameterizedException {

    public static TestExceptionBuilder builder() {
        return new TestExceptionBuilder();
    }

    protected TestException(final TestExceptionBuilder builder) {
        super(builder);
    }

    public static class TestExceptionBuilder
            extends ExceptionBuilder<TestException, TestExceptionBuilder> {

        TestExceptionBuilder() {
            super(TestErrors.CATEGORY, new TestErrors());
        }

        @Override
        protected TestException createInstance() {
            return new TestException(this);
        }
    }
}
