package uapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation is used to load module information from module-info file
 */
@Target(ElementType.MODULE)
@Retention(RetentionPolicy.SOURCE)
public @interface Module { }
