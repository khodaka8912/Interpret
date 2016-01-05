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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;

import interpret.ClassNameField.ClassChangedListener;
import interpret.ConstructorList.ConstructorChangedListener;

@SuppressWarnings("serial")
public class ClassFrame extends SubFrame implements DialogListener {

	private final boolean isRootFrame;

	private final JPanel topGroup = new JPanel();
	private final JPanel classNamePanel = new JPanel();
	private final ClassNameField classNameField = new ClassNameField();
	private final JPanel arrayPanel = new JPanel();
	private final JSpinner arrayLengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

	private final JSplitPane mainSplit = new JSplitPane();
	private final JScrollPane constructorListScrollPane = new JScrollPane();
	private final JPanel constructorListPanel = new JPanel();
	private final ConstructorList constructorList = new ConstructorList();
	private final JPanel argumentsTablePanel = new JPanel();
	private final JScrollPane argumentsTableScrollPane = new JScrollPane();
	private final ParamTable argumentsTable = new ParamTable();

	private final JPanel buttonPanel = new JPanel();
	private final JButton cancelButton = new JButton("Cancel");
	private final JButton returnNullButton = new JButton("Return null");
	private final JButton constructArrayButton = new JButton("Construct Array");
	private final JButton constructButton = new JButton("Construct Object");

	public ClassFrame() {
		this(null, null);
	}

	public ClassFrame(Class<?> type, DialogListener dialogListener) {
		super(dialogListener);

		if (type != null && type.isEnum()) {
			throw new IllegalArgumentException("type must not be enum type");
		}

		isRootFrame = (dialogListener == null);
		cancelButton.setVisible(!isRootFrame);
		returnNullButton.setVisible(!isRootFrame);

		setupLayout();
		setupListener();

		if (type != null) {
			if (type.isArray()) {
				type = type.getComponentType();
				constructButton.setVisible(false);
			} else {
				arrayPanel.setVisible(false);
				constructArrayButton.setVisible(false);
			}

			classNameField.setText(type.getCanonicalName());
			classChangedListener.onChange(type);
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (isRootFrame) {
					System.exit(0);
				} else {
					onCancel();
				}
			}
		});
	}

	@Override
	public void onSubFrameReturn(Object value) {
		if (isRootFrame) {
			setVisible(true);
		} else {
			onReturn(value);
		}
	}

	@Override
	public void onSubFrameCancel() {
		if (isRootFrame) {
			setVisible(true);
		} else {
			onCancel();
		}
	}

	private final ClassChangedListener classChangedListener = new ClassChangedListener() {
		@Override
		public void onChange(Class<?> class_) {
			constructorList.setClass(class_);
			constructArrayButton.setEnabled(class_ != null);
			constructButton.setEnabled(false);
		}
	};

	private final ConstructorChangedListener constructorChangedListener = new ConstructorChangedListener() {
		@Override
		public void onChange(Constructor<?> constructor) {
			argumentsTable.setClass(constructor == null ? null : constructor.getParameterTypes());
			constructButton.setEnabled(constructor != null);
		}
	};

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

	private final ActionListener constructArrayButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				Class<?> cls = classNameField.getClassObject();
				int length = Integer.parseInt(arrayLengthSpinner.getValue().toString());
				Object[] array = (Object[]) Array.newInstance(cls, length);
				ArrayFrame arrayViewer = new ArrayFrame(array, ClassFrame.this);
				setVisible(false);
				arrayViewer.setVisible(true);
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
				Object[] arguments = argumentsTable.getValues();
				Object object = ReflectUtils.construct(constructor, arguments);
				if (isRootFrame) {
				ObjectFrame objectViewer = new ObjectFrame(object, ClassFrame.this);
				objectViewer.setVisible(true);
				} else {
					onReturn(object);
				}
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(ClassFrame.this, e.toString());
			}
		}
	};

	private void setupListener() {
		classNameField.addClassChangedListener(classChangedListener);
		constructorList.addConstructorChangedListener(constructorChangedListener);
		cancelButton.addActionListener(cancelButtonListener);
		returnNullButton.addActionListener(returnNullButtonListener);
		constructArrayButton.addActionListener(constructArrayButtonListener);
		constructButton.addActionListener(constructButtonListener);
	}

	// Setup layout

	private void setupLayout() {
		// Setup Frame
		setTitle("ClassFrame");
		setSize(800, 600);

		// Setup whole layout
		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH, topGroup);
		getContentPane().add(BorderLayout.CENTER, mainSplit);
		getContentPane().add(BorderLayout.SOUTH, buttonPanel);

		// Setup North area layout
		topGroup.setLayout(new BorderLayout());
		topGroup.add(BorderLayout.CENTER, classNamePanel);
		topGroup.add(BorderLayout.EAST, arrayPanel);

		// Setup Class Name area layout
		classNamePanel.setBorder(BorderFactory.createTitledBorder("Class Name"));
		classNamePanel.setLayout(new GridLayout(1, 1));
		classNamePanel.add(classNameField);

		// Setup Array Length area layout
		arrayPanel.setBorder(BorderFactory.createTitledBorder("Array Length"));
		arrayPanel.setLayout(new GridLayout(1, 1));
		arrayPanel.add(arrayLengthSpinner);

		// Setup Center area layout
		mainSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mainSplit.setDividerLocation(400);
		mainSplit.setTopComponent(constructorListPanel);
		mainSplit.setBottomComponent(argumentsTablePanel);

		// Setup Constructor List layout
		constructorListPanel.setBorder(BorderFactory.createTitledBorder("Constructors"));
		constructorListPanel.setLayout(new BorderLayout());
		constructorListPanel.add(BorderLayout.CENTER, constructorListScrollPane);
		constructorListScrollPane.setViewportView(constructorList);

		// Setup Argument Table Layout
		argumentsTablePanel.setBorder(BorderFactory.createTitledBorder("Arguments"));
		argumentsTablePanel.setLayout(new BorderLayout());
		argumentsTablePanel.add(BorderLayout.CENTER, argumentsTableScrollPane);
		argumentsTableScrollPane.setViewportView(argumentsTable);

		// Setup Button area layout
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(cancelButton);
		buttonPanel.add(returnNullButton);
		buttonPanel.add(constructArrayButton);
		buttonPanel.add(constructButton);

		// Locate JFrame to center of screen
		setLocationRelativeTo(null);
	}
}
