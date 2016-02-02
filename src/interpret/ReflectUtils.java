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
		int modifiers = field.getModifiers();
		if (Modifier.isFinal(modifiers)) {
			Field modifiersField = field.getClass().getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		}
		if (Modifier.isStatic(modifiers)) {
			field.set(null, value);
		} else {
			field.set(target, value);
		}
	}
}
