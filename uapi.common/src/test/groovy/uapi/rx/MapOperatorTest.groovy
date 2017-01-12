/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx

import spock.lang.Specification

/**
 * Test for MapMapper
 */
class MapOperatorTest extends Specification {

    def 'Test get item'() {
        when:
        Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >> next
            getItem() >> data
        }
        MapMapper mapOpt = new MapMapper(preOpt, { item -> Integer.parseInt(item)});

        then:
        mapOpt.hasItem() == next
        mapOpt.getItem() == rtn

        where:
        next    | data  | rtn
        true    | "1"   | 1
        true    | "2"   | 2
        false   | null  | null
    }
}
