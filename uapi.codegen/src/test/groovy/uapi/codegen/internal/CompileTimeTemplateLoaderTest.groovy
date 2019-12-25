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
        def tempLoader = new CompileTimeTemplateLoader(Mock(BuilderContext))

        then:
        noExceptionThrown()
    }

    def 'Test find template source'() {
        when:
        def budrCtx = Mock(BuilderContext) {
            getFiler() >> Mock(Filer) {
                getResource(StandardLocation.MODULE_PATH, module + "/", name) >> Mock(FileObject)
            }
        }
        def tempLoader = new CompileTimeTemplateLoader(budrCtx)

        then:
        tempLoader.findTemplateSource(module + ":" + name) != null

        where:
        module  | pkg   | name
        'A'     | 'd'   | 'BB'
    }

    def 'Test get last modified'() {
        when:
        def fileObj = Mock(Filer) {
            getResource(_, _, filePath) >> Mock(FileObject) {
                getLastModified() >> lastMod
            }
        }
        def budrCtx = Mock(BuilderContext) {
            getFiler() >> fileObj
        }
        def tempLoader = new CompileTimeTemplateLoader(budrCtx)
        def tempSrc = tempLoader.findTemplateSource(module + ":" + filePath)

        then:
        tempLoader.getLastModified(tempSrc) == lastMod

        where:
        module  | lastMod   | filePath
        'abc'   | 1L        | 'test'
    }

    def 'Test get last modified with exception'() {
        when:
        def budrCtx = Mock(BuilderContext)
        def tempLoader = new CompileTimeTemplateLoader(budrCtx)
        tempLoader.getLastModified(Mock(Object))

        then:
        thrown(GeneralException)

        where:
        module  | lastMod
        'abc'   | 1L
    }

    def 'Test get reader'() {
        when:
        def reader = Mock(Reader)
        def filer = Mock(Filer) {
            getResource(_, _, filePath) >> Mock(FileObject) {
                openReader(true) >> reader
            }
        }
        def budrCtx = Mock(BuilderContext) {
            getFiler() >> filer
        }
        def tempLoader = new CompileTimeTemplateLoader(budrCtx)
        def tempSrc = tempLoader.findTemplateSource(module + ":" + filePath)

        then:
        tempLoader.getReader(tempSrc, encoding) == reader

        where:
        module  | lastMod   | encoding  | filePath
        'abc'   | 1L        | 'UTF-8'   | 'Test'
    }
}
