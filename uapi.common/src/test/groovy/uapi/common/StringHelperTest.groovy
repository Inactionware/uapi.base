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
import uapi.GeneralException
import uapi.InvalidArgumentException

/**
 * Unit test for StringHelper
 */
class StringHelperTest extends Specification{

    def 'Test make string by array'() {
        expect:
        StringHelper.makeString(msg, args) == expect

        where:
        msg                                         | args                                          | expect
        "Invalid String - {}"                       | ["argument"] as Object[]                      | "Invalid String - argument"
        "Invalid argument [{}] at class {}"         | ["test", "uapi.kernel.Class"] as Object[]     | "Invalid argument [test] at class uapi.kernel.Class"
        "{} is invalid"                             | ["test"] as Object[]                          | "test is invalid"
        "{ we are one team } the member are {}, {}" | ["a", "b"] as Object[]                        | "{ we are one team } the member are a, b"
        "{ we are one team, the member are {}, {}}" | ["a", "b"] as Object[]                        | "{ we are one team, the member are a, b}"
        "Invalid String - {0}"                      | ["argument"] as Object[]                      | "Invalid String - argument"
        "Test {0} is {1}"                           | ["test", "uapi.kernel.Class"] as Object[]     | "Test test is uapi.kernel.Class"
        "{0} is test"                               | ["a"] as Object[]                             | "a is test"
        "{1} index is not start on {}"              | ["test", "un-index", "0"] as Object[]         | "un-index index is not start on un-index"
        "abc {} tt {}"                              | [null, "dd"] as Object[]                      | "abc  tt dd"
        ''                                          | [] as Object[]                                | ''
        'abc {} tt {1'                              | [] as Object[]                                | 'abc {} tt {1'
        'abc {1a} tt'                               | [] as Object[]                                | 'abc {1a} tt'
        'abc {1a} tt'                               | null                                          | 'abc {1a} tt'
    }

    def 'Test make string by map'() {
        expect:
        StringHelper.makeString(strTemp, args) == expect

        where:
        strTemp                                     | args                                          | expect
        ''                                          | [a: 'b']                                      | ''
        'Invalid String - {name}'                   | [name: 'test']                                | 'Invalid String - test'
        'Invalid arg [{a}] at class {b}'            | [a: '11', b: '22']                            | 'Invalid arg [11] at class 22'
        'Invalid arg [{a}] at class {b}'            | [b: '22']                                     | 'Invalid arg [{a}] at class 22'
        'Invalid arg {a'                            | []                                            | 'Invalid arg {a'
        'Invalid arg {{ab}}'                        | [ab: 12]                                      | 'Invalid arg {12}'
        'Invalid arg {a{b}c}'                       | [b: 12]                                       | 'Invalid arg {a12c}'
        'Invalid arg } bc'                          | [b: 23]                                       | 'Invalid arg } bc'
        'Invalid arg { abc'                         | null                                          | 'Invalid arg { abc'
    }

    def 'Test make string by array and map'() {
        expect:
        StringHelper.makeString(strTemp, namedVars, indexedVars) == expect

        where:
        strTemp                 | namedVars                         | indexedVars               | expect
        ''                      | [a: 'b']                          | [] as Object[]            | ''
        'A {} b'                | [a: 'b']                          | ['1'] as Object[]         | 'A 1 b'
        'A {a} b'               | [a: '1']                          | [] as Object[]            | 'A 1 b'
        'A {} {a} b'            | [a: '1']                          | ['2'] as Object[]         | 'A 2 1 b'
        'A {a} {} b'            | [a: '1']                          | [null, '2'] as Object[]   | 'A 1 2 b'
        'A {a} {} b'            | [b: 'c']                          | ['1', '2'] as Object[]    | 'A 1 2 b'
        'A {} {0} b'            | [b: 'c']                          | ['1', '2'] as Object[]    | 'A 1 1 b'
        'A {} b'                | [b: 'c']                          | [] as Object[]            | 'A {} b'
        'A {} b'                | [b: 'c']                          | [null] as Object[]        | 'A  b'
        'A {} b {qw'            | [b: 'c']                          | ['1'] as Object[]         | 'A 1 b {qw'
        'A {} b {12'            | [b: 'c']                          | ['1'] as Object[]         | 'A 1 b {12'
        'A {1b} b'              | ['1b': 'c']                       | [] as Object[]            | 'A c b'
        'A } b'                 | [b: 'c']                          | [] as Object[]            | 'A } b'
        'A } b'                 | null                              | null                      | 'A } b'
    }

    def 'Test get first line'() {
        expect:
        StringHelper.firstLine(str) == line

        where:
        str             | line
        ""              | ""
        "ab"            | "ab"
        "ab\nbc"        | "ab"
    }

    def 'Test get first line error'() {
        when:
        StringHelper.firstLine(null)

        then:
        thrown(InvalidArgumentException)
    }

    def 'Test make MD5'() {
        expect:
        StringHelper.makeMD5(str) == md5

        where:
        str     | md5
        "Hello" | "8b1a9953c4611296a827abf8c47804d7"
        "World" | "f5a7924e621e84c9280a9a27e1bcb7f6"
    }

    def 'Test clear StringBuilder'() {
        given:
        StringBuilder buffer1 = new StringBuilder(str1)
        StringBuilder buffer2 = new StringBuilder(str2)

        when:
        StringHelper.clear(buffer1, buffer2)

        then:
        buffer1.length() == 0
        buffer2.length() == 0

        where:
        str1    | str2
        ''      | 'c'
        'abc'   | ''
        'aaa'   | ' '
    }
}
