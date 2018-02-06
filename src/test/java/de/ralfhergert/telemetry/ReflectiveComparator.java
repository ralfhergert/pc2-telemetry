package de.ralfhergert.telemetry;

import org.junit.Assert;
import org.junit.internal.ExactComparisonCriteria;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This utility class compares to object using reflection.
 */
public class ReflectiveComparator {

	public static boolean equal(Object o1, Object o2) {
		try {
			for (Field field : o1.getClass().getFields()) {
				if (field.getType().isArray()) {
					new ExactComparisonCriteria().arrayEquals("field " + field.getName() + " should be equal", field.get(o1), field.get(o2));
				} else {
					Assert.assertEquals("field " + field.getName() + " should be equal", field.get(o1), field.get(o2));
				}
			}
			for (Method method : o1.getClass().getMethods()) {
				if (method.getName().startsWith("get")) {
					if (method.getReturnType().isArray()) {
						new ExactComparisonCriteria().arrayEquals("method " + method.getName() + " return value should be equal",
							method.invoke(o1), method.invoke(o2));
					} else {
						Assert.assertEquals("method " + method.getName() + " return value should be equal", method.invoke(o1), method.invoke(o2));
					}
				}
			}
			return true;
		} catch (ReflectiveOperationException e) {
			Assert.fail("could not access one of the given objects");
			return false;
		}
	}
}
