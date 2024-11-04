package com.mibodega.mystore.shared;

import android.content.Context;
        import android.content.SharedPreferences;

        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;
import java.util.TimeZone;

public class SharedPreferencesHelper {

    private static final String PREFS_NAME = "MyStoreAppPreferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Guardar una cadena
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    // Obtener una cadena
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Guardar un entero
    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    // Obtener un entero
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    // Guardar un booleano
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Obtener un booleano
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Guardar la fecha actual con una clave
    public void putCurrentDate(String key) {
        String currentDate = getCurrentDateInPeruTimeZone();
        editor.putString(key, currentDate);
        editor.apply();
    }

    // Verificar si ha pasado el intervalo especificado (en días)
    public boolean hasIntervalPassed(String key, int days) {
        String lastDate = getString(key, "");
        if (lastDate.isEmpty()) {
            return true; // Si no hay una fecha previa guardada, el intervalo ha pasado.
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Lima")); // Zona horaria de Perú
            Date lastSavedDate = dateFormat.parse(lastDate);
            Date currentDate = new Date();

            // Calcular la diferencia en días entre la fecha guardada y la fecha actual
            long diff = currentDate.getTime() - lastSavedDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            return diffDays >= days;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    // Limpiar un valor
    public void clearValue(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Limpiar todas las preferencias
    public void clearAll() {
        editor.clear();
        editor.apply();
    }

    // Guardar el timestamp actual en milisegundos
    public void putCurrentTimestamp(String key) {
        long currentTimestamp = System.currentTimeMillis();
        editor.putLong(key, currentTimestamp);
        editor.apply();
    }

    // Verificar si ha pasado el intervalo especificado (en minutos)
    public boolean hasIntervalPassedInMinutes(String key, int minutes) {
        long lastTimestamp = sharedPreferences.getLong(key, 0);

        if (lastTimestamp == 0) {
            return true; // Si no hay un timestamp guardado, el intervalo ha pasado.
        }

        long currentTimestamp = System.currentTimeMillis();
        long diff = currentTimestamp - lastTimestamp;

        // Convertir el intervalo de minutos a milisegundos
        long diffMinutes = diff / (60 * 1000);

        return diffMinutes >= minutes;
    }

    // Obtener la fecha actual en la zona horaria de Perú
    private String getCurrentDateInPeruTimeZone() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Lima")); // Zona horaria de Perú
        return dateFormat.format(new Date());
    }
    public boolean isNewDay(String key) {
        String lastSavedDate = getString(key, "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Lima")); // Zona horaria de Perú
        String currentDate = dateFormat.format(new Date());

        // Verificar si la fecha actual es diferente de la última fecha guardada
        return !currentDate.equals(lastSavedDate);
    }

}
