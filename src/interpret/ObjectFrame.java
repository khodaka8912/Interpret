package interpret;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import interpret.MethodList.MethodChangedListener;

@SuppressWarnings("serial")
public class ObjectFrame extends JFrame {
	private final Object object;

	private final JPanel methodPanel = new JPanel();
	private final JPanel methodInnerPanel = new JPanel();
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

	public ObjectFrame(Object object) {
		this.object = object;
		methodList.setClass(object.getClass());
		fieldsTable.setObject(object);
		setupLayout();
		setupListener();
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
		methodList.addListener(methodChangedListener);
		invokeButton.addActionListener(invokeButtonListener);
	}

	private void setupLayout() {
		setTitle(object.getClass().getSimpleName() + "#" + object.hashCode());
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
		fieldsTableScrollPane.setViewportView(fieldsTable);

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		setLocationRelativeTo(null);
	}
}
