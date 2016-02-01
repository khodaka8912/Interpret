package interpret;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import interpret.ClassNameField.ClassChangedListener;
import interpret.ConstructorList.ConstructorChangedListener;
import interpret.CreatedObjectList.ObjectChangedListener;

@SuppressWarnings("serial")
public class ClassFrame extends JFrame {

	private final JPanel topGroup = new JPanel();
	private final JPanel classNamePanel = new JPanel();
	private final ClassNameField classNameField = new ClassNameField();
	private final JButton showConstructorButton = new JButton("Show constructors");
	private final JPanel arrayPanel = new JPanel();
	private final JSpinner arrayLengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	private final JButton createArrayButton = new JButton("Create Array");

	private final JPanel mainPanel = new JPanel();
	private final JScrollPane constructorListPane = new JScrollPane();
	private final JPanel constructorListPanel = new JPanel();
	private final ConstructorList constructorList = new ConstructorList();
	private final JPanel paramTablePanel = new JPanel();
	private final JScrollPane paramTablePane = new JScrollPane();
	private final ParamTable paramTable = new ParamTable();

	private final JPanel createdListPanel = new JPanel();
	private final JScrollPane createdListPane = new JScrollPane();
	private final CreatedObjectList createdList = new CreatedObjectList();
	private final JButton constructButton = new JButton("Create instance");
	private final JButton showObjectButton = new JButton("Show object");

	public ClassFrame() {
		setupLayout();
		setupListener();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
	}

	private final ClassChangedListener classChangedListener = new ClassChangedListener() {
		@Override
		public void onChange(Class<?> class_) {
			constructorList.setClass(class_);
			constructButton.setEnabled(false);
		}
	};

	private final ConstructorChangedListener constructorChangedListener = new ConstructorChangedListener() {
		@Override
		public void onChange(Constructor<?> constructor) {
			paramTable.setClass(constructor == null ? null : constructor.getParameterTypes());
			constructButton.setEnabled(constructor != null);
		}
	};

	private final ActionListener showConstructorButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			classNameField.updateClass();
		}
	};

	private final ActionListener createArrayButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			classNameField.updateClass();
			try {
				Class<?> cls = classNameField.getClassObject();
				if (cls == null) {
					JOptionPane.showMessageDialog(ClassFrame.this, "Class not found.");
					return;
				}
				int length = Integer.parseInt(arrayLengthSpinner.getValue().toString());
				Object[] array = (Object[]) Array.newInstance(cls, length);
				createdList.addObject(array);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(ClassFrame.this, "Invalid array length.");
			}
		}
	};

	private final ActionListener constructButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				Constructor<?> constructor = constructorList.getSelectedConstructor();
				Object[] arguments = paramTable.getValues();
				Object object = ReflectUtils.construct(constructor, arguments);
				createdList.addObject(object);
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(ClassFrame.this, e.toString());
			}
		}
	};

	private void setupListener() {
		classNameField.addClassChangedListener(classChangedListener);
		constructorList.addConstructorChangedListener(constructorChangedListener);
		createdList.addListener(objectChangedListener);
		constructButton.addActionListener(constructButtonListener);
		showConstructorButton.addActionListener(showConstructorButtonListener);
		showObjectButton.addActionListener(showObjectButtonListener);
		createArrayButton.addActionListener(createArrayButtonListener);
	}

	private void setupLayout() {
		setTitle("Interpret");
		setSize(800, 600);

		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH, topGroup);
		getContentPane().add(BorderLayout.CENTER, mainPanel);
		// getContentPane().add(BorderLayout.SOUTH, createdListPanel);

		// topGroup.setLayout(new BorderLayout());
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
		createdListPanel.add(BorderLayout.SOUTH, showObjectButton);
		createdListPane.setViewportView(createdList);

		setLocationRelativeTo(null);
	}

	private Object selectedObject = null;

	private ObjectChangedListener objectChangedListener = new ObjectChangedListener() {

		@Override
		public void onChange(Object object) {
			selectedObject = object;
			showObjectButton.setEnabled(object != null);
		}
	};

	private ActionListener showObjectButtonListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (selectedObject.getClass().isArray()) {
				ArrayFrame arrayViewer = new ArrayFrame((Object[]) selectedObject);
				arrayViewer.setVisible(true);
			} else {
				ObjectFrame objectViewer = new ObjectFrame(selectedObject);
				objectViewer.setVisible(true);
			}
		}
	};
}
