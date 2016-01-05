package interpret;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ConstructorList extends JList<Constructor<?>> {

	private static final Constructor<?>[] EMPTY_CONSTRUCTORS = new Constructor<?>[0];

	private Constructor<?>[] constructors = EMPTY_CONSTRUCTORS;
	private final Set<ConstructorChangedListener> listeners = new HashSet<ConstructorChangedListener>();

	public ConstructorList() {
		setModel(new ConstructorListModel());
		setCellRenderer(new ConstructorListCellRenderer());

		addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				for (ConstructorChangedListener listener : listeners) {
					int i = getSelectedIndex();
					listener.onChange(i == -1 ? null : constructors[i]);
				}
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

	private class ConstructorListModel extends AbstractListModel<Constructor<?>> {

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
	}

	private class ConstructorListCellRenderer implements ListCellRenderer<Constructor<?>> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Constructor<?>> list, Constructor<?> value,
				int index, boolean isSelected, boolean hasFocus) {
			JLabel label = new JLabel(constructors[index].toString().replaceAll("java\\.lang\\.", ""));

			if (isSelected) {
				label.setForeground(Color.WHITE);
				label.setBackground(Color.GRAY);
				label.setOpaque(true);
			}

			return label;
		}
	}
}
