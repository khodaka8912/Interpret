package interpret;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ArrayFrame extends JFrame {

	private final Object[] array;
	private final JScrollPane arraysTableScrollPane = new JScrollPane();
	private final ArrayTable arraysTable = new ArrayTable();

	public ArrayFrame(Object[] array) {
		this.array = array;
		arraysTable.setArray(array);
		setupLayout();
	}

	private void setupLayout() {
		setTitle(array.getClass().getCanonicalName());
		setSize(800, 600);

		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, arraysTableScrollPane);

		arraysTableScrollPane.setViewportView(arraysTable);

		setLocationRelativeTo(null);
	}
}
