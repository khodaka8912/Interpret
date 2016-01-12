package interpret;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Method;
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
public class MethodList extends JList<Method> {

	private Class<?> cls;
	private Method[] methods = new Method[0];
	private final Set<MethodChangedListener> listeners = new HashSet<>();

	public MethodList() {
		setModel(new MethodListModel());
		setCellRenderer(new MethodListCellRenderer());

		addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				for (MethodChangedListener listener : listeners) {
					int i = getSelectedIndex();
					listener.onChange(i == -1 ? null : methods[i]);
				}
			}
		});
	}

	public void setClass(Class<?> cls) {
		if (cls == null) {
			methods = new Method[0];
		} else {
			methods = cls.getMethods();
		}
		updateUI();
	}

	public Method getSelectedMethod() {
		int i = getSelectedIndex();
		if (i == -1) {
			return null;
		} else {
			return methods[i];
		}
	}

	public boolean addListener(MethodChangedListener listener) {
		return listeners.add(listener);
	}

	public boolean removeListener(MethodChangedListener listener) {
		return listeners.remove(listener);
	}

	public interface MethodChangedListener {
		void onChange(Method method);
	}

	private class MethodListModel extends AbstractListModel<Method> {

		@Override
		public int getSize() {
			if (methods.length == 0) {
				clearSelection();
			}

			return methods.length;
		}

		@Override
		public Method getElementAt(int i) {
			return methods[i];
		}
	}

	private class MethodListCellRenderer implements ListCellRenderer<Method> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Method> list, Method value, int index,
				boolean isSelected, boolean hasFocus) {
			String str = methods[index].toString().replaceAll("java\\.lang\\.", "");
			if (cls != null) {
				System.out.println(cls.getCanonicalName());
				str = str.replaceAll(cls.getCanonicalName() + "\\.", "");
			}
			JLabel label = new JLabel(str);

			if (isSelected) {
				label.setForeground(Color.WHITE);
				label.setBackground(Color.GRAY);
				label.setOpaque(true);
			}
			return label;
		}
	}
}
