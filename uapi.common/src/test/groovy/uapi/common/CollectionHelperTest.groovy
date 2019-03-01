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
 * Test case for CollectionHelper
 */
class CollectionHelperTest extends Specification {

    def 'Test hasNull method on array'() {
        expect:
        CollectionHelper.hasNull(array) == hasNull

        where:
        array                           | hasNull
        ["1", "2", "3"] as String[]     | false
        [null, "2", "3"] as String[]    | true
        ["1", "2", null] as String[]    | true
        ["1", null, "3"] as String[]    | true
        [null, null, null] as String[]  | true
    }

    def 'Test hasNull method on collection'() {
        expect:
        CollectionHelper.hasNull(array) == hasNull

        where:
        array               | hasNull
        ["1", "2", "3"]     | false
        [null, "2", "3"]    | true
        ["1", "2", null]    | true
        ["1", null, "3"]    | true
        [null, null, null]  | true
    }

    def 'Test isContains method on collection'() {
        expect:
        CollectionHelper.isContains(collection, item1, item2) == isContains

        where:
        collection      | item1                 | item2     | isContains
        ["1", "2"]      | new String("1")       | "3"       | true
        ["1", "2"]      | "3"                   | "4"       | false
    }

    def 'Test isContains method on array'() {
        expect:
        CollectionHelper.isContains(array, item1, item2) == isContains

        where:
        array                   | item1                 | item2     | isContains
        ["1", "2"] as String[]  | new String("1")       | "3"       | true
        ["1", "2"] as String[]  | "3"                   | "4"       | false
    }

    def 'Test isStrictContains method on collection'() {
        expect:
        CollectionHelper.isStrictContains(collection, item1, item2) == result

        where:
        collection      | item1                 | item2                 | result
        ["1", "2"]      | new String("1")       | new String("2")       | false
        ["1", "2"]      | "1"                   | "2"                   | true
    }

    def 'Test isStrictContains method on array'() {
        expect:
        CollectionHelper.isStrictContains(array, item1, item2) == result

        where:
        array                   | item1                 | item2                 | result
        ["1", "2"] as String[]  | new String("1")       | new String("2")       | false
        ["1", "2"] as String[]  | "1"                   | "2"                   | true
    }

    def 'Test contains method on collection'() {
        expect:
        CollectionHelper.contains(collection, item1, item2) == result

        where:
        collection      | item1                 | item2     | result
        ["1", "2"]      | new String("1")       | "3"       | "1"
        ["1", "2"]      | "3"                   | "4"       | null
    }

    def 'Test contains method on array'() {
        expect:
        CollectionHelper.contains(array, item1, item2) == result

        where:
        array                   | item1                 | item2     | result
        ["1", "2"] as String[]  | new String("1")       | "3"       | "1"
        ["1", "2"] as String[]  | "3"                   | "4"       | null
    }

    def 'Test strictContains method on collection'() {
        expect:
        CollectionHelper.strictContains(collection, item1, item2) == result

        where:
        collection      | item1                 | item2                 | result
        ["1", "2"]      | new String("1")       | new String("2")       | null
        ["1", "2"]      | "1"                   | "2"                   | "1"
    }

    def 'Test strictContains method on array'() {
        expect:
        CollectionHelper.strictContains(array, item1, item2) == result

        where:
        array                   | item1                 | item2                 | result
        ["1", "2"] as String[]  | new String("1")       | new String("2")       | null
        ["1", "2"] as String[]  | "1"                   | "2"                   | "1"
    }

    def 'Test asString method on collection'() {
        expect:
        CollectionHelper.asString(collection) == result

        where:
        collection              | result
        ["1", "2"]              | "1,2"
        ["1", null]             | "1,"
    }

    def 'Test isContains on collection'() {
        expect:
        CollectionHelper.isContainsAll(collection, elements) == result

        where:
        collection          | elements                      | result
        ["1", "2", "3"]     | ["1", "2", "3"] as String[]   | true
        ["1", "2", "3"]     | ["1", "2"] as String[]        | true
        ["1", "2", "3"]     | ["1", "4"] as String[]        | false
    }

    def 'Test isContains on array'() {
        expect:
        CollectionHelper.isContainsAll(array, elements) == result

        where:
        array                       | elements                      | result
        ['1', '2', '3'] as String[] | ['1', '2', '3'] as String[]   | true
        ['1', '2', '3'] as String[] | ['1', '3'] as String[]        | true
        ['1', '2', '3'] as String[] | ['5', '3'] as String[]        | false
    }

    def 'Test removeDuplicate'() {
        when:
        CollectionHelper.removeDuplicate(list)

        then:
        list == expectList

        where:
        list            | expectList
        ['1', '2', '1'] | ['1', '2']
        [1, 2, 1]       | [1, 2]
    }

    def 'Test equals set'() {
        expect:
        CollectionHelper.equals(set1, set2) == result

        where:
        set1                | set2                      | result
        null                | ['1'] as Set              | false
        ['1'] as Set        | null                      | false
        ["1", "2"] as Set   | ["1", "2"] as Set         | true
        ["2", "1"] as Set   | ["1", "2"] as Set         | true
        ["1", "2"] as Set   | ["1", "2", "3"] as Set    | false
    }

    def 'Test equals on array'() {
        expect:
        CollectionHelper.equals(a1, a2) == result

        where:
        a1                      | a2                        | result
        null                    | ['1'] as String[]         | false
        ['1'] as String[]       | null                      | false
        ['1', '2'] as String[]  | ['1', '2'] as String[]    | true
        ['2', '1'] as String[]  | ['1', '2'] as String[]    | false
        [1, 2] as Integer[]     | [1, 2] as Integer[]           | true
        [2, 1] as Integer[]     | [1, 2] as Integer[]           | false
        ['1', 2] as Object[]    | ['1', 2] as Object[]      | true
        [1, 2] as Object[]      | ['1', 2] as Object[]      | false
    }

    def 'Test array as string'() {
        expect:
        CollectionHelper.asString(array, sep) == result

        where:
        array                           | sep   | result
        [1, 2, 3] as Integer[]          | ','   | '1,2,3'
        [1, 2, 3] as Integer[]          | ', '  | '1, 2, 3'
        ['1', null, '3'] as String[]    | ', '  | '1, , 3'
    }

    def 'Test array as string with default sep'() {
        expect:
        CollectionHelper.asString(array) == result

        where:
        array                   | result
        [1, 2, 3] as Integer[]  | '1,2,3'
    }

    def 'Test create new object array'() {
        expect:
        CollectionHelper.newObjectArray('1', 2) == ['1', 2] as Object[]
        CollectionHelper.newObjectArray() == [] as Object[]
    }
}