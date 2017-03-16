/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common

import spock.lang.Specification

/**
 * Unit test for Range
 */
class RangeTest extends Specification {

    def 'Test create instance'() {
        given:
        def range = Range.from(start).to(end)

        when:
        def pos = start

        then:
        for (int r : range) {
            r == pos++
        }

        where:
        start   | end
        0       | 10
        -12     | 12
    }
}
