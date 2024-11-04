package com.mibodega.mystore.shared;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InputValidator {

    // Expresión regular para permitir letras, números y algunos signos básicos (sin emojis o caracteres especiales)
    private static final String REGEX_VALIDO = "^[a-zA-Z0-9\\s,.!?@#&()\\-+]*$";

    public static void addInputValidation(EditText editText, Runnable onValidInput) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No necesitamos implementar esto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No necesitamos implementar esto
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();

                // Si el input no es válido, eliminamos los caracteres no permitidos
                if (!input.matches(REGEX_VALIDO)) {
                    editText.setError("Caracter no permitido");
                    String filteredInput = input.replaceAll("[^a-zA-Z0-9\\s,.!?@#&()\\-+]", "");
                    editText.setText(filteredInput);
                    editText.setSelection(filteredInput.length());  // Mover el cursor al final
                }else {
                    onValidInput.run();
                }
            }
        });
    }

    // Expresión regular para permitir letras, números, espacios, y algunos signos básicos comunes en nombres comerciales
    private static final String REGEX_EMPRESA_VALIDO = "^[a-zA-Z0-9\\s&.,'-]*$";

    public static void addEmpresaInputValidation(EditText editText, Context context, Runnable onValidInput) {
        editText.addTextChangedListener(new TextWatcher() {
            private String currentText = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                currentText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No necesitamos implementar esto
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();

                // Si el input no es válido, eliminamos los caracteres no permitidos
                if (!input.matches(REGEX_EMPRESA_VALIDO)) {
                    // Mostrar un Toast informando del carácter no permitido
                    Toast.makeText(context, "Caracter no permitido", Toast.LENGTH_SHORT).show();

                    // Revertir al último texto válido sin cambiar el contenido
                    editText.setText(currentText);
                    editText.setSelection(currentText.length()); // Mover el cursor al final
                } else {
                    currentText = input;
                    onValidInput.run();// Actualizar el texto válido
                }
            }
        });
    }
    public static void addEmpresaInputValidationTextInput(TextInputEditText editText, TextInputLayout inputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();

                if (!input.matches(REGEX_EMPRESA_VALIDO)) {
                    inputLayout.setError("Caracter no permitido");
                    String filteredInput = input.replaceAll("[^a-zA-Z0-9\\s&.,'-]", "");
                    editText.setText(filteredInput);
                    editText.setSelection(filteredInput.length());  // Mover el cursor al final
                } else {
                    inputLayout.setError(null);
                    // Limpiar el error si es válido
                }
            }
        });
    }

    // Expresión regular para letras, números, y algunos símbolos comunes en nombres y códigos de productos
    private static final String REGEX_BUSQUEDA_VALIDO = "^[a-zA-Z0-9\\s\\-_#]*$";

    public static void addBusquedaInputValidation(TextInputEditText editText, TextInputLayout inputLayout, Runnable onValidInput) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();

                if (!input.matches(REGEX_BUSQUEDA_VALIDO)) {
                    inputLayout.setError("Caracter no permitido en la búsqueda");
                    String filteredInput = input.replaceAll("[^a-zA-Z0-9\\s\\-_#]", "");
                    editText.setText(filteredInput);
                    editText.setSelection(filteredInput.length());  // Mover el cursor al final
                } else {
                    inputLayout.setError(null);
                    onValidInput.run(); // Limpiar el error si es válido
                }
            }
        });
    }

    // Expresión regular para letras, números, espacios y algunos caracteres comunes en nombres o códigos de productos

    public static void addSearchValidation(SearchView searchView, Context context) {
        // Acceder al EditText interno de SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        if (searchEditText != null) {
            searchEditText.addTextChangedListener(new TextWatcher() {
                private String currentText = "";  // Guarda el texto válido actual

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    currentText = s.toString(); // Guardar el último texto válido antes de cambios
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // No necesitamos implementar esto
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String input = editable.toString();

                    // Verifica si el texto contiene caracteres no permitidos
                    if (!input.matches(REGEX_BUSQUEDA_VALIDO)) {
                        // Mostrar Toast informando el carácter no permitido
                        Toast.makeText(context, "Caracter no permitido en la búsqueda", Toast.LENGTH_SHORT).show();

                        // Revertir al último texto válido sin cambiar el contenido visible
                        searchEditText.setText(currentText);
                        searchEditText.setSelection(currentText.length()); // Mover el cursor al final
                    } else {
                        currentText = input; // Actualizar el último texto válido
                    }
                }
            });
        }
    }

}
