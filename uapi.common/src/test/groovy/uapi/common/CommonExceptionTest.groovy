package uapi.common

import spock.lang.Specification

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
}
