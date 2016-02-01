package interpret;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ArrayTable extends JTable {

	private static final String[] COLUMN_NAMES = { "Index", "Value" };

	private Object[] array;

	public ArrayTable() {
		setModel(new ArraysTableModel());
		getColumn(COLUMN_NAMES[0])
				.setCellRenderer((table, value, isSelected, hasFocus, i, j) -> new JLabel(String.valueOf(value)));
		getColumn(COLUMN_NAMES[1]).setCellRenderer(new ObjectCellEditor());
		getColumn(COLUMN_NAMES[1]).setCellEditor(new ObjectCellEditor());
	}

	public void setArray(Object[] array) {
		this.array = array;

		// Set default values
		final Class<?> type = array.getClass().getComponentType();
		for (int i = 0; i < array.length; i++) {
			if (type == byte.class || type == Byte.class) {
				array[i] = (byte) 0;
			} else if (type == short.class || type == Short.class) {
				array[i] = (short) 0;
			} else if (type == int.class || type == Integer.class) {
				array[i] = 0;
			} else if (type == long.class || type == Long.class) {
				array[i] = 0L;
			} else if (type == float.class || type == Float.class) {
				array[i] = (float) 0;
			} else if (type == double.class || type == Double.class) {
				array[i] = (double) 0;
			} else if (type == char.class || type == Character.class) {
				array[i] = ' ';
			} else if (type == boolean.class || type == Boolean.class) {
				array[i] = false;
			} else if (type == String.class) {
				array[i] = "";
			} else if (type.isEnum()) {
				array[i] = null;
			} else if (type.isArray()) {
				array[i] = null;
			} else {
				array[i] = null;
			}
		}

		updateUI();
	}

	private class ArraysTableModel extends AbstractTableModel {

		@Override
		public int getRowCount() {
			return array.length;
		}

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		@Override
		public String getColumnName(int i) {
			return COLUMN_NAMES[i];
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
				array[i] = ((TypedValue) value).getValue();
				break;
			default:
				throw new AssertionError("");
			}
		}

		@Override
		public Object getValueAt(int i, int j) {
			switch (j) {
			case 0:
				return i;
			case 1:
				return new TypedValue(array.getClass().getComponentType(), array[i]);
			default:
				throw new AssertionError("");
			}
		}
	}
}