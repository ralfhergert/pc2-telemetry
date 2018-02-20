package de.ralfhergert.telemetry.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will generate a list of {@link PropertyInfo} for the given {@link Class}.
 */
public class PropertyInspector {

	/**
	 * This method creates a set of propertyInfo for the given {@param clazz}.
	 */
	public static <Type> List<PropertyInfo<Type,?>> inspect(Class<Type> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("class can not be null");
		}
		final List<PropertyInfo<Type,?>> propertyInfoList = new ArrayList<>();
		// use reflection to iterate over all fields and compare them.
		for (Field field : clazz.getFields()) {
			propertyInfoList.add(new PropertyInfo<>(field.getName(), field.getType(), new FieldAccessor<>(field)));
		}
		for (Method method : clazz.getMethods()) {
			if (method.getName().startsWith("get")) {
				final String propertyName = convertFirstCharacterToLowerCase(method.getName().substring(3));
				propertyInfoList.add(new PropertyInfo<>(propertyName, method.getReturnType(), new MethodAccessor<>(method)));
			}
		}
		return propertyInfoList;
	}

	public static String convertFirstCharacterToLowerCase(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}
}
