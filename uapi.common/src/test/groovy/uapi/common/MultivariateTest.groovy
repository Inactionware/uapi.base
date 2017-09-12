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
import uapi.InvalidArgumentException

/**
 * Unit test for Multivariate
 */
class MultivariateTest extends Specification {

    def 'Test create instance'() {
        when:
        new Multivariate(count)

        then:
        noExceptionThrown()

        where:
        count               | placeholder
        1                   | null
        10                  | null
        10000               | null
    }

    def 'Test put and get item'() {
        when:
        def inst = new Multivariate(3)
        inst.put(0, first)
        inst.put(1, second)
        inst.put(2, third)

        then:
        inst.get(0) == first
        inst.get(1) == second
        inst.get(2) == third

        where:
        first   | second    | third
        '1'     | '2'       | '3'
        '1'     | null      | '2'
    }

    def 'Test put item by incorrect index'() {
        when:
        def inst = new Multivariate(3)
        inst.put(index, null)

        then:
        thrown(InvalidArgumentException)

        where:
        index   | plactholder
        -1      | null
        3       | null
        10      | null
        -3      | null
    }

    def 'Test get item by incorrect index'() {
        when:
        def inst = new Multivariate(3)
        inst.get(index)

        then:
        thrown(InvalidArgumentException)

        where:
        index   | plactholder
        -1      | null
        3       | null
        10      | null
        -3      | null
    }

    def 'Test remove item'() {
        when:
        def inst = new Multivariate(3)
        inst.put(2, 'sss')

        then:
        inst.remove(2) == 'sss'
        inst.get(2) == null
    }

    def 'Test clear'() {
        when:
        def inst = new Multivariate(3)
        inst.put(0, '1')
        inst.put(2, '3')
        inst.clear()

        then:
        inst.get(0) == null
        inst.get(1) == null
        inst.get(2) == null
    }

    def 'Test has value'() {
        when:
        def inst = new Multivariate(2)
        def inst2 = new Multivariate(1)
        inst2.put(0, 'a')

        then:
        ! inst.hasValue()
        inst2.hasValue()
    }

    def 'Test has value by index'() {
        when:
        def inst = new Multivariate(2)
        inst.put(1, 'a')

        then:
        ! inst.hasValue(0)
        inst.hasValue(1)
    }
}
