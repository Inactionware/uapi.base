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

class GeneralExceptionTest extends Specification {

    def 'Test create instance from throwable'() {
        given:
        def instance = new GeneralException(Mock(Throwable))

        expect:
        instance != null
    }

    def 'Test create instance from throwable and string'() {
        given:
        def instance = new GeneralException(Mock(Throwable), msg, args)

        expect:
        instance != null
        instance.message == strMsg

        where:
        msg         | args      | strMsg
        'ab {}'     | 'cd'      | 'ab cd'
    }
}