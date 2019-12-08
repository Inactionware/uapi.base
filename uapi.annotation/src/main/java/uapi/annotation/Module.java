/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.annotation;

import uapi.common.StringHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicate the annotated packet is a module description class.
 * Normal the annotation should be defined in top level package-info file when you want to enable the project is module
 * ready.
 * An exception will be thrown if you define more then one time Module annotation.
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.SOURCE)
public @interface Module {

    String name() default StringHelper.EMPTY;

    String[] requires() default {};
}
