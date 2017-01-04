/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.state.internal;

import uapi.common.ArgumentChecker;
import uapi.state.IOperation;

/**
 * The simple operation contains operation type only
 */
class SimpleOperation implements IOperation {

    private final String _type;

    SimpleOperation(final String type) {
        ArgumentChecker.required(type, "type");
        this._type = type;
    }

    @Override
    public String type() {
        return this._type;
    }
}
