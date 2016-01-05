package interpret;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interpret.MethodList.MethodChangedListener;

@SuppressWarnings("serial")
public class ObjectFrame extends SubFrame {
	private final Object object;

	private final JPanel methodPanel = new JPanel();
	private final JTextField filterTextField = new JTextField();
	private final JSplitPane methodSplitPane = new JSplitPane();
	private final JScrollPane methodListScrollPane = new JScrollPane();
	private final MethodList methodList = new MethodList();
	private final JScrollPane paramTableScrollPane = new JScrollPane();
	private final ParamTable paramTable = new ParamTable();
	private final JButton invokeButton = new JButton("Invoke");

	private final JPanel fieldPanel = new JPanel();
	private final JScrollPane fieldsTableScrollPane = new JScrollPane();
	private final FieldTable fieldsTable = new FieldTable();

	private final JPanel buttonPanel = new JPanel();
	private final JButton cancelButton = new JButton("Cancel");
	private final JButton returnNullButton = new JButton("Return null");
	private final JButton returnInstanceButton = new JButton("Return instance");

	private final JSplitPane mainSplitPane = new JSplitPane();

	public ObjectFrame(Object object, DialogListener dialogListener) {
		super(dialogListener);
		this.object = object;
		methodList.setClass(object.getClass());
		fieldsTable.setObject(object);
		setupLayout();
		setupListener();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				onCancel();
			}
		});
	}

	private final DocumentListener filterTextChangedListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent documentEvent) {
			methodList.setFilterText(filterTextField.getText());
		}

		@Override
		public void removeUpdate(DocumentEvent documentEvent) {
			methodList.setFilterText(filterTextField.getText());
		}

		@Override
		public void changedUpdate(DocumentEvent documentEvent) {

		}
	};

	private final MethodChangedListener methodChangedListener = new MethodChangedListener() {
		@Override
		public void onChange(Method method) {
			if (method == null) {
				paramTable.setClass((Class<?>[]) null);
			} else {
				paramTable.setClass(method.getParameterTypes());
			}
		}
	};

	private final ActionListener invokeButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			Method method = methodList.getSelectedMethod();
			if (method != null) {
				try {
					Object returnValue = ReflectUtils.invoke(object, method, paramTable.getValues());
					if (returnValue == null) {
						JOptionPane.showMessageDialog(ObjectFrame.this, "void");
					} else {
						JOptionPane.showMessageDialog(ObjectFrame.this, String.valueOf(returnValue));
					}
				} catch (Throwable e) {
					JOptionPane.showMessageDialog(ObjectFrame.this, e.toString());
				}
			}
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

	private final ActionListener returnInstanceButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			onReturn(object);
		}
	};

	private void setupListener() {
		filterTextField.getDocument().addDocumentListener(filterTextChangedListener);
		methodList.addMethodChangedListener(methodChangedListener);
		invokeButton.addActionListener(invokeButtonListener);
		cancelButton.addActionListener(cancelButtonListener);
		returnNullButton.addActionListener(returnNullButtonListener);
		returnInstanceButton.addActionListener(returnInstanceButtonListener);
	}

	private void setupLayout() {
		setTitle("Instance of " + object.getClass().getCanonicalName());
		setSize(900, 600);

		setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, mainSplitPane);
		getContentPane().add(BorderLayout.SOUTH, buttonPanel);

		// Setup Center area layout
		mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane.setDividerLocation(300);
		mainSplitPane.setTopComponent(methodPanel);
		mainSplitPane.setBottomComponent(fieldPanel);

		// Setup Method Panel layout
		methodPanel.setBorder(BorderFactory.createTitledBorder("Method(s)"));
		methodPanel.setLayout(new BorderLayout());
		methodPanel.add(BorderLayout.NORTH, filterTextField);
		methodPanel.add(BorderLayout.CENTER, methodSplitPane);
		methodPanel.add(BorderLayout.SOUTH, invokeButton);
		methodSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		methodSplitPane.setDividerLocation(500);
		methodSplitPane.setTopComponent(methodListScrollPane);
		methodListScrollPane.setViewportView(methodList);
		methodSplitPane.setBottomComponent(paramTableScrollPane);
		paramTableScrollPane.setViewportView(paramTable);

		fieldPanel.setBorder(BorderFactory.createTitledBorder("Field"));
		fieldPanel.setLayout(new BorderLayout());
		fieldPanel.add(BorderLayout.CENTER, fieldsTableScrollPane);
		fieldsTableScrollPane.setViewportView(fieldsTable);

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(cancelButton);
		buttonPanel.add(returnNullButton);
		buttonPanel.add(returnInstanceButton);

		// Locate JFrame to center of screen
		setLocationRelativeTo(null); // Should be called after setup layout
	}
}
