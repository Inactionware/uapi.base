/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.state

import spock.lang.Specification

/**
 * Unit test for StateCreator
 */
class StateCreatorTest extends Specification {

    def 'test create'() {
        given:
        def shifter = Mock(IShifter)

        when:
        def state = StateCreator.createTracer(shifter, "init")

        then:
        state != null
        state.get() == "init"
    }
}
