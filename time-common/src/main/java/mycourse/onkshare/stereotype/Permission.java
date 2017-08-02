package mycourse.onkshare.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by doll on 4/11.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String value() default "";

    public static String USER = "user";
    public static String SUPER_USER = "super";
}
