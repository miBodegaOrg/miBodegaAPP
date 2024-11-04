package com.mibodega.mystore.shared.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.MessageResponse;
import com.mibodega.mystore.models.common.ChatMessage;
import com.mibodega.mystore.shared.TextFormaterMarkdown;

import java.util.List;
import java.util.Objects;

import io.noties.markwon.Markwon;
import io.noties.markwon.SpanFactory;
import io.noties.markwon.core.MarkwonTheme;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<ChatMessage> messageList;
    private Context context;
    private TextFormaterMarkdown textFormaterMarkdown = new TextFormaterMarkdown();

    public MessageAdapter(Context context,List<ChatMessage> messageList) {
        this.messageList = messageList;
        this.context=context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        holder.textViewMessage.setText(textFormaterMarkdown.formatText(context,message.getMessage()));

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.textViewMessage.getLayoutParams();

        if (Objects.equals(message.getOwner(), "user")) {
            layoutParams.gravity = Gravity.END;
            holder.textViewMessage.setBackgroundResource(R.drawable.bg_message_me);
            holder.textViewMessage.setTextColor(Color.WHITE);

        } else {
            layoutParams.gravity = Gravity.START;
            holder.textViewMessage.setBackgroundResource(R.drawable.bg_message);
            holder.textViewMessage.setTextColor(Color.BLACK);
        }
        holder.textViewMessage.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.Tv_chat_Message);
        }
    }
}
