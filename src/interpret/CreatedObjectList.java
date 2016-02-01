package interpret;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class CreatedObjectList extends JList<Object> {

	private List<Object> createdObjects;
	private final Set<ObjectChangedListener> listeners = new HashSet<>();

	public CreatedObjectList() {
		this(null);
	}

	public CreatedObjectList(List<Object> objects) {
		this.createdObjects = objects == null ? new ArrayList<>() : objects;
		setModel(new AbstractListModel<Object>() {

			@Override
			public int getSize() {
				return createdObjects.size();
			}

			@Override
			public Object getElementAt(int i) {
				return createdObjects.get(i);
			}
		});

		setCellRenderer((list, value, index, isSelected, hasFocus) -> {
			JLabel label = new JLabel(value.getClass().getSimpleName() + "#" + value.hashCode());
			if (isSelected) {
				label.setForeground(Color.WHITE);
				label.setBackground(Color.GRAY);
				label.setOpaque(true);
			}
			return label;
		});

		addListSelectionListener((e) -> {
			for (ObjectChangedListener listener : listeners) {
				int i = getSelectedIndex();
				listener.onChange(i == -1 ? null : createdObjects.get(i));
			}
		});
	}

	public void addObject(Object obj) {
		createdObjects.add(obj);
		updateUI();
	}

	public interface ObjectChangedListener {
		public void onChange(Object object);
	}

	public void addListener(ObjectChangedListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ObjectChangedListener listener) {
		listeners.remove(listener);
	}

	public List<Object> getCreatedObjects() {
		return createdObjects;
	}
}
