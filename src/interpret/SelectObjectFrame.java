package interpret;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class SelectObjectFrame extends JFrame {

	List<Object> createdObjects = CreatedObjects.getList();
	Set<SelectObjectListener> listeners = new HashSet<>();

	CreatedObjectList createdList = new CreatedObjectList();
	JPanel createdListPanel = new JPanel();
	JScrollPane createdListPane = new JScrollPane();
	JPanel buttonPanel = new JPanel();

	Object selectedObject;

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");

	public SelectObjectFrame() {
		setupLayout();
		setupListener();
	}

	public interface SelectObjectListener {
		public void onSelect(Object obj);

		public void onCancel();
	}

	private void setupLayout() {
		setSize(600, 600);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(BorderLayout.CENTER, createdListPanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);
		createdListPanel.add(createdListPane);
		createdListPane.setViewportView(createdList);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);

		add(mainPanel);
	}

	private void setupListener() {
		okButton.addActionListener((e) -> {
			for (SelectObjectListener listener : listeners) {
				listener.onSelect(selectedObject);
			}
			dispose();
		});
		cancelButton.addActionListener((e) -> {
			for (SelectObjectListener listener : listeners) {
				listener.onCancel();
			}
			dispose();
		});
		createdList.addListener((object) -> {
			selectedObject = object;
		});
	}

	public void addListener(SelectObjectListener listener) {
		listeners.add(listener);
	}
}
