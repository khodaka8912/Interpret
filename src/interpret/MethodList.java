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
	private Method[] filteredMethods = new Method[0];
	private final Set<MethodChangedListener> listeners = new HashSet<MethodChangedListener>();

	public MethodList() {
		setModel(new MethodListModel());
		setCellRenderer(new MethodListCellRenderer());

		addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				for (MethodChangedListener listener : listeners) {
					int i = getSelectedIndex();
					listener.onChange(i == -1 ? null : filteredMethods[i]);
				}
			}
		});
	}

	public Class<?> getClass_() {
		return cls;
	}

	public void setClass(Class<?> class_) {
		if (class_ == null) {
			methods = new Method[0];
		} else {
			methods = class_.getMethods();
		}

		filteredMethods = methods;

		updateUI();
	}

	public Method getSelectedMethod() {
		int i = getSelectedIndex();
		if (i == -1) {
			return null;
		} else {
			return filteredMethods[i];
		}
	}

	public void setFilterText(String filterText) {
		if (filterText.equals("")) {
			filteredMethods = methods;
		} else {
			List<Method> filteredMethodList = new ArrayList<Method>();
			for (Method method : methods) {
				if (method.getName().indexOf(filterText) != -1) {
					filteredMethodList.add(method);
				}
			}
			filteredMethods = filteredMethodList.toArray(new Method[0]);
		}

		clearSelection();
		updateUI();
	}

	public boolean addMethodChangedListener(MethodChangedListener listener) {
		return listeners.add(listener);
	}

	public boolean removeMethodChangedListener(MethodChangedListener listener) {
		return listeners.remove(listener);
	}

	public interface MethodChangedListener {
		void onChange(Method method);
	}

	private class MethodListModel extends AbstractListModel<Method> {

		@Override
		public int getSize() {
			if (filteredMethods.length == 0) {
				clearSelection();
			}

			return filteredMethods.length;
		}

		@Override
		public Method getElementAt(int i) {
			return filteredMethods[i];
		}
	}

	private class MethodListCellRenderer implements ListCellRenderer<Method> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Method> list, Method value, int index,
				boolean isSelected, boolean hasFocus) {
			String str = filteredMethods[index].toString().replaceAll("java\\.lang\\.", "");
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
