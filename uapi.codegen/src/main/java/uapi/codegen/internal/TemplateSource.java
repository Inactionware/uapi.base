/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen.internal;

import uapi.GeneralException;
import uapi.common.ArgumentChecker;
import uapi.rx.Looper;

import javax.tools.FileObject;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * The TemplateSource hold file object and opened reader
 */
class TemplateSource {

    private final FileObject _fileObj;
    private final List<Reader> _readers;

    TemplateSource(final FileObject fileObject) {
        ArgumentChecker.required(fileObject, "fileObject");
        this._fileObj = fileObject;
        this._readers = new ArrayList<>();
    }

    Reader openReader() throws IOException {
        return this._fileObj.openReader(true);
    }

    void close() throws IOException {
        Looper.on(this._readers).foreach(reader -> {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new GeneralException(ex);
            }
        });
    }

    long getLastModified() {
        return this._fileObj.getLastModified();
    }
}
