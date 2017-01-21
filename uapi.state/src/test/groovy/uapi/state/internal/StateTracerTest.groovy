/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.state.internal

import junit.extensions.TestSetup
import spock.lang.Specification
import uapi.GeneralException
import uapi.state.IOperation
import uapi.state.IShifter
import uapi.state.IStateListener
import uapi.state.IStateTracer

/**
 * Unit test for StateTracer
 */
class StateTracerTest extends Specification {

    public static final STATE_ACTIVE   = "active"
    public static final STATE_DEACTIVE = "deactive";

    def testShift() {
        given:
        IStateTracer<TestSetup> state = new StateTracer(new Shifter(), TestState.INIT)

        when:
        state.shift(toState)

        then:
        state.get() == expectedState

        where:
        toState         | expectedState
        STATE_ACTIVE    | TestState.ACTIVED
    }

    def testShiftUnsupportedOperation() {
        given:
        IStateTracer<TestSetup> state = new StateTracer(new Shifter(), TestState.INIT)

        when:
        state.shift(toState)

        then:
        thrown(GeneralException)

        where:
        toState | unsupported
        'ABC'   | 'ABC'
    }

    def testSubscribe() {
        given:
        IStateTracer<TestSetup> state = new StateTracer(new Shifter(), TestState.INIT)
        def listener = Mock(IStateListener)
        state.subscribe(listener)

        when:
        state.shift(toState)

        then:
        state.get() == expectedState
        1 * listener.stateChanged(TestState.INIT, TestState.ACTIVED)

        where:
        toState         | expectedState
        STATE_ACTIVE    | TestState.ACTIVED
    }

    def testDuplicatedSbuscribe() {
        given:
        IStateTracer<TestSetup> state = new StateTracer(new Shifter(), TestState.INIT)
        def listener = Mock(IStateListener)
        state.subscribe(listener)

        when:
        state.subscribe(listener)

        then:
        thrown(GeneralException)
    }

    def testUnsubscribe() {
        given:
        IStateTracer<TestSetup> state = new StateTracer(new Shifter(), TestState.INIT)
        def listener = Mock(IStateListener)
        state.subscribe(listener)

        when:
        state.unsubscribe(listener)
        state.shift(toState)

        then:
        state.get() == expectedState
        0 * listener.stateChanged(TestState.INIT, TestState.ACTIVED)

        where:
        toState         | expectedState
        STATE_ACTIVE    | TestState.ACTIVED
    }
}

class Shifter implements IShifter<TestState> {

    @Override
    TestState shift(TestState state, IOperation operation) {
        switch (operation.type()) {
            case StateTracerTest.STATE_ACTIVE:
                return TestState.ACTIVED
            case StateTracerTest.STATE_DEACTIVE:
                return TestState.DEACTIVED
            default:
                throw new GeneralException("unsupported state");
        }
    }
}

enum TestState {
    INIT, ACTIVED, DEACTIVED
}
