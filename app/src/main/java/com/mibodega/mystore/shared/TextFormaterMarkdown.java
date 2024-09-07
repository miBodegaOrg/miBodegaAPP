package com.mibodega.mystore.shared;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.TextView;

import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonSpansFactory;
import io.noties.markwon.MarkwonVisitor;
import io.noties.markwon.SpanFactory;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tables.TablePlugin;

public class TextFormaterMarkdown {
    public Spannable formatText(Context context, String text) {
        // Crear una instancia de Markwon
        Markwon markwon = Markwon.builder(context)
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TablePlugin.create(context))
                .build();

        // Convertir el texto Markdown a un Spannable
        Spannable spannableText = (Spannable) markwon.toMarkdown(text);
        return spannableText;
    }
}

