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
 * Unit test for WordHelper
 */
class WordHelperTest extends Specification {

    def 'Test singularize'() {
        expect:
        WordHelper.singularize(origin) == expect

        where:
        origin      | expect
        'tests'     | 'test'
        'boxes'     | 'box'
        'factories' | 'factory'
        'oxen'      | 'ox'
    }

    def 'Test pluralize'() {
        expect:
        WordHelper.pluralize(origin) == expect

        where:
        origin      | expect
        'test'      | 'tests'
        'box'       | 'boxes'
        'factory'   | 'factories'
        'ox'        | 'oxen'
    }
}
