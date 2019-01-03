/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.common

import spock.lang.Specification

class AttributedTest extends Specification {

    def 'Test create instance'() {
        when:
        def inst = new Attributed()

        then:
        noExceptionThrown()
        inst != null
    }

    def 'Test get value'() {
        given:
        def inst = new Attributed()
        def ov = inst.set(key, value)

        when:
        def nv = inst.get(key)

        then:
        ov == null
        nv == value

        where:
        key     | value
        'a'     | 'b'
    }

    def 'Test contains'() {
        given:
        def inst = new Attributed()
        def ov = inst.set(key, value)

        when:
        def isContains = inst.contains(key, value)

        then:
        ov == null
        isContains

        where:
        key     | value
        'a'     | 'b'
    }

    def 'Test contains multiple key-value'() {
        given:
        def inst = new Attributed()

        when:
        inst.set(k1, v1)
        inst.set(k2, v2)

        then:
        inst.contains([(k1): v1])
        inst.contains([(k2): v2])
        inst.contains([(k1): v1, (k2): v2])
        ! inst.contains([(k3): v3])
        ! inst.contains([(k1): v1, (k3): v3])
        ! inst.contains([(k1): v3])
        ! inst.contains([(k3): v1])

        where:
        k1  | v1    | k2    | v2    | k3    | v3
        'a' | 'A'   | 'b'   | 'B'   | 'c'   | 'C'
    }
}
