package interpret;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ArrayTable extends ObjectTable {

	private static final String[] COLUMN_NAMES = { "Index", "Value" };

	private Object[] array;

	public ArrayTable(List<Object> createdObjects) {
		super(createdObjects);
		setModel(new ArraysTableModel());
		getColumn(COLUMN_NAMES[0])
				.setCellRenderer((table, value, isSelected, hasFocus, i, j) -> new JLabel(String.valueOf(value)));
		getColumn(COLUMN_NAMES[1]).setCellRenderer(editor);
		getColumn(COLUMN_NAMES[1]).setCellEditor(editor);
	}

	public void setArray(Object[] array) {
		this.array = array;
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
				try {
					array[i] = ((TypedValue) value).getValue();					
				} catch (ArrayStoreException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(ArrayTable.this,
							e.getClass().getSimpleName() + ": " + e.getMessage());					
				}
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