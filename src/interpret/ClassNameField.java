package interpret;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ClassNameField extends JTextField {

	private Class<?> lastClass;
	private final Set<ClassChangedListener> listeners = new HashSet<ClassChangedListener>();

	public Class<?> getClassObject() {
		String className = getText();
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			if (!className.contains(".")) {
				try {
					return Class.forName("java.lang." + className);
				} catch (ClassNotFoundException e1) {
					return null;
				}
			}
			return null;
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
