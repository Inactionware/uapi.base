package uapi.common

import spock.lang.Ignore
import spock.lang.Specification
import uapi.ExceptionErrors
import uapi.GeneralException
import uapi.InvalidArgumentException
import uapi.PropertiedException

/**
 * Unit test for CommonException
 */
class CommonExceptionTest extends Specification {
    
    def 'Test create instance'() {
        when:
        def ex = CommonException.builder()
                .errorCode(CommonErrors.INVALID_ARGUMENT)
                .variables(new CommonErrors.InvalidArgumentVariableBuilder()
                        .argumentName('test')
                        .build())
                .build()

        then:
        ex != null
        ex.message == 'The argument is invalid - test'
    }

    def 'Test create instance with conflict category'() {
        when:
        CommonException.builder().errorCode(CommonErrors.INVALID_ARGUMENT).build()
        TestException.builder().errorCode(CommonErrors.INVALID_ARGUMENT).build()

        then:
        thrown(GeneralException)
    }

    def 'Test create instance with sub exception class'() {
        when:
        CommonException.builder().errorCode(CommonErrors.INVALID_ARGUMENT).build()
        ConcreteCommonException.builder().errorCode(CommonErrors.INVALID_ARGUMENT).build()

        then:
        noExceptionThrown()
    }

    def 'Test create instance with incorrect error code'() {
        when:
        CommonException.builder().errorCode(2).build()

        then:
        thrown(InvalidArgumentException)
    }

    static class TestException extends PropertiedException {

        public static TestExceptionBuilder builder() {
            return new TestExceptionBuilder(new TestErrors())
        }

        protected TestException(TestExceptionBuilder builder) {
            super(builder)
        }
    }

    static class TestExceptionBuilder extends PropertiedException.ExceptionBuilder<TestException, TestExceptionBuilder> {

        TestExceptionBuilder(ExceptionErrors errors) {
            super(CommonErrors.CATEGORY, errors)
        }

        @Override
        protected TestException createInstance() {
            return new TestException(this)
        }
    }

    static class TestErrors extends ExceptionErrors<TestException> {

        static {
            mapCodeKey(1, 'aaa')
        }

        @Override
        protected String getPropertiesFile(TestException exception) {
            return "/commonError.properties"
        }
    }

    static class ConcreteCommonException extends CommonException {

        @Override
        static ConcreteCommonExceptionBuilder builder() {
            return new ConcreteCommonExceptionBuilder()
        }

        protected ConcreteCommonException(ConcreteCommonExceptionBuilder builder) {
            super(builder)
        }
    }

    static class ConcreteCommonExceptionBuilder extends CommonException.CommonExceptionBuilder {

        @Override
        protected ConcreteCommonException createInstance() {
            return new ConcreteCommonException(this);
        }
    }
}
