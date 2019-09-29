/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

import uapi.InvalidArgumentException;
import uapi.InvalidArgumentException.InvalidArgumentType;

import java.util.Collection;

/**
 * A utility for argument checking
 *
 * @author min
 */
public class ArgumentChecker {

    private ArgumentChecker() { }

    /**
     * Check the integer argument value should between min value and max value
     * The min value and max value should be included.
     * An {@code InvalidArgumentException} will be thrown when the argument value
     * is not between min value and max value
     *
     * @param   arg
     *          The integer argument which will be checked
     * @param   argName
     *          The checked argument name
     * @param   minValue
     *          The min value
     * @param   maxValue
     *          The max value
     * @throws  InvalidArgumentException
     *          The checked argument value is not between min value and max value
     */
    public static void checkInt(
            final int arg,
            final String argName,
            final int minValue,
            final int maxValue
    ) {
        if (arg < minValue || arg > maxValue) {
            throw new InvalidArgumentException(
                    argName,
                    "The argument must be more than {} and less than {}",
                    minValue, maxValue);
        }
    }

    /**
     * Check the long argument value should between min value and max value
     * The min value and max value should be included.
     * An {@code InvalidArgumentException} will be thrown when the argument value
     * is not between min value and max value
     *
     * @param   arg
     *          The long argument which will be checked
     * @param   argName
     *          The checked argument name
     * @param   minValue
     *          The min value
     * @param   maxValue
     *          The max value
     * @throws  InvalidArgumentException
     *          The checked argument value is not between min value and max value
     */
    public static void checkLong(
            final long arg,
            final String argName,
            final long minValue,
            final long maxValue
    ) {
        if (arg < minValue || arg > maxValue) {
            throw new InvalidArgumentException(
                    argName,
                    "The argument mut be more than {} and less than {}",
                    minValue, maxValue);
        }
    }

    public static void checkType(Object argument, String argumentName, Class<?> type) {
        if (! type.isInstance(argument)) {
            throw new InvalidArgumentException(argumentName, InvalidArgumentType.CAST);
        }
    }

    /**
     * Ensure the argument is not null, if it is null the exception will be thrown
     *
     * @param   argument
     *          The argument which will be checked
     * @param   argumentName
     *          The argument name
     * @throws  InvalidArgumentException
     *          The argument is null
     */
    public static void notNull(
            final Object argument,
            final String argumentName
    ) throws InvalidArgumentException {
        if (argument == null) {
            throw new InvalidArgumentException(argumentName, InvalidArgumentType.EMPTY);
        }
    }

    /**
     * Check the argument is null or not.
     *
     * @param   argument
     *          The argument which will be checked
     * @return  True means the argument is null otherwise return false
     */
    public static boolean isNull(final Object argument) {
        return argument == null;
    }

    /**
     * Ensure the argument is not emptyArray, if it is emptyArray then the exception will be thrown
     *
     * @param   argument
     *          The argument which will be checked
     * @param   argumentName
     *          The argument name
     * @throws  InvalidArgumentException
     *          The argument is null or emptyArray string or only contains blank string
     */
    public static void notEmpty(
            final String argument,
            final String argumentName
    ) throws InvalidArgumentException {
        if (isEmpty(argument)) {
            throw new InvalidArgumentException(argumentName, InvalidArgumentType.EMPTY);
        }
    }

    /**
     * Test the argument is emptyArray or not.
     * Empty string means null, emptyArray string or only contains blank string
     *
     * @param   argument
     *          The argument which will be tested
     * @return  true if the argument is emptyArray otherwise return false
     */
    public static boolean isEmpty(
            final String argument
    ) {
        return StringHelper.isNullOrEmpty(argument) || argument.trim().length() == 0;
    }

    /**
     * Ensure the array argument is not emptyArray.
     * A emptyArray array means null or the array length is 0
     *
     * @param   argument
     *          The argument which will be checked
     * @param   argumentName
     *          The argument name
     * @param   <T>
     *          The argument type
     * @throws  InvalidArgumentException
     *          If the array argument is emptyArray
     */
    public static <T> void notEmpty(
            final T[] argument,
            final String argumentName
    ) throws InvalidArgumentException {
        notNull(argument, "argument");
        if (argument.length == 0) {
            throw new InvalidArgumentException(argumentName, InvalidArgumentType.EMPTY);
        }
    }

    /**
     * Test the argument equals expected object, if it does not equals then the exception will be thrown
     *
     * @param   argument
     *          The argument which will be checked
     * @param   expected
     *          The expected object
     * @param   argumentName
     *          The argument name
     * @throws  InvalidArgumentException
     *          If the argument does not equals expected object
     */
    public static void equals(
            final Object argument,
            final Object expected,
            final String argumentName
    ) throws InvalidArgumentException {
        if (argument == null && expected == null) {
            return;
        }
        if (argument != null && argument.equals(expected)) {
            return;
        }
        throw new InvalidArgumentException("The arguments {} is not equals expected value {} - {}",
                argumentName, argument, expected);
    }

    /**
     * Test the argument equals unexpected object, if it equals then the exception will be thrown
     *
     * @param   argument
     *          The argument which will be checked
     * @param   unexpected
     *          The unexpected object
     * @param   argumentName
     *          The argument name
     * @throws  InvalidArgumentException
     *          If the argument equals expected object
     */
    public static void notEquals(
            final Object argument,
            final Object unexpected,
            final String argumentName
    ) throws InvalidArgumentException {
        if (argument != null && ! argument.equals(unexpected)) {
            return;
        }
        throw new InvalidArgumentException("The arguments {} is equals unexpected value {} - {}",
                argumentName, argument, unexpected);
    }

    /**
     * Test the argument contains unexpected string, if it contains then an exception will be thrown
     *
     * @param   argument
     *          The argument which will be checked
     * @param   unexpected
     *          The unexpected string
     * @param   argumentName
     *          The argument name
     * @throws  InvalidArgumentException
     *          If the argument contains the unexpected string
     */
    public static void notContains(
            final String argument,
            final String unexpected,
            final String argumentName
    ) throws InvalidArgumentException {
        if (argument != null && ! argument.contains(unexpected)) {
            return;
        }
        throw new InvalidArgumentException("The argument {} contains unexpected value {} - {}",
                argumentName, argument, unexpected);
    }

    /**
     * The the collection contains specified unexpected objects, if it contains then an exception will be thrown
     *
     * @param   argument
     *          The argument which will be checked
     * @param   argumentName
     *          The argument name
     * @param   unexpects
     *          The unexpected object arrary
     * @param   <T>
     *          The element type of argument collection
     * @throws  InvalidArgumentException
     *          If the argument contains the unexpected object
     */
    public static <T> void notContains(
            final Collection<T> argument,
            final String argumentName,
            final T... unexpects
    ) throws InvalidArgumentException {
        notNull(argument, argumentName);
        T unexpected = CollectionHelper.contains(argument, unexpects);
        if (unexpected != null) {
            throw new InvalidArgumentException(
                    "The argument {} contains an unexpected item: {}",
                    argumentName, unexpected);
        }
    }

    /**
     * Ensure the argument is presented, if the argument is null or is emptyArray string
     * then the exception will be thrown.
     *
     * @param   argument
     *          The argument object which will be checked
     * @param   argumentName
     *          The argument name will be used in exception message if check failed
     * @throws  InvalidArgumentException
     *          The argument is null or it is emptyArray string
     */
    public static void required(
            final Object argument,
            final String argumentName
    ) throws InvalidArgumentException {
        if (argument instanceof String) {
            notEmpty((String) argument, argumentName);
        } else {
            notNull(argument, argumentName);
        }
    }

    /**
     * Test the collection is null or it is emptyArray, if so an exception will be thrown
     *
     * @param   collection
     *          The collection which will be checked
     * @param   argumentName
     *          The argument name
     * @throws  InvalidArgumentException
     *          The checked collection is null or it is emptyArray
     */
    public static void notZero(
            final Collection collection,
            final String argumentName
    ) throws InvalidArgumentException {
        notNull(collection, "collection");
        if (collection.size() == 0) {
            throw new InvalidArgumentException(
                    "The size of argument[{}] must be more then 0", argumentName);
        }
    }
}
