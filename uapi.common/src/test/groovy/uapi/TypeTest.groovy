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
 * Unit test for Type
 */
class TypeTest extends Specification {

    def 'Test native type to qualified type'() {
        expect:
        Type.toQType(nType) == qType

        where:
        nType           | qType
        Type.VOID       | Type.Q_VOID
        Type.BOOLEAN    | Type.Q_BOOLEAN
        Type.INTEGER    | Type.Q_INTEGER
        Type.LONG       | Type.Q_LONG
        Type.SHORT      | Type.Q_SHORT
        Type.FLOAT      | Type.Q_FLOAT
        Type.DOUBLE     | Type.Q_DOUBLE
        'Test'          | 'Test'
    }

    def 'Test qualified type to native type'() {
        expect:
        Type.toNType(qType) == nType

        where:
        nType           | qType
        Type.VOID       | Type.Q_VOID
        Type.BOOLEAN    | Type.Q_BOOLEAN
        Type.INTEGER    | Type.Q_INTEGER
        Type.LONG       | Type.Q_LONG
        Type.SHORT      | Type.Q_SHORT
        Type.FLOAT      | Type.Q_FLOAT
        Type.DOUBLE     | Type.Q_DOUBLE
        'ABC'           | 'ABC'
    }
}
