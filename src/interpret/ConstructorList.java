package interpret;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class ConstructorList extends JList<Constructor<?>> {

	private static final Constructor<?>[] EMPTY_CONSTRUCTORS = new Constructor<?>[0];

	private Constructor<?>[] constructors = EMPTY_CONSTRUCTORS;
	private final Set<ConstructorChangedListener> listeners = new HashSet<ConstructorChangedListener>();

	public ConstructorList() {
		setModel(new AbstractListModel<Constructor<?>>() {

			@Override
			public int getSize() {
				if (constructors.length == 0) {
					clearSelection();
				}
				return constructors.length;
			}

			@Override
			public Constructor<?> getElementAt(int i) {
				return constructors[i];
			}
		});

		setCellRenderer((list, value, index, isSelected, hasFocus) -> {
			JLabel label = new JLabel(
					constructors[index].toString().replaceAll("java\\.lang\\.", "").replaceAll("public ", ""));
			if (isSelected) {
				label.setForeground(Color.WHITE);
				label.setBackground(Color.GRAY);
				label.setOpaque(true);
			}
			return label;
		});

		addListSelectionListener(e -> {
			for (ConstructorChangedListener listener : listeners) {
				int i = getSelectedIndex();
				listener.onChange(i == -1 ? null : constructors[i]);
			}
		});
	}

	public void setClass(Class<?> cls) {
		if (cls == null) {
			constructors = EMPTY_CONSTRUCTORS;
		} else {
			constructors = cls.getConstructors();
		}
		updateUI();
	}

	public Constructor<?> getSelectedConstructor() {
		int i = getSelectedIndex();
		return i < 0 ? null : constructors[i];
	}

	public boolean addConstructorChangedListener(ConstructorChangedListener listener) {
		return listeners.add(listener);
	}

	public boolean removeConstructorChangedListener(ConstructorChangedListener listener) {
		return listeners.remove(listener);
	}

	public interface ConstructorChangedListener {
		void onChange(Constructor<?> constructor);
	}
}
