package de.ralfhergert.telemetry.reflection;

/**
 * This is a helper class to group a property's:
 * <ul>
 *   <li>name</li>
 *   <li>type</li>
 *   <li>accessor and</li>
 * </ul>
 */
public class PropertyInfo<Item> {

	private final String propertyName;
	private final Class propertyType;
	private final Accessor<Item, Number> propertyAccessor;

	public PropertyInfo(String propertyName, Class propertyType, Accessor<Item, Number> propertyAccessor) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.propertyAccessor = propertyAccessor;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class getPropertyType() {
		return propertyType;
	}

	public Accessor<Item, Number> getPropertyAccessor() {
		return propertyAccessor;
	}

	@Override
	public String toString() {
		return "PropertyInfo{" +
			"propertyName='" + propertyName + '\'' +
			", propertyType=" + propertyType +
			", propertyAccessor=" + propertyAccessor +
			'}';
	}
}
