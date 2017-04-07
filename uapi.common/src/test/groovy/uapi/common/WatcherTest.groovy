package uapi.common

import spock.lang.Specification
import uapi.GeneralException

/**
 * Unit tests for Watcher
 */
class WatcherTest extends Specification {

    def 'Test condition satisfied'() {
        given:
        def watcher = Watcher.on({ -> new Watcher.ConditionResult(false) })

        when:
        watcher.start()

        then:
        noExceptionThrown()
    }

    def 'Test polling'() {
        given:
        def condition = Mock(Watcher.WatcherCondition) {
            3 * accept() >>> [ new Watcher.ConditionResult(true), new Watcher.ConditionResult(true), new Watcher.ConditionResult(false) ]
        }
        def watcher = Watcher.on(condition).pollingTime('50ms')

        when:
        watcher.start()

        then:
        noExceptionThrown()
    }

    def 'Test await'() {
        given:
        def awaiting = Mock(IAwaiting) {
            1 * await(_) >> true
        }
        def condition = Mock(Watcher.WatcherCondition) {
            2 * accept() >>> [new Watcher.ConditionResult(awaiting), new Watcher.ConditionResult(false)]
        }
        def watcher = Watcher.on(condition)

        when:
        watcher.start()

        then:
        noExceptionThrown()
    }

    def 'Test polling then await'() {
        given:
        def awaiting = Mock(IAwaiting) {
            1 * await(_) >> true
        }
        def condition = Mock(Watcher.WatcherCondition) {
            3 * accept() >>> [
                    new Watcher.ConditionResult(true),
                    new Watcher.ConditionResult(awaiting),
                    new Watcher.ConditionResult(false)
            ]
        }
        def watcher = Watcher.on(condition)

        when:
        watcher.start()

        then:
        noExceptionThrown()
    }

    def 'Test await then polling'() {
        given:
        def awaiting = Mock(IAwaiting) {
            1 * await(_) >> true
        }
        def condition = Mock(Watcher.WatcherCondition) {
            3 * accept() >>> [
                    new Watcher.ConditionResult(awaiting),
                    new Watcher.ConditionResult(true),
                    new Watcher.ConditionResult(false)
            ]
        }
        def watcher = Watcher.on(condition)

        when:
        watcher.start()

        then:
        noExceptionThrown()
    }

    def 'Test default time out'() {
        given:
        def condition = Mock(Watcher.WatcherCondition) {
            11 * accept() >> new Watcher.ConditionResult(true)
        }
        def watcher = Watcher.on(condition).pollingTime("100ms").timeout("1s")

        when:
        watcher.start()

        then:
        thrown(GeneralException)
    }

    def 'Test default time out by IntervalTime'() {
        given:
        def condition = Mock(Watcher.WatcherCondition) {
            11 * accept() >> new Watcher.ConditionResult(true)
        }
        def watcher = Watcher.on(condition).pollingTime(IntervalTime.parse("100ms")).timeout(IntervalTime.parse("1s"))

        when:
        watcher.start()

        then:
        thrown(GeneralException)
    }
}
