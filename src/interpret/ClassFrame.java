package interpret;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class ClassFrame extends JFrame {

	private Object selectedObject = null;
	private List<Object> createdObjects = new ArrayList<>();
	private final CreatedObjectList createdList = new CreatedObjectList(createdObjects);

	private final JPanel topGroup = new JPanel();
	private final JPanel classNamePanel = new JPanel();
	private final ClassNameField classNameField = new ClassNameField();
	private final JButton showConstructorButton = new JButton("Show constructors");
	private final JPanel arrayPanel = new JPanel();
	private final JSpinner arrayLengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0,
			Integer.MAX_VALUE, 1));
	private final JButton createArrayButton = new JButton("Create Array");

	private final JPanel mainPanel = new JPanel();
	private final JScrollPane constructorListPane = new JScrollPane();
	private final JPanel constructorListPanel = new JPanel();
	private final ConstructorList constructorList = new ConstructorList();
	private final JPanel paramTablePanel = new JPanel();
	private final JScrollPane paramTablePane = new JScrollPane();
	private final ParamTable paramTable = new ParamTable(createdObjects);

	private final JPanel createdListPanel = new JPanel();
	private final JScrollPane createdListPane = new JScrollPane();
	private final JButton constructButton = new JButton("Create instance");
	private final JButton showObjectButton = new JButton("Show Object");
	private final JButton editArrayButton = new JButton("Edit Array");

	public ClassFrame() {
		setupLayouts();
		setupListeners();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void setupListeners() {
		classNameField.addClassChangedListener(cls -> {
			constructorList.setClass(cls);
			constructButton.setEnabled(false);
		});
		constructorList.addConstructorChangedListener(constructor -> {
			paramTable.setClass(constructor == null ? null : constructor.getParameterTypes());
			constructButton.setEnabled(constructor != null);
		});
		createdList.addListener(obj -> {
			selectedObject = obj;
			showObjectButton.setEnabled(obj != null);
			editArrayButton.setEnabled(obj != null && obj.getClass().isArray());
		});
		constructButton.addActionListener(e -> {
			try {
				Constructor<?> constructor = constructorList.getSelectedConstructor();
				Object[] arguments = paramTable.getValues();
				Object object = ReflectUtils.construct(constructor, arguments);
				createdList.addObject(object);
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(ClassFrame.this, t.toString());
			}
		});
		showConstructorButton.addActionListener(e -> classNameField.updateClass());
		showObjectButton.addActionListener(e -> {
//			if (selectedObject.getClass().isArray()) {
//				ArrayFrame arrayViewer = new ArrayFrame(selectedObject, createdObjects);
//				arrayViewer.setVisible(true);
//			} else {
				ObjectFrame objectViewer = new ObjectFrame(selectedObject, createdObjects);
				objectViewer.setVisible(true);
//			}
		});
		editArrayButton.addActionListener(e -> {
			if (!selectedObject.getClass().isArray()) {
				return;
			}
			ArrayFrame arrayViewer = new ArrayFrame(selectedObject, createdObjects);
			arrayViewer.setVisible(true);
		});
		createArrayButton.addActionListener(e -> {
			classNameField.updateClass();
			try {
				Class<?> cls = classNameField.getClassObject();
				if (cls == null) {
					
					JOptionPane.showMessageDialog(ClassFrame.this, "Class not found.");
					return;
				}
				int length = Integer.parseInt(arrayLengthSpinner.getValue().toString());
				Object array = Array.newInstance(cls, length);
				createdList.addObject(array);
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(ClassFrame.this, "Invalid array length.");
			}
		});
	}

	private void setupLayouts() {
		setTitle("Interpret");
		setSize(800, 600);

		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH, topGroup);
		getContentPane().add(BorderLayout.CENTER, mainPanel);
		// getContentPane().add(BorderLayout.SOUTH, createdListPanel);

		// topGroup.setLayout(new GridLayout(1, 2));
		topGroup.add(classNamePanel);
		topGroup.add(arrayPanel);

		// Setup Class Name area layout
		classNamePanel.setLayout(new GridLayout(2, 1));
		classNamePanel.add(new JLabel("Input class name(java.lang. can be omitted)."));
		JPanel classNameInnerPanel = new JPanel();
		classNameInnerPanel.setLayout(new GridLayout(1, 2));
		classNameInnerPanel.add(classNameField);
		classNameInnerPanel.add(showConstructorButton);
		classNamePanel.add(classNameInnerPanel);

		arrayPanel.setLayout(new GridLayout(2, 1));
		arrayPanel.add(new JLabel("Create array using inputed class."));
		JPanel arrayInnerPanel = new JPanel();
		arrayInnerPanel.setLayout(new FlowLayout());
		arrayInnerPanel.add(new JLabel("length"));
		arrayInnerPanel.add(arrayLengthSpinner);
		arrayInnerPanel.add(createArrayButton);
		arrayPanel.add(arrayInnerPanel);

		mainPanel.setLayout(new GridLayout(2, 1));
		JPanel constructorPanel = new JPanel();
		constructorPanel.setLayout(new GridLayout(1, 2));
		constructorPanel.add(constructorListPanel);
		constructorPanel.add(paramTablePanel);
		mainPanel.add(constructorPanel);
		mainPanel.add(createdListPanel);

		constructorListPanel.setLayout(new BorderLayout());
		constructorListPanel.add(BorderLayout.NORTH, new JLabel("Constructor(s)"));
		constructorListPanel.add(BorderLayout.CENTER, constructorListPane);
		constructorListPane.setViewportView(constructorList);

		paramTablePanel.setLayout(new BorderLayout());
		paramTablePanel.add(BorderLayout.NORTH, new JLabel("Constructor Param(s)"));
		paramTablePanel.add(BorderLayout.CENTER, paramTablePane);
		paramTablePanel.add(BorderLayout.SOUTH, constructButton);
		paramTablePane.setViewportView(paramTable);

		createdListPanel.setLayout(new BorderLayout());
		createdListPanel.add(BorderLayout.NORTH, new JLabel("Created Object(s)"));
		createdListPanel.add(BorderLayout.CENTER, createdListPane);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(showObjectButton);
		buttonPanel.add(editArrayButton);
		showObjectButton.setEnabled(false);
		editArrayButton.setEnabled(false);
		createdListPanel.add(BorderLayout.SOUTH, buttonPanel);
		createdListPane.setViewportView(createdList);

		setLocationRelativeTo(null);
	}
}
