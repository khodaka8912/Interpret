package interpret;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ArrayFrame extends JFrame {

	private final Object[] array;

	public ArrayFrame(Object[] array) {
		this.array = array;
		arraysTable.setArray(array);
		setupLayout();
	}

	private final JScrollPane arraysTableScrollPane = new JScrollPane();
	private final ArrayTable arraysTable = new ArrayTable();




	private void setupLayout() {
		setTitle(array.getClass().getCanonicalName());
		setSize(800, 600);

		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, arraysTableScrollPane);

		arraysTableScrollPane.setViewportView(arraysTable);

		setLocationRelativeTo(null);
	}
}
