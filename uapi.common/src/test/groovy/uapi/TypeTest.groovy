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

    def 'Test to array type'() {
        expect:
        Type.toArrayType(itemType) == arrType

        where:
        itemType        | arrType
        String.class    | 'java.lang.String[]'
    }

    def 'Test to list type'() {
        expect:
        Type.toListType(itemType) == listType

        where:
        itemType        | listType
        String.class    | 'java.util.List<java.lang.String>'
    }

    def 'Test to map type'() {
        expect:
        Type.toMapType(itemType) == mapType

        where:
        itemType        | mapType
        String.class    | 'java.util.Map<java.lang.String>'
    }

    def 'Test type is assignable'() {
        expect:
        Type.isAssignable(fromType, toType) == result

        where:
        fromType                | toType            | result
        int.class               | Integer.class     | true
        Integer.class           | int.class         | true
        Float.class             | float.class       | true
        int.class               | long.class        | false
        String.class            | Object.class      | true
        Object.class            | String.class      | false
        IIdentifiable.class     | IPartibleIdentify | false
        IPartibleIdentify.class | IIdentifiable     | true
    }
}
