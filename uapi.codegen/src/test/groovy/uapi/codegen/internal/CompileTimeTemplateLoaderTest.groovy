/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen.internal

import spock.lang.Specification
import uapi.GeneralException

import javax.annotation.processing.Filer
import javax.tools.FileObject
import javax.tools.StandardLocation

/**
 * Unit test for CompileTimeTemplateLoader
 */
class CompileTimeTemplateLoaderTest extends Specification {

    def 'Test create instance'() {
        when:
        def tempLoader = new CompileTimeTemplateLoader(Mock(BuilderContext), 'pkg')

        then:
        noExceptionThrown()
    }

    def 'Test find template source'() {
        when:
        def budrCtx = Mock(BuilderContext) {
            getFiler() >> Mock(Filer) {
                getResource(StandardLocation.CLASS_PATH, basePkg, name) >> Mock(FileObject)
            }
        }
        def tempLoader = new CompileTimeTemplateLoader(budrCtx, basePkg)

        then:
        tempLoader.findTemplateSource(name) != null

        where:
        basePkg     | pkg   | name
        'abc'       | 'd'   | 'BB'
    }

    def 'Test get last modified'() {
        when:
        def budrCtx = Mock(BuilderContext)
        def tempSrc = Mock(FileObject) {
            getLastModified() >> lastMod
        }
        def tempLoader = new CompileTimeTemplateLoader(budrCtx, basePkg)

        then:
        tempLoader.getLastModified(tempSrc) == lastMod

        where:
        basePkg | lastMod
        'abc'   | 1L
    }

    def 'Test get last modified with exception'() {
        when:
        def budrCtx = Mock(BuilderContext)
        def tempLoader = new CompileTimeTemplateLoader(budrCtx, basePkg)
        tempLoader.getLastModified(Mock(Object))

        then:
        thrown(GeneralException)

        where:
        basePkg | lastMod
        'abc'   | 1L
    }

    def 'Test get reader'() {
        when:
        def budrCtx = Mock(BuilderContext)
        def reader = Mock(Reader)
        def tempSrc = Mock(FileObject) {
            openReader(true) >> reader
        }
        def tempLoader = new CompileTimeTemplateLoader(budrCtx, basePkg)

        then:
        tempLoader.getReader(tempSrc, encoding) == reader

        where:
        basePkg | lastMod   | encoding
        'abc'   | 1L        | 'UTF-8'
    }
}
