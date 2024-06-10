package com.mibodega.mystore.shared.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.MessageResponse;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageResponse> messageList;

    public MessageAdapter(List<MessageResponse> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        MessageResponse message = messageList.get(position);
        holder.textViewMessage.setText(message.getContent());

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.textViewMessage.getLayoutParams();

        if (Objects.equals(message.getRole(), "user")) {
            layoutParams.gravity = Gravity.END;
            holder.textViewMessage.setBackgroundResource(R.drawable.bg_message_me);
        } else {
            layoutParams.gravity = Gravity.START;
            holder.textViewMessage.setBackgroundResource(R.drawable.bg_message);
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
