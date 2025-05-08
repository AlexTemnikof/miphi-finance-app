package ru.rfma.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Util {

    /**
     * Получение списка различий в полях
     *
     * @param origin исходный объект
     * @param object новый объект
     * @return Список наименований полей, в которых различные значения
     */
    public static List<String> getFieldDifferences(final Object origin, final Object object) {
        List<String> differences = new ArrayList<>();

        if (origin == null || object == null) {
            throw new IllegalArgumentException("Both objects must be non-null");
        }

        Class<?> clazz = origin.getClass();

        if (!clazz.equals(object.getClass())) {
            throw new IllegalArgumentException("Both objects must be of the same class");
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);

            try {
                Object originValue = field.get(origin);
                Object objectValue = field.get(object);

                if (originValue == null && objectValue != null) {
                    differences.add(field.getName());
                } else if (originValue != null && !originValue.equals(objectValue)) {
                    differences.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(isAccessible);
            }
        }

        return differences;
    }

    public static void uncheck(final ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }

    public static <T> T uncheck(final ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }
    }

    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}
