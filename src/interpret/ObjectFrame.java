package interpret;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ObjectFrame extends JFrame {
	private final Object object;

	private final JPanel methodPanel = new JPanel();
	private final JPanel methodInnerPanel = new JPanel();
	private final JPanel searchPanel = new JPanel();
	private final JScrollPane methodListScrollPane = new JScrollPane();
	private final MethodList methodList = new MethodList();
	private final JScrollPane paramTableScrollPane = new JScrollPane();
	private final ParamTable paramTable;
	private final JButton invokeButton = new JButton("Invoke");
	private final JTextField searchField = new JTextField();
	private final JButton searchButton = new JButton("Search");
	private final JButton clearButton = new JButton("Clear");

	private final JPanel fieldPanel = new JPanel();
	private final JScrollPane fieldsTableScrollPane = new JScrollPane();
	private final FieldTable fieldTable;

	private final JPanel buttonPanel = new JPanel();
	private final JSplitPane mainSplitPane = new JSplitPane();

	public ObjectFrame(Object object, List<Object> createdObjects) {
		this.object = object;
		paramTable = new ParamTable(createdObjects);
		methodList.setClass(object.getClass());
		fieldTable = new FieldTable(createdObjects);
		fieldTable.setObject(object);
		setupLayout();
		setupListener();
	}

	private void setupListener() {
		methodList.addListener(m -> paramTable.setClass(m == null ? (Class<?>[]) null : m.getParameterTypes()));
		invokeButton.addActionListener(e -> {
			Method method = methodList.getSelectedMethod();
			if (method != null) {
				try {
					Object returnValue = ReflectUtils.invoke(object, method, paramTable.getValues());
					if (returnValue == null) {
						JOptionPane.showMessageDialog(ObjectFrame.this, "void");
					} else {
						JOptionPane.showMessageDialog(ObjectFrame.this, String.valueOf(returnValue));
					}
				} catch (Throwable t) {
					JOptionPane.showMessageDialog(ObjectFrame.this, t.toString());
				}
			}
		});
		searchButton.addActionListener(e -> methodList.filter(searchField.getText()));
		clearButton.addActionListener(e -> {
			searchField.setText("");
			methodList.filter(null);
		});
	}

	private void setupLayout() {
		setTitle(object.getClass().getSimpleName() + "#" + object.hashCode());
		setSize(900, 600);

		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, mainSplitPane);
		getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		getContentPane().add(BorderLayout.NORTH, searchPanel);

		mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane.setDividerLocation(300);
		mainSplitPane.setTopComponent(methodPanel);
		mainSplitPane.setBottomComponent(fieldPanel);

		methodPanel.setLayout(new BorderLayout());
		methodPanel.add(BorderLayout.NORTH, new JLabel("Method(s)"));
		methodPanel.add(BorderLayout.CENTER, methodInnerPanel);
		methodPanel.add(BorderLayout.SOUTH, invokeButton);
		methodInnerPanel.setLayout(new GridLayout(1, 2));
		methodInnerPanel.add(methodListScrollPane);
		methodListScrollPane.setViewportView(methodList);
		JPanel paramPanel = new JPanel();
		paramPanel.setLayout(new BorderLayout());
		paramPanel.add(BorderLayout.NORTH, new JLabel("Method Param(s)"));
		paramPanel.add(BorderLayout.CENTER, paramTableScrollPane);
		methodInnerPanel.add(paramPanel);
		paramTableScrollPane.setViewportView(paramTable);

		fieldPanel.setLayout(new BorderLayout());
		fieldPanel.add(BorderLayout.NORTH, new JLabel("Field(s)"));
		fieldPanel.add(BorderLayout.CENTER, fieldsTableScrollPane);
		fieldsTableScrollPane.setViewportView(fieldTable);

		searchPanel.setLayout(new BorderLayout());
		searchPanel.add(BorderLayout.NORTH, new JLabel("Method search"));
		JPanel searchInnerPanel = new JPanel();
		searchInnerPanel.setLayout(new GridLayout(1, 3));
		searchInnerPanel.add(searchField);
		searchInnerPanel.add(searchButton);
		searchInnerPanel.add(clearButton);
		searchPanel.add(BorderLayout.CENTER, searchInnerPanel);

		setLocationRelativeTo(null);
	}
}
