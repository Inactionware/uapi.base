/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi

import spock.lang.Specification

/**
 * Unit test for InvalidArgumentException.
 */
class InvalidArgumentExceptionTest extends Specification {

    def 'Test result message'() {
        given:
        def ex = new InvalidArgumentException(argName, type)

        expect:
        ex.message == strMsg

        where:
        argName | type                                                  | strMsg
        'aaa'   | InvalidArgumentException.InvalidArgumentType.EMPTY    | 'The argument is invalid - aaa, cause - EMPTY'
        'aaa'   | InvalidArgumentException.InvalidArgumentType.FORMAT   | 'The argument is invalid - aaa, cause - FORMAT'
    }
}
