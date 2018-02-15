package de.ralfhergert.telemetry.reflection;

/**
 * This is a helper class to group a property's:
 * <ul>
 *   <li>name</li>
 *   <li>type</li>
 *   <li>accessor and</li>
 * </ul>
 */
public class PropertyInfo<Item, Type> {

	private final String propertyName;
	private final Class<Type> propertyType;
	private final Accessor<Item, Type> propertyAccessor;

	public PropertyInfo(String propertyName, Class<Type> propertyType, Accessor<Item, Type> propertyAccessor) {
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

	public Accessor<Item, Type> getPropertyAccessor() {
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
