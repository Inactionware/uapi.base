package uapi.common

import spock.lang.Specification
import uapi.GeneralException

/**
 * Unit tests for Watcher
 */
class WatcherTest extends Specification {

    def 'Test condition satisfied'() {
        given:
        def watcher = Watcher.on({ -> true })

        when:
        watcher.start()

        then:
        noExceptionThrown()
    }

    def 'Test delay'() {
        given:
        def condition = Mock(Watcher.WatcherCondition) {
            2 * accept() >>> [ false, true ]
        }
        def watcher = Watcher.on(condition).delay('100ms')

        when:
        watcher.start()

        then:
        noExceptionThrown()
    }

    def 'Test polling'() {
        given:
        def condition = Mock(Watcher.WatcherCondition) {
            3 * accept() >>> [ false, false, true ]
        }
        def watcher = Watcher.on(condition).polling('50ms')

        when:
        watcher.start()

        then:
        noExceptionThrown()
    }

    def 'Test default time out'() {
        given:
        def condition = Mock(Watcher.WatcherCondition) {
            11 * accept() >> false
        }
        def watcher = Watcher.on(condition).polling("100ms").timeout("1s")

        when:
        watcher.start()

        then:
        thrown(GeneralException)
    }
}
