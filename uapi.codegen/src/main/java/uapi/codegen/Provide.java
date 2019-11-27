/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.codegen;

import uapi.common.Pair;

public class Provide extends Pair<String, String> {

    public Provide(
            final String service,
            final String implementation
    ) {
        super(service, implementation);
    }

    public String service() {
        return getLeftValue();
    }

    public String implementation() {
        return getRightValue();
    }
}
