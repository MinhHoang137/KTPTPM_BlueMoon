package view_controller;

public class DateConverter {
    public static java.util.Date convertToDate(java.time.LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return java.sql.Date.valueOf(localDate);
    }

    public static java.time.LocalDate convertToLocalDate(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}
