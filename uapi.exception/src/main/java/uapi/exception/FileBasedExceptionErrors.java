/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.exception;

import com.google.common.base.Strings;
import uapi.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The exception messages are defined in one or more files (properties file)
 */
public abstract class FileBasedExceptionErrors<E extends ParameterizedException>
        extends ExceptionErrors<E> {

    private static final String TEMP_UNKNOWN    = "Unknown error";

    /**
     * Get error defined properties file based on category and other tags
     *
     * @param   exception
     *          The exception
     * @return  The properties file which is defined errors belongs the category
     */
    protected abstract String getFile(E exception);

    protected abstract String getKey(E exception);

    @Override
    public String getMessageTemplate(E exception) {
        if (! exception.hasErrorCode()) {
            return TEMP_UNKNOWN;
        }

        var propKey = getKey(exception);
        if (Strings.isNullOrEmpty(propKey)) {
            return null;
        }
        var propFile = getFile(exception);
        if (propFile == null) {
            throw new GeneralException("No properties file is mapped to category - {}", exception.category());
        }
        String msgTemp = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.getClass().getResourceAsStream(propFile)))) {
            var line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (line.indexOf("#") == 0 || line.indexOf(propKey) != 0) {
                    line = reader.readLine();
                    continue;
                }
                var idx = line.indexOf("=");
                if (idx <= 0 || (idx + 1) >= line.length()) {
                    throw new GeneralException("Incorrect property line in commonErrors.properties - {}", line);
                }
                msgTemp = line.substring(idx + 1).trim();
                break;
            }
        } catch (Exception ex) {
            throw new GeneralException(ex, "Encounter an exception when construct exception - {}",
                    this.getClass().getCanonicalName());
        }
        return msgTemp;
    }
}
