package de.ralfhergert.telemetry.gui;

import de.ralfhergert.telemetry.graph.LineGraph;
import de.ralfhergert.telemetry.graph.NegativeOffsetAccessor;
import de.ralfhergert.telemetry.pc2.datagram.Vector;
import de.ralfhergert.telemetry.reflection.*;
import de.ralfhergert.telemetry.pc2.datagram.v2.CarPhysicsPacket;
import de.ralfhergert.telemetry.repository.IndexedRepository;
import de.ralfhergert.telemetry.repository.ItemRepository;
import de.ralfhergert.telemetry.repository.Repository;

import java.awt.Color;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class will generate a {@link Repository} of {@link LineGraph}.
 */
public class GraphRepositoryFactory {

	private static final ResourceBundle graphColors = ResourceBundle.getBundle("graphColors");

	/**
	 * This method create a set of LineGraphs for the given {@param carPhysicsRepository}.
	 * @param carPhysicRepository the repository to which all the LineGraph will be connected.
	 * @param keyAccessor determines which value is key for all generated LineGraphs.
	 * @param sampleItem mainly is used to detect the length of array values.
	 */
	public Repository<LineGraph> createLineGraphs(IndexedRepository<CarPhysicsPacket> carPhysicRepository, NegativeOffsetAccessor<CarPhysicsPacket, ? extends Number> keyAccessor, CarPhysicsPacket sampleItem) {
		final Repository<LineGraph> graphRepository = new ItemRepository<>();

		final List<PropertyInfo<CarPhysicsPacket,?>> propertyInfoList = new ArrayList<>();
		// use reflection to iterate over all fields and compare them.
		for (Field field : CarPhysicsPacket.class.getFields()) {
			propertyInfoList.add(new PropertyInfo<>(field.getName(), field.getType(), new FieldAccessor<>(field)));
		}
		for (Method method : CarPhysicsPacket.class.getMethods()) {
			if (method.getName().startsWith("get")) {
				final String propertyName = convertFirstCharacterToLowerCase(method.getName().substring(3));
				propertyInfoList.add(new PropertyInfo<>(propertyName, method.getReturnType(), new MethodAccessor<>(method)));
			}
		}
		for (PropertyInfo<CarPhysicsPacket,?> propertyInfo : propertyInfoList) {
			if (propertyInfo.getPropertyType().isArray()) {
				if (!isNumber(propertyInfo.getPropertyType().getComponentType())) {
					continue; // skip this field if its not a number.
				}
				// try to use the sampleItem to detect the array length.
				final int length = (sampleItem == null) ? 1 : detectArraySize(sampleItem, propertyInfo.getPropertyAccessor());

				for (int i = 0; i < length; i++) {
					final String graphName = "carPhysicsPacket.property." + propertyInfo.getPropertyName() + "[" + i + "]";
					graphRepository.addItem(
						new LineGraph<>(carPhysicRepository, keyAccessor, getArrayAccessor(propertyInfo.getPropertyType(), propertyInfo.getPropertyAccessor(), i))
							.setProperty("name", graphName)
							.setProperty("color", graphColors.containsKey(graphName) ? decode(graphColors.getString(graphName)) : Color.LIGHT_GRAY)
					);
				}
			} else if (isNumber(propertyInfo.getPropertyType())) {
				final String graphName = "carPhysicsPacket.property." + propertyInfo.getPropertyName();
				graphRepository.addItem(
					new LineGraph<>(carPhysicRepository, keyAccessor, (Accessor<CarPhysicsPacket, Number>)propertyInfo.getPropertyAccessor())
						.setProperty("name", graphName)
						.setProperty("color", graphColors.containsKey(graphName) ? decode(graphColors.getString(graphName)) : Color.LIGHT_GRAY)
				);
			} else if (Vector.class.isAssignableFrom(propertyInfo.getPropertyType())) {
				final String graphName = "carPhysicsPacket.property." + propertyInfo.getPropertyName() + ".length";
				graphRepository.addItem(
					new LineGraph<>(carPhysicRepository, keyAccessor, (Accessor<CarPhysicsPacket, Number>) carPhysicsPacket -> {
						Vector v = (Vector)propertyInfo.getPropertyAccessor().getValue(carPhysicsPacket);
						return (v != null) ? v.length() : 0;
					})
						.setProperty("name", graphName)
						.setProperty("color", graphColors.containsKey(graphName) ? decode(graphColors.getString(graphName)) : Color.LIGHT_GRAY)
				);
			}
		}
		return graphRepository;
	}

	private <Item> Accessor<Item, ? extends Number> getArrayAccessor(Class type, Accessor<Item, ?> accessor, int index) {
		if (!type.isArray()) {
			throw new IllegalArgumentException("given type is not an array: " + type);
		}
		if (byte[].class.equals(type)) {
			return new ByteArrayAccessor<>(index, (Accessor<Item, byte[]>)accessor);
		} else if (short[].class.equals(type)) {
			return new ShortArrayAccessor<>(index, (Accessor<Item, short[]>)accessor);
		} else if (float[].class.equals(type)) {
			return new FloatArrayAccessor<>(index, (Accessor<Item, float[]>)accessor);
		} else {
			throw new IllegalArgumentException("no array accessor available for " + type);
		}
	}

	public static String convertFirstCharacterToLowerCase(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}

	public static boolean isNumber(Class clazz) {
		return Number.class.isAssignableFrom(clazz)
			|| short.class.equals(clazz)
			|| int.class.equals(clazz)
			|| long.class.equals(clazz)
			|| float.class.equals(clazz)
			|| double.class.equals(clazz);
	}

	public int detectArraySize(CarPhysicsPacket sample, Accessor<CarPhysicsPacket, ?> accessor) {
		Object object = accessor.getValue(sample);
		if (object != null && object.getClass().isArray()) {
			return Array.getLength(object);
		}
		return 0;
	}

	/**
	 * This method will decode a string like "#ff0000ff" into a RGBA-Color.
	 */
	public static Color decode(String value) throws NumberFormatException {
		final long i = Long.decode(value);
		return new Color((int)((i >> 24) & 0xFF), (int)((i >> 16) & 0xFF), (int)((i >> 8) & 0xFF), (int)(i & 0xFF));
	}
}
