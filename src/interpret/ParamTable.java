package interpret;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class ParamTable extends JTable {

	private final String[] columnNames = { "Type", "Value" };

	private static final Class<?>[] EMPTY_TYPES = new Class<?>[0];
	private static final Class<?>[] EMPTY_PARAMS = new Class<?>[0];

	private Class<?>[] types = EMPTY_TYPES;
	private Object[] params = EMPTY_PARAMS;

	public ParamTable() {
		setModel(new ArgumentTableModel());
		getColumn(columnNames[0]).setCellRenderer(new ClassColumnRenderer());
		ObjectCellEditor valueCell = new ObjectCellEditor();
		getColumn(columnNames[1]).setCellRenderer(valueCell);
		getColumn(columnNames[1]).setCellEditor(valueCell);
	}

	public void setClass(Class<?>... types) {
		if (types == null) {
			this.types = EMPTY_TYPES;
			params = EMPTY_PARAMS;
		} else {
			this.types = types;
			params = new Object[types.length];
		}

		for (int i = 0; i < this.types.length; i++) {
			if (types[i] == byte.class || types[i] == Byte.class) {
				params[i] = (byte) 0;
			} else if (types[i] == short.class || types[i] == Short.class) {
				params[i] = (short) 0;
			} else if (types[i] == int.class || types[i] == Integer.class) {
				params[i] = 0;
			} else if (types[i] == long.class || types[i] == Long.class) {
				params[i] = 0L;
			} else if (types[i] == float.class || types[i] == Float.class) {
				params[i] = (float) 0;
			} else if (types[i] == double.class || types[i] == Double.class) {
				params[i] = (double) 0;
			} else if (types[i] == char.class || types[i] == Character.class) {
				params[i] = ' ';
			} else if (types[i] == boolean.class || types[i] == Boolean.class) {
				params[i] = false;
			} else if (types[i] == String.class) {
				params[i] = "";
			} else if (types[i].isEnum()) {
				params[i] = null;
			} else if (types[i].isArray()) {
				params[i] = null;
			} else {
				params[i] = null;
			}
		}
		updateUI();
	}

	public Object[] getValues() {
		return params;
	}

	private class ArgumentTableModel extends AbstractTableModel {

		@Override
		public int getRowCount() {
			return types.length;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int i) {
			return columnNames[i];
		}

		@Override
		public boolean isCellEditable(int i, int j) {
			switch (j) {
			case 0:
				return false;
			case 1:
				return true;
			default:
				throw new AssertionError("");
			}
		}

		@Override
		public void setValueAt(Object value, int i, int j) {
			switch (j) {
			case 0:
				break;
			case 1:
				params[i] = ((TypedValue) value).getValue();
				break;
			default:
				throw new AssertionError("");
			}
		}

		@Override
		public Object getValueAt(int i, int j) {
			switch (j) {
			case 0:
				return types[i];
			case 1:
				return new TypedValue(types[i], params[i]);
			default:
				throw new AssertionError("");
			}
		}
	}

	private class ClassColumnRenderer implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int i, int j) {
			return new JLabel(((Class<?>) value).getCanonicalName().replaceAll("java\\.lang\\.", ""));
		}
	}

}
