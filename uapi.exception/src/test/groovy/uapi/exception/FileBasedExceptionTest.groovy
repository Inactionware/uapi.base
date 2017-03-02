/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.exception

import spock.lang.Ignore
import spock.lang.Specification
import uapi.GeneralException
import uapi.InvalidArgumentException

/**
 * Unit test for file based exception
 */
class FileBasedExceptionTest extends Specification {

    def 'Test create instance'() {
        when:
        def ex = TestException.builder()
                .errorCode(TestErrors.INVALID_ARGUMENT)
                .variables(new TestErrors.InvalidArgumentVariables()
                    .argumentName('test')
                    .get())
                .build()

        then:
        ex != null
        ex.message == 'The argument is invalid - test'
    }

    def 'Test create instance with conflict category'() {
        when:
        TestException.builder().errorCode(TestErrors.INVALID_ARGUMENT).build()
        TestException2.builder().errorCode(TestErrors.INVALID_ARGUMENT).build()

        then:
        thrown(GeneralException)
    }

    def 'Test create instance with sub exception class'() {
        when:
        TestException.builder().errorCode(TestErrors.INVALID_ARGUMENT).build()
        ConcreteCommonException.builder().errorCode(TestErrors.INVALID_ARGUMENT).build()

        then:
        noExceptionThrown()
    }

    @Ignore
    def 'Test create instance with incorrect error code'() {
        when:
        TestException.builder().errorCode(2).build()

        then:
        thrown(InvalidArgumentException)
    }

    static class TestException2 extends ParameterizedException {

        public static TestExceptionBuilder2 builder() {
            return new TestExceptionBuilder2(new TestErrors2())
        }

        protected TestException2(TestExceptionBuilder2 builder) {
            super(builder)
        }
    }

    static class TestExceptionBuilder2 extends ExceptionBuilder<TestException2, TestExceptionBuilder2> {

        TestExceptionBuilder2(ExceptionErrors errors) {
            super(uapi.exception.TestErrors.CATEGORY, errors)
        }

        @Override
        protected TestException2 createInstance() {
            return new TestException2(this)
        }
    }

    static class TestErrors2 extends FileBasedExceptionErrors<TestException2> {

        static {
            mapCodeKey(100, 'aaa')
        }

        @Override
        protected String getFile(TestException2 exception) {
            return "/testError.properties"
        }
    }

    static class ConcreteCommonException extends TestException {

        @Override
        static ConcreteCommonExceptionBuilder builder() {
            return new ConcreteCommonExceptionBuilder()
        }

        protected ConcreteCommonException(ConcreteCommonExceptionBuilder builder) {
            super(builder)
        }
    }

    static class ConcreteCommonExceptionBuilder extends TestException.TestExceptionBuilder {

        @Override
        protected ConcreteCommonException createInstance() {
            return new ConcreteCommonException(this);
        }
    }
}
