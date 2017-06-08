/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen

import spock.lang.Ignore
import spock.lang.Specification

import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.tools.Diagnostic

/**
 * Unit test for LogSupport
 */
class LogSupportTest extends Specification {

    def 'Test info'() {
        given:
        def msger = Mock(Messager)
        def procEnv = Mock(ProcessingEnvironment) {
            getMessager() >> msger
        }
        def LogSupport log = new LogSupport(procEnv)

        when:
        log.info(msg)

        then:
        noExceptionThrown()
        1 * msger.printMessage(Diagnostic.Kind.NOTE, msg)

        where:
        msg     | placeholder
        'msg'   | null
    }

    def 'Test info with arg'() {
        given:
        def msger = Mock(Messager)
        def procEnv = Mock(ProcessingEnvironment) {
            getMessager() >> msger
        }
        def LogSupport log = new LogSupport(procEnv)

        when:
        log.info(msg, args)

        then:
        noExceptionThrown()
        1 * msger.printMessage(Diagnostic.Kind.NOTE, output)

        where:
        msg         | args                      | output
        'a {} c {}' | ['b', 'd'] as Object[]    | 'a b c d'
    }

    def 'Test error'() {
        given:
        def msger = Mock(Messager)
        def procEnv = Mock(ProcessingEnvironment) {
            getMessager() >> msger
        }
        def LogSupport log = new LogSupport(procEnv)

        when:
        log.error(msg, args)

        then:
        noExceptionThrown()

        1 * msger.printMessage(Diagnostic.Kind.ERROR, output)

        where:
        msg         | args                      | output
        'a {} c {}' | ['b', 'd'] as Object[]    | 'a b c d'
    }

    def 'Test error with additional info'() {
        given:
        def elemt = Mock(Element)
        def annoMirror = Mock(AnnotationMirror)
        def msger = Mock(Messager)
        def procEnv = Mock(ProcessingEnvironment) {
            getMessager() >> msger
        }
        def LogSupport log = new LogSupport(procEnv)

        when:
        log.error(msg, elemt, annoMirror)

        then:
        noExceptionThrown()

        1 * msger.printMessage(Diagnostic.Kind.ERROR, msg, elemt, annoMirror)

        where:
        msg         | placeholder
        'a b c d'   | null
    }

    @Ignore
    def 'Test error with throwable'() {
        given:
        def msger = Mock(Messager)
        def procEnv = Mock(ProcessingEnvironment) {
            getMessager() >> msger
        }
        def LogSupport log = new LogSupport(procEnv)
        def t = Mock(Throwable)

        when:
        log.error(t)

        then:
        noExceptionThrown()
        1 * msger.printMessage(Diagnostic.Kind.ERROR, _ as String)

        where:
        msg     | placeholder
        'msg'   | null
    }
}
