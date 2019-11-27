/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen.internal

import spock.lang.Ignore
import spock.lang.Specification

import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * Unit test for AnnotationProcessor
 */
class AnnotationProcessorTest extends Specification {

    def 'Test init'() {
        when:
        def procEnv = Mock(ProcessingEnvironment) {
            getMessager() >> Mock(Messager)
        }
        def annoProc = new AnnotationProcessor()
        annoProc.init(procEnv)

        then:
        noExceptionThrown()
        annoProc.getHandlerCount() == 1
        annoProc.getSupportedAnnotationTypes() != null
        annoProc.getSupportedSourceVersion() != null
    }

    def 'Test process'() {
        def annoElem = Mock(TypeElement) {

        }
        def procEnv = Mock(ProcessingEnvironment) {
            getMessager() >> Mock(Messager)
        }
        def roundEnv = Mock(RoundEnvironment) {
            getElementsAnnotatedWith(_) >> {
                return new HashSet<>()
            }
        }
        def annoProc = new AnnotationProcessor()

        when:
        annoProc.init(procEnv)
        annoProc.process([annoElem] as Set, roundEnv)

        then:
        noExceptionThrown()
    }
}
