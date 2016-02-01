package interpret;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import interpret.SelectObjectFrame.SelectObjectListener;

@SuppressWarnings("serial")
public class ObjectCellEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	private Class<?> type;
	private JComponent editor;
	private Object selectedValue;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object typeValuePair, boolean isSelected,
			boolean hasFocus, int row, int column) {
		final Class<?> type = ((TypedValue) typeValuePair).getType();
		final Object value = ((TypedValue) typeValuePair).getValue();

		if (isNumberClass(type)) {
			return new JLabel(String.valueOf(value));
		} else if (type == char.class) {
			return new JLabel(String.valueOf(value));
		} else if (type == boolean.class) {
			JCheckBox jCheckBox = new JCheckBox("true?");
			jCheckBox.setSelected((Boolean) value);
			return jCheckBox;
		} else if (type.isEnum()) {
			return new JLabel(String.valueOf(value));
		} else if (type.isArray()) {
			return new JButton(String.valueOf(value) + " ...");
		} else {
			return new JButton(String.valueOf(value) + " ...");
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object typeValuePair, boolean isSelected, int row,
			int column) {
		this.type = ((TypedValue) typeValuePair).getType();
		final Object value = ((TypedValue) typeValuePair).getValue();

		if (isNumberClass(type)) {
			editor = new NumberSpinner((Class<? extends Number>) type, (Number) value);
		} else if (type == char.class) {
			editor = new JTextField(value == null ? "" : String.valueOf(value));
		} else if (type == boolean.class) {
			JCheckBox jCheckBox = new JCheckBox();
			jCheckBox.setSelected((Boolean) value);
			jCheckBox.setBackground(Color.white);
			jCheckBox.addItemListener((e) -> stopCellEditing());
			editor = jCheckBox;
		} else if (type.isEnum()) {
			EnumComboBox jEnumComboBox = new EnumComboBox((Class<? extends Enum<?>>) type, (Enum<?>) value);
			jEnumComboBox.addPopupMenuListener(new PopupMenuListener() {
				@Override
				public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
				}

				@Override
				public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
					stopCellEditing();
				}

				@Override
				public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
					stopCellEditing();
				}
			});
			editor = jEnumComboBox;
		} else {
			JButton selectObjectButton = new JButton(
					value == null ? "null" : value.getClass().getSimpleName() + "#" + value.hashCode() + " ...");
			selectObjectButton.addActionListener((e) -> {
				SelectObjectFrame frame = new SelectObjectFrame();
				frame.addListener(new SelectObjectListener() {
					@Override
					public void onSelect(Object value) {
						selectedValue = value;
						stopCellEditing();
					}

					@Override
					public void onCancel() {
						stopCellEditing();
					}
				});
				frame.setVisible(true);
			});
			editor = selectObjectButton;
		}
		return editor;
	}

	@Override
	public Object getCellEditorValue() {
		Object value;
		if (isNumberClass(type)) {
			value = ((NumberSpinner) editor).getPrimitiveValue();
		} else if (type == char.class || type == Character.class) {
			value = ((JTextField) editor).getText().charAt(0);
		} else if (type == boolean.class || type == Boolean.class) {
			value = ((JCheckBox) editor).isSelected();
		} else if (type == String.class) {
			value = ((JTextField) editor).getText();
		} else if (type.isEnum()) {
			value = ((EnumComboBox) editor).getSelectedItem();
		} else if (type.isArray()) {
			value = this.selectedValue;
		} else {
			value = this.selectedValue;
		}

		return new TypedValue(type, value);
	}

	private boolean isNumberClass(Class<?> type) {
		return type == byte.class || type == short.class || type == int.class || type == long.class
				|| type == float.class || type == double.class;
	}
}
