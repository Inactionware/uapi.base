package uapi.codegen.internal;

import uapi.codegen.IResourceFile;
import uapi.common.ArgumentChecker;

import javax.tools.JavaFileManager;
import java.util.ArrayList;
import java.util.List;

class ResourceFile implements IResourceFile {

    private static final String DEFAULT_ENCODER = "utf-8";

    private final JavaFileManager.Location _location;

    private final String _fileName;

    private final String _encoder;

    private final List<String> _content;

    ResourceFile(
            final JavaFileManager.Location location,
            final String fileName
    ) {
        this(location, fileName, null);
    }

    ResourceFile(
            final JavaFileManager.Location location,
            final String fileName,
            final String encoder
    ) {
        ArgumentChecker.required(location, "location");
        ArgumentChecker.required(fileName, "fileName");
        this._location = location;
        this._fileName = fileName;
        if (encoder == null) {
            this._encoder = DEFAULT_ENCODER;
        } else {
            this._encoder = encoder;
        }
        this._content = new ArrayList<>();
    }

    @Override
    public void appendContent(String content) {
        ArgumentChecker.required(content, "content");
        this._content.add(content);
    }

    JavaFileManager.Location location() {
        return this._location;
    }

    String fileName() {
        return this._fileName;
    }

    String encoder() {
        return this._encoder;
    }

    String[] content() {
        return this._content.toArray(new String[0]);
    }
}
