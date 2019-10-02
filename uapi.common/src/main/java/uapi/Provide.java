package uapi;

import uapi.common.ArgumentChecker;

public class Provide {

    private final String _svc;
    private final String _impl;

    public Provide(
            final String service,
            final String implementation
    ) {
        ArgumentChecker.required(service, "service");
        ArgumentChecker.required(implementation, "implementation");
        this._svc = service;
        this._impl = implementation;
    }

    public String service() {
        return this._svc;
    }

    public String implementation() {
        return this._impl;
    }
}
