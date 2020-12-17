package rsb.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PassiveScriptManifest {

	String name();

	double version() default 1.0;

	String description() default "";

	String[] authors();
}
