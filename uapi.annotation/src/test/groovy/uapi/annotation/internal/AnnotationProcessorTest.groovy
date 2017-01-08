package uapi.annotation.internal

import spock.lang.Ignore
import spock.lang.Specification

import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
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

    @Ignore
    def 'Test process'() {
        def annoElem = Mock(TypeElement) {

        }
        def procEnv = Mock(ProcessingEnvironment) {
            getMessager() >> Mock(Messager)
        }
        def roundEnv = Mock(RoundEnvironment) {

        }
        def annoProc = new AnnotationProcessor()

        when:
        annoProc.init(procEnv)
        annoProc.process([annoElem] as Set, roundEnv)

        then:
        noExceptionThrown()
    }
}
