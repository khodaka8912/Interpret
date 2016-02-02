package interpret;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class FieldTable extends ObjectTable {

	private static final String[] COLUMN_NAMES = { "Modifier", "Type", "Name", "Value" };

	private Object object;
	private Field[] fields = new Field[0];

	public FieldTable(List<Object> object) {
		super(object);
		setModel(new ArgumentsTableModel());
		getColumn(COLUMN_NAMES[0]).setCellRenderer(
				(table, value, isSelected, hasFocus, i, j) -> new JLabel(Modifier.toString(fields[i].getModifiers())));
		getColumn(COLUMN_NAMES[1]).setCellRenderer(
				(table, value, isSelected, hasFocus, i, j) -> new JLabel(fields[i].getType().getCanonicalName()));
		getColumn(COLUMN_NAMES[2])
				.setCellRenderer((table, value, isSelected, hasFocus, i, j) -> new JLabel(fields[i].getName()));
		getColumn(COLUMN_NAMES[3]).setCellRenderer(editor);
		getColumn(COLUMN_NAMES[3]).setCellEditor(editor);
	}

	public void setObject(Object object) {
		this.object = object;
		if (object == null) {
			fields = new Field[0];
		} else {
			fields = object.getClass().getDeclaredFields();
		}
		updateUI();
	}

	private class ArgumentsTableModel extends AbstractTableModel {

		@Override
		public int getRowCount() {
			return fields.length;
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
				return false;
			case 2:
				return false;
			case 3:
				return ReflectUtils.isSettableField(fields[i]);
			default:
				throw new AssertionError("");
			}
		}

		@Override
		public void setValueAt(Object value, int i, int j) {
			switch (j) {
			case 0:
			case 1:
			case 2:
				break;
			case 3:
				try {
					ReflectUtils.setField(object, fields[i], ((TypedValue) value).getValue());
				} catch (Throwable e) {
					JOptionPane.showMessageDialog(FieldTable.this,
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
				return Modifier.toString(fields[i].getModifiers());
			case 1:
				return fields[i].getType();
			case 2:
				return fields[i].getName();
			case 3:
				try {
					return new TypedValue(fields[i].getType(), ReflectUtils.getField(object, fields[i]));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			default:
				throw new AssertionError("");
			}
		}
	}
}
