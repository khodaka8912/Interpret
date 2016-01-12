package interpret;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class CreatedObjectList extends JList<Object> {

	List<Object> createdObjects = CreatedObjects.getList();
	private final Set<ObjectChangedListener> listeners = new HashSet<>();

	public CreatedObjectList() {
		setModel(new CreatedListModel());
		setCellRenderer(new CreatedListCellRenderer());

		addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				for (ObjectChangedListener listener : listeners) {
					int i = getSelectedIndex();
					listener.onChange(i == -1 ? null : createdObjects.get(i));
				}
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


	private class CreatedListModel extends AbstractListModel<Object> {

		@Override
		public int getSize() {
			return createdObjects.size();
		}

		@Override
		public Object getElementAt(int i){
			return createdObjects.get(i);
		}
	}

	private static class CreatedListCellRenderer implements ListCellRenderer<Object> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
				boolean isSelected, boolean hasFocus) {
			JLabel label = new JLabel(value.getClass().getSimpleName() + "#" + value.hashCode());

			if (isSelected) {
				label.setForeground(Color.WHITE);
				label.setBackground(Color.GRAY);
				label.setOpaque(true);
			}
			return label;
		}
	}

}
