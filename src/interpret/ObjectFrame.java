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
	private void setupListener() {
		methodList.addMethodChangedListener(methodChangedListener);
		invokeButton.addActionListener(invokeButtonListener);
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

		// Locate JFrame to center of screen
		setLocationRelativeTo(null); // Should be called after setup layout
	}
}
