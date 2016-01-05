package interpret;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ArrayFrame extends SubFrame {

	private final Object[] array;

	public ArrayFrame(Object[] array, DialogListener dialogListener) {
		super(dialogListener);
		this.array = array;
		arraysTable.setArray(array);
		setupLayout();
		setupListener();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				onCancel();
			}
		});
	}

	private final JScrollPane arraysTableScrollPane = new JScrollPane();
	private final ArrayTable arraysTable = new ArrayTable();

	private final JPanel buttonPanel = new JPanel();
	private final JButton cancelButton = new JButton("Cancel");
	private final JButton returnNullButton = new JButton("Return null");
	private final JButton returnArrayButton = new JButton("Return array");

	// Setup component event listener

	private final ActionListener cancelButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			onCancel();
		}
	};

	private final ActionListener returnNullButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			onReturn(null);
		}
	};

	private final ActionListener returnArrayButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			onReturn(array);
		}
	};

	private void setupListener() {
		cancelButton.addActionListener(cancelButtonListener);
		returnNullButton.addActionListener(returnNullButtonListener);
		returnArrayButton.addActionListener(returnArrayButtonListener);
	}

	private void setupLayout() {
		setTitle(array.getClass().getCanonicalName());
		setSize(900, 600);

		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, arraysTableScrollPane);
		getContentPane().add(BorderLayout.SOUTH, buttonPanel);

		// Setup Center area layout
		arraysTableScrollPane.setViewportView(arraysTable);

		// Setup Button Panel
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(cancelButton);
		buttonPanel.add(returnNullButton);
		buttonPanel.add(returnArrayButton);

		// Locate JFrame to center of screen
		setLocationRelativeTo(null); // Should be called after setup layout
	}
}
