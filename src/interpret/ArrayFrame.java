package interpret;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ArrayFrame extends JFrame {

	private final Object[] array;
	private final JScrollPane arraysTableScrollPane = new JScrollPane();
	private final ArrayTable arrayTable;

	public ArrayFrame(Object[] array, List<Object> createdObjects) {
		this.array = array;
		arrayTable = new ArrayTable(createdObjects);
		arrayTable.setArray(array);
		setupLayout();
	}

	private void setupLayout() {
		setTitle(array.getClass().getCanonicalName());
		setSize(800, 600);

		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, arraysTableScrollPane);

		arraysTableScrollPane.setViewportView(arrayTable);

		setLocationRelativeTo(null);
	}
}
