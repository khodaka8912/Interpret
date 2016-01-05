package interpret;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class NumberSpinner extends JSpinner {

	private final Class<? extends Number> numberType;

	public NumberSpinner(Class<? extends Number> numberType, Number initialValue) {
		this.numberType = numberType;

		// Set model
		SpinnerModel model;
		if (numberType == byte.class || numberType == Byte.class) {
			model = new SpinnerNumberModel(initialValue.byteValue(), Byte.MIN_VALUE, Byte.MAX_VALUE, 1);
		} else if (numberType == short.class || numberType == Short.class) {
			model = new SpinnerNumberModel(initialValue.shortValue(), Short.MIN_VALUE, Short.MAX_VALUE, 1);
		} else if (numberType == int.class || numberType == Integer.class) {
			model = new SpinnerNumberModel(initialValue.intValue(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
		} else if (numberType == long.class || numberType == Long.class) {
			model = new SpinnerNumberModel(initialValue.longValue(), Long.MIN_VALUE, Long.MAX_VALUE, 1);
		} else if (numberType == float.class || numberType == Float.class) {
			model = new SpinnerNumberModel(initialValue.floatValue(), 0, 0, 1);
		} else if (numberType == double.class || numberType == Double.class) {
			model = new SpinnerNumberModel(initialValue.doubleValue(), 0, 0, 1);
		} else {
			throw new IllegalArgumentException("type must be primitive or wrapper");
		}
		setModel(model);
	}

	public Number getPrimitiveValue() {
		if (numberType == byte.class || numberType == Byte.class) {
			return ((Number) getValue()).byteValue();
		} else if (numberType == short.class || numberType == Short.class) {
			return ((Number) getValue()).shortValue();
		} else if (numberType == int.class || numberType == Integer.class) {
			return ((Number) getValue()).intValue();
		} else if (numberType == long.class || numberType == Long.class) {
			return ((Number) getValue()).longValue();
		} else if (numberType == float.class || numberType == Float.class) {
			return ((Number) getValue()).floatValue();
		} else if (numberType == double.class || numberType == Double.class) {
			return ((Number) getValue()).doubleValue();
		} else {
			throw new IllegalArgumentException("type must be primitive or wrapper");
		}
	}
}