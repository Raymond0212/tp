package seedu.schedar.model.task;

import static java.util.Objects.requireNonNull;
import static seedu.schedar.commons.util.AppUtil.checkArgument;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Task's date in the task manager.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class TaskDate implements Comparable<TaskDate> {

    public static final String MESSAGE_CONSTRAINTS =
            "Date should be a valid date in the format YYYY-MM-DD";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public final LocalDate date;

    /**
     * Constructs a {@code TaskDate}.
     *
     * @param dateString A valid date.
     */
    public TaskDate(String dateString) {
        requireNonNull(dateString);
        checkArgument(isValidDate(dateString), MESSAGE_CONSTRAINTS);
        this.date = LocalDate.parse(dateString);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        try {
            LocalDate.parse(test, FORMATTER);
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return date.format(FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof TaskDate
                && date.equals(((TaskDate) other).date));
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public int compareTo(TaskDate o) {
        return this.date.compareTo(o.date);
    }
}
