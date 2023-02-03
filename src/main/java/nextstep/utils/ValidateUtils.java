package nextstep.utils;

public final class ValidateUtils {

    private ValidateUtils() {
    }

    public static Class<?> getType() {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }
}
