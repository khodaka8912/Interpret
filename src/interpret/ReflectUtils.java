package interpret;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectUtils {

	public static <T> T construct(Constructor<T> constructor, Object... args) throws Throwable {

		try {
			if (args == null) {
				return constructor.newInstance();
			} else {
				return constructor.newInstance(args);
			}
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	public static Object invoke(Object target, Method method, Object... args) throws Throwable {
		try {
			if (args == null) {
				return method.invoke(target);
			} else {
				return method.invoke(target, args);
			}
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	public static Object getField(Object target, Field field) throws Throwable {
		field.setAccessible(true);
		return field.get(target);
	}

	public static void setField(Object target, Field field, Object value) throws Throwable {
		field.setAccessible(true);
		field.set(target, value);
	}

	public static boolean isSettableField(Field field) {
		int modifiers = field.getModifiers();
		return !(Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}
}
