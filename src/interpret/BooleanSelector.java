package interpret;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class BooleanSelector extends JPanel {

	private final JRadioButton trueBtn = new JRadioButton("true");
	private final JRadioButton falseBtn = new JRadioButton("false");

	public BooleanSelector(boolean value) {
		ButtonGroup group = new ButtonGroup();
		group.add(trueBtn);
		group.add(falseBtn);
		add(trueBtn);
		add(falseBtn);
		trueBtn.setSelected(value);
		falseBtn.setSelected(!value);
	}

	public boolean getValue() {
		return trueBtn.isSelected();
	}

}
