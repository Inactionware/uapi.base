/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common

import spock.lang.Ignore
import spock.lang.Specification
import uapi.InvalidArgumentException

/**
 * The unit test for Builder
 */
@Ignore
class BuilderTest extends Specification {

    def 'Test put transience'() {
        given:
        def builder = new FakeBuilder()

        when:
        builder.putTransience(objName, obj)

        then:
        builder.getTransience(objName) == obj

        where:
        objName     | obj       | result
        'test'      | 'obj'     | 'obj'
    }

    def 'Test create transience item if absent'() {
        given:
        def builder = new FakeBuilder()

        when:
        builder.createTransienceIfAbsent(name, { -> obj })

        then:
        builder.getTransience(name) == result

        where:
        name    | obj   | result
        'name'  | 'obj' | 'obj'
    }

    def 'Test create transience item if exist'() {
        given:
        def builder = new FakeBuilder()

        when:
        builder.putTransience(name, obj)
        builder.createTransienceIfAbsent(name, { -> newObj })

        then:
        builder.getTransience(name) == result

        where:
        name    | obj   | newObj    | result
        'name'  | 'obj' | 'newObj'  | 'obj'
    }

    def 'Test build'() {
        given:
        def builder = new FakeBuilder()

        when:
        builder.build()

        then:
        builder.doInitProperties
        builder.doValidation
    }

    private class FakeBuilder extends Builder<String> {

        boolean doValidation = false;
        boolean doInitProperties = false;
        boolean doCreateInstance = false;

        @Override
        protected void validate() throws InvalidArgumentException {
            this.doValidation = true;
        }

        @Override
        protected void beforeCreateInstance() {

        }

        @Override
        protected void afterCreateInstance() {

        }

        protected void initProperties() {
            this.doInitProperties = true;
        }

        @Override
        protected String createInstance() {
            this.doCreateInstance = true;
            return null
        }
    }
}
