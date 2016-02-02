package interpret;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SelectObjectFrame extends JFrame {

	Set<SelectObjectListener> listeners = new HashSet<>();
	CreatedObjectList createdList;
	JPanel createdListPanel = new JPanel();
	JScrollPane createdListPane = new JScrollPane();
	JPanel buttonPanel = new JPanel();

	Object selectedObject;

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	JTextField literalField = new JTextField();
	JCheckBox useLiteralCheck = new JCheckBox("Use Inputed String below");

	public SelectObjectFrame() {
		this(null);
	}

	public SelectObjectFrame(List<Object> createdObjects) {
		createdList = createdObjects == null ? new CreatedObjectList() : new CreatedObjectList(createdObjects);
		setupLayout();
		setupListener();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
	}

	public interface SelectObjectListener {
		public void onSelect(Object obj);

		public void onCancel();
	}

	private void setupLayout() {
		setSize(500, 600);
		setTitle("Select object");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(BorderLayout.CENTER, createdListPanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);
		createdListPanel.setLayout(new GridLayout(2, 1));
		createdListPanel.add(createdListPane);
		JPanel literalPanel = new JPanel();
		literalPanel.setLayout(new BorderLayout());
		literalPanel.add(BorderLayout.NORTH, useLiteralCheck);
		literalPanel.add(BorderLayout.CENTER, literalField);
		createdListPanel.add(literalPanel);
		createdListPane.setViewportView(createdList);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);

		add(mainPanel);
	}

	private void setupListener() {
		okButton.addActionListener(e -> {
			Object obj = useLiteralCheck.isSelected() ? literalField.getText() : selectedObject;
			for (SelectObjectListener listener : listeners) {
				listener.onSelect(obj);
			}
			dispose();
		});
		cancelButton.addActionListener(e -> onCancel());
		createdList.addListener(obj -> selectedObject = obj);
	}

	public void addListener(SelectObjectListener listener) {
		listeners.add(listener);
	}

	private void onCancel() {
		for (SelectObjectListener listener : listeners) {
			listener.onCancel();
		}
		dispose();
	}
}
