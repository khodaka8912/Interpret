package interpret;

import java.util.List;

import javax.swing.JTable;

public class ObjectTable extends JTable {

	protected final ObjectCellEditor editor;

	public ObjectTable(List<Object> createdObjects) {
		editor = new ObjectCellEditor(createdObjects);
	}
}
