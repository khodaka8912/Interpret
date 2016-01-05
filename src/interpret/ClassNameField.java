package interpret;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class ClassNameField extends JTextField {

	private Class<?> lastClass;
	private final Set<ClassChangedListener> listeners = new HashSet<ClassChangedListener>();

	public ClassNameField() {
		getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent documentEvent) {
				onUpdate();
			}

			@Override
			public void removeUpdate(DocumentEvent documentEvent) {
				onUpdate();
			}

			@Override
			public void changedUpdate(DocumentEvent documentEvent) {
			}
		});
	}

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

	private void onUpdate() {
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
