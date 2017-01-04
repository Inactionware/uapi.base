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
 * Unit test for StringHelper
 */
class StringHelperTest extends Specification{

    def 'Test make string'() {
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
        "{1} index is not start from {}"            | ["test", "un-index", "0"] as Object[]         | "un-index index is not start from 0"
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