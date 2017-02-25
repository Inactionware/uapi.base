/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import com.google.common.base.Strings;
import uapi.InvalidArgumentException;
import uapi.GeneralException;
import uapi.rx.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StringHelper {

    private static final char HEX_DIGITS[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};

    public static final String EMPTY    = "";

    private static final char VAR_START = '{';
    private static final char VAR_END   = '}';

    private static final int MASK_F     = 0xf;
    private static final int FOUR       = 4;
    private static final int SIXTEEN    = 16;

    private StringHelper() { }

    public static String makeString(String str, Object... args) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        StringBuilder buffer = new StringBuilder();
        boolean foundVarStart = false;
        int idxVar = 0;
        int tmpIdx = -1;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == VAR_START) {
                foundVarStart = true;
            } else if (c == VAR_END) {
                if (foundVarStart) {
                    if (tmpIdx != -1) {
                        idxVar = tmpIdx;
                    }
                    if (args.length <= idxVar) {
                        buffer.append(VAR_START).append(tmpIdx == -1 ? "" : tmpIdx).append(VAR_END);
                    } else if (args[idxVar] != null) {
                        buffer.append(args[idxVar]);
                    }
                    foundVarStart = false;
                    idxVar++;
                    tmpIdx = -1;
                } else {
                    buffer.append(c);
                }
            } else {
                if (foundVarStart) {
                    if (c >= '0' && c <= '9') {
                        if (tmpIdx == -1) {
                            tmpIdx = 0;
                        }
                        tmpIdx = tmpIdx * 10 + Character.getNumericValue(c);
                    } else {
                        buffer.append(VAR_START);
                        if (tmpIdx != -1) {
                            buffer.append(tmpIdx);
                            tmpIdx = -1;
                        }
                        buffer.append(c);
                        foundVarStart = false;
                    }
                } else {
                    buffer.append(c);
                }
            }
        }
        if (foundVarStart) {
            buffer.append(VAR_START).append(tmpIdx == -1 ? "" : tmpIdx);
        }
        return buffer.toString();
    }

    /**
     * Construct string from a string template and all named variable in the template will be replaced by arguments map.
     * The named variable will around with { and }. If named variable is found in argument map then the named variable
     * will keep no change including { and }.
     *
     * @param   stringTemplate
     *          The string template
     * @param   arguments
     *          The argument map which will replace to the string template
     * @return  The result string
     */
    public static String makeString(String stringTemplate, Map<Object, Object> arguments) {
        if (Strings.isNullOrEmpty(stringTemplate)) {
            return stringTemplate;
        }
        if (arguments == null) {
            arguments = new HashMap<>();
        }
        StringBuilder buffer = new StringBuilder();
        boolean foundVarStart = false;
        String varName = "";
        for (int i = 0; i < stringTemplate.length(); i++) {
            char c = stringTemplate.charAt(i);
            if (c == VAR_START) {
                if (foundVarStart) {
                    buffer.append(VAR_START).append(varName);
                    varName = "";
                }
                foundVarStart = true;
            } else if (c == VAR_END) {
                if (foundVarStart) {
                    Object argument = arguments.get(varName.trim());
                    if (argument != null) {
                        buffer.append(argument);
                    } else {
                        buffer.append(VAR_START).append(varName).append(VAR_END);
                    }
                    foundVarStart = false;
                    varName = "";
                } else {
                    buffer.append(c);
                }
            } else {
                if (foundVarStart) {
                    varName += c;
                } else {
                    buffer.append(c);
                }
            }
        }
        if (foundVarStart) {
            buffer.append(VAR_START).append(varName);
        }
        return buffer.toString();
    }

    /**
     * Make MD5 string based string array items
     *
     * @param   strs
     *          String array that used for make MD5 string
     * @return  MD5 string
     */
    public static String makeMD5(final String... strs) {
        if (strs == null || strs.length == 0) {
            throw new IllegalArgumentException();
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch(NoSuchAlgorithmException ex) {
            throw new GeneralException(ex);
        }
        for (String str : strs) {
            if (str == null) {
                throw new InvalidArgumentException("Not allow null string in the array {}", CollectionHelper.asString(strs));
            }
            md.update(str.getBytes());
        }
        byte source[] = md.digest();
        char target[] = new char[SIXTEEN * 2];
        int k = 0;
        for (int i = 0; i < SIXTEEN; i++) {
            byte sbyte = source[i];
            target[k++] = HEX_DIGITS[sbyte >>> FOUR & MASK_F];
            target[k++] = HEX_DIGITS[sbyte & MASK_F];
        }
        return new String(target);
    }

    public static String firstLine(String str) {
        ArgumentChecker.notNull(str, "str");
        BufferedReader br = new BufferedReader(new StringReader(str));
        String line = "";
        try {
            line = br.readLine();
        } catch (IOException ex) {
            throw new InvalidArgumentException("Can't read input string - {}", str);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                throw new GeneralException("Close string reader failed");
            }
        }
        return line == null ? StringHelper.EMPTY : line;
    }

    public static void clear(StringBuilder... buffers) {
        ArgumentChecker.required(buffers, "buffers");
        Looper.on(buffers).foreach(buffer -> buffer.delete(0, buffer.length()));
    }
}
