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

/**
 * The exception messages are defined in one or more files (properties file)
 */
public abstract class FileBasedExceptionErrors<E extends ParameterizedException>
        extends ExceptionErrors<E> {

    private static final Map<Integer, String> codeKeyMapper = new HashMap<>();

    protected static void mapCodeKey(int code, String key) {
        if (codeKeyMapper.containsKey(code) && ! key.equals(codeKeyMapper.get(code))) {
            throw new GeneralException("The code {} is mapped to key {} but it will be overridden to different key {}");
        }
        codeKeyMapper.put(code, key);
    }

    /**
     * Get error defined properties file based on category and other tags
     *
     * @param   exception
     *          The exception
     * @return  The properties file which is defined errors belongs the category
     */
    protected abstract String getFile(E exception);

    @Override
    public String getMessageTemplate(E exception) {
        String propKey = codeKeyMapper.get(exception.errorCode());
        if (Strings.isNullOrEmpty(propKey)) {
            return null;
        }
        String propFile = getFile(exception);
        if (propFile == null) {
            throw new GeneralException("No properties file is mapped to category - {}", exception.category());
        }
        String msgTemp = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(PropertiedException.class.getResourceAsStream(propFile)))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.indexOf(propKey) != 0) {
                    continue;
                }
                int idx = line.indexOf("=");
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
