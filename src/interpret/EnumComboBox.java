package interpret;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class EnumComboBox extends JComboBox<Enum<?>> {

	private final Enum<?>[] enumConstants;

	private Enum<?> selectedItem;

	public EnumComboBox(Class<? extends Enum<?>> type, Enum<?> initialValue) {
		enumConstants = type.getEnumConstants();

		selectedItem = initialValue;

		setModel(new EnumListModel());
	}

	private class EnumListModel extends AbstractListModel<Enum<?>>implements ComboBoxModel<Enum<?>> {
		@Override
		public int getSize() {
			return enumConstants.length;
		}

		@Override
		public Enum<?> getElementAt(int i) {
			if (i >= enumConstants.length) {
				return null;
			} else {
				return enumConstants[i];
			}
		}

		@Override
		public void setSelectedItem(Object value) {
			selectedItem = (Enum<?>) value;
		}

		@Override
		public Object getSelectedItem() {
			return selectedItem;
		}
	}
}
