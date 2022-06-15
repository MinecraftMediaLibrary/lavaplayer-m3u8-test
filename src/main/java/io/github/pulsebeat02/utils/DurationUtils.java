package io.github.pulsebeat02.utils;

public final class DurationUtils {

  private DurationUtils() {}

  public static Long parseSecondDuration(final String value) {
    try {
      return (long) (Double.parseDouble(value) * 1000.0);
    } catch (final NumberFormatException ignored) {
      return null;
    }
  }
}
