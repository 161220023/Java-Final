package annotation;

import java.lang.annotation.*;

@Documented
@Retention(value=RetentionPolicy.SOURCE)
@Target(value=ElementType.TYPE)
public @interface Author {
	String name();
	String number();
	String email();
}
