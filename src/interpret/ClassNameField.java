package interpret;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ClassNameField extends JTextField {

	private Class<?> lastClass;
	private final Set<ClassChangedListener> listeners = new HashSet<ClassChangedListener>();
	
	private static final Map<String, Class<?>> PRIMITIVE_TYPE_NAME_MAP = new HashMap<>();
	
	static {
		Arrays.stream(new Class<?>[]{boolean.class, short.class, byte.class, int.class, long.class, float.class, double.class})
		.forEach((c) ->
			PRIMITIVE_TYPE_NAME_MAP.put(c.getName(), c)
		);
	}

	public Class<?> getClassObject() {
		String className = getText();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			if (className.contains(".")) {
				return null;
			}
			try {
				return Class.forName("java.lang." + className);
			} catch (ClassNotFoundException e1) {
				return PRIMITIVE_TYPE_NAME_MAP.get(className);
			}
		}
	}

	public boolean addClassChangedListener(ClassChangedListener listener) {
		return listeners.add(listener);
	}

	public boolean removeClassChangedListener(ClassChangedListener listener) {
		return listeners.remove(listener);
	}

	public interface ClassChangedListener {
		void onChange(Class<?> class_);
	}

	public void updateClass() {
		Class<?> currentClass = getClassObject();
		if (currentClass == lastClass) {
			return;
		}
		lastClass = currentClass;
		for (ClassChangedListener listener : listeners) {
			listener.onChange(currentClass);
		}
	}

}
