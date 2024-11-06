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


    private static final String REGEX_EMPRESA_VALIDO = "^[a-zA-Z0-9\\s\\-_&*.,]*$";
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
                    String filteredInput = input.replaceAll("[^a-zA-Z0-9\\s\\-_&*.,]", "");
                    editText.setText(filteredInput);
                    editText.setSelection(filteredInput.length());// Mover el cursor al final
                } else {
                    inputLayout.setError(null);
                    // Limpiar el error si es válido
                }
            }
        });
    }

    private static final String REGEX_NOMBRE_PERSONA_VALIDO = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']*$";
    public static void addPersonaInputValidation(EditText editText, Context context, Runnable onValidInput) {
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
    public static void addPersonaInputValidationTextInput(TextInputEditText editText, TextInputLayout inputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();

                if (!input.matches(REGEX_NOMBRE_PERSONA_VALIDO)) {
                    inputLayout.setError("Caracter no permitido");
                    String filteredInput = input.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']", "");
                    editText.setText(filteredInput);
                    editText.setSelection(filteredInput.length());// Mover el cursor al final
                } else {
                    inputLayout.setError(null);
                    // Limpiar el error si es válido
                }
            }
        });
    }


    // Expresión regular para letras, números, y algunos símbolos comunes en nombres y códigos de productos
    private static final String REGEX_BUSQUEDA_VALIDO = "^[a-zA-Z0-9\\s\\-_&*.,]*$";

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
                    String filteredInput = input.replaceAll("[^a-zA-Z0-9\\s\\-_&*.,]", "");
                    editText.setText(filteredInput);
                    editText.setSelection(filteredInput.length());  // Mover el cursor al final
                } else {
                    inputLayout.setError(null);
                    onValidInput.run(); // Limpiar el error si es válido
                }
            }
        });
    }

    private static final String REGEX_EMAIL_VALIDO = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public boolean esEmailValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(REGEX_EMAIL_VALIDO);
    }



}
