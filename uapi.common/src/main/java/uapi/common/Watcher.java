/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import uapi.GeneralException;

/**
 * The watcher watch (block current thread) on a specific condition until the condition is satisfied or timed out.
 */
public final class Watcher {

    private static final IntervalTime DEFAULT_TIMEOUT           = IntervalTime.parse("5s");
    private static final IntervalTime DEFAULT_POLLING_INTERVAL  = IntervalTime.parse("100ms");
    private static final IntervalTime DEFAULT_DELAY_INTERVAL    = IntervalTime.parse("0ms");

    public static Watcher on(WatcherCondition condition) {
        return new Watcher(condition);
    }

    private final WatcherCondition _condition;

    private IntervalTime _timeout           = DEFAULT_TIMEOUT;
    private IntervalTime _pollingInterval   = DEFAULT_POLLING_INTERVAL;
    private IntervalTime _delayInterval     = DEFAULT_DELAY_INTERVAL;

    private Watcher(WatcherCondition condition) {
        ArgumentChecker.required(condition, "condition");
        this._condition = condition;
    }

    public Watcher timeout(String timeout) {
        this._timeout = IntervalTime.parse(timeout);
        return this;
    }

    public Watcher polling(String pollingInterval) {
        this._pollingInterval = IntervalTime.parse(pollingInterval);
        return this;
    }

    public Watcher delay(String delayInterval) {
        this._delayInterval = IntervalTime.parse(delayInterval);
        return this;
    }

    public void start() {
        long startTime = System.currentTimeMillis();
        if (this._condition.accept()) {
            return;
        }
        if (this._delayInterval.milliseconds() != 0L) {
            try {
                Thread.sleep(this._delayInterval.milliseconds());
            } catch (InterruptedException ex) {
                throw new GeneralException(ex);
            }
            if (this._condition.accept()) {
                return;
            }
        }
        long timeout = this._timeout.milliseconds();
        if (System.currentTimeMillis() - startTime >= timeout) {
            throw new GeneralException("The watcher is timed out");
        }
        while (true) {
            try {
                Thread.sleep(this._pollingInterval.milliseconds());
            } catch (InterruptedException ex) {
                throw new GeneralException(ex);
            }
            if (this._condition.accept()) {
                return;
            }
            if (System.currentTimeMillis() - startTime >= timeout) {
                throw new GeneralException("The watcher is timed out");
            }
        }
    }

    public interface WatcherCondition {

        boolean accept();
    }
}
