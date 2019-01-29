/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.rx

import spock.lang.Specification

class TerminateMapperTest extends Specification {

    def 'Test get item'() {
        Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, true, false]
            getItem() >>> ["1", '2', "3", null]
        }

        given:
        TerminateMapper opt = new TerminateMapper(preOpt, { it == '2' })

        when:
        opt.getItem() == "1"
        opt.getItem()

        then:
        thrown(NoItemException)
        ! opt.hasItem()
    }
}
