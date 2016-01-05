package interpret;

public class TypeValuePair implements Cloneable {

	private Class<?> type;
	private Object value;

	public TypeValuePair(Class<?> type, Object value) {
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

	@Override
	public TypeValuePair clone() {
		try {
			return (TypeValuePair) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("Clone is supported.");
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TypeValuePair) {
			return false;
		}

		TypeValuePair typeValuePair = (TypeValuePair) other;
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
