package interpret;

public class TypedValue implements Cloneable {

	private Class<?> type;
	private Object value;

	public TypedValue(Class<?> type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getLabelName() {
		return value == null ? "null" : (type.getSimpleName() + "#" + value.hashCode());
	}

	@Override
	public TypedValue clone() {
		try {
			return (TypedValue) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("Clone is supported.");
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TypedValue) {
			return false;
		}

		TypedValue typeValuePair = (TypedValue) other;
		return type.equals(typeValuePair.type) && value.equals(typeValuePair.value);
	}

	@Override
	public int hashCode() {
		return type.hashCode() ^ value.hashCode();
	}

	@Override
	public String toString() {
		return String.format("[%s:%s]", type.getCanonicalName(), value);
	}
}
