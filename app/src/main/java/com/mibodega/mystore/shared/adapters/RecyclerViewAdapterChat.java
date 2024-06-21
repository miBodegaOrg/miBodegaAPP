package com.mibodega.mystore.shared.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ChatResponse;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.Utils;

import java.util.ArrayList;

public class RecyclerViewAdapterChat extends RecyclerView.Adapter<RecyclerViewAdapterChat.ViewHolder> implements View.OnClickListener {
    private Utils utils = new Utils();
    private Config config = new Config();
    private ArrayList<ChatResponse> chatList = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    final RecyclerViewAdapterChat.OnDetailItem onDetailItem;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnDetailItem {
        void onClick(ChatResponse chat);
    }
    public RecyclerViewAdapterChat(Context context, ArrayList<ChatResponse> chatList, RecyclerViewAdapterChat.OnDetailItem onDetailItem) {
        this.context = context;
        this.chatList = chatList;
        this.onDetailItem = onDetailItem;

    }

    public void setFilteredList(ArrayList<ChatResponse> filteredList) {
        this.chatList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewAdapterChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewAdapterChat.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterChat.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChatResponse chat = chatList.get(position);

        String formattedDate = utils.convertDateToClearFormat(chat.getCreatedAt().toString());
        holder.chatTitle.setText("Bodegas Consulta");
        holder.chatDate.setText(formattedDate);
        holder.lastMessage.setText(chat.getMessages().get(0).getText());

        holder.mv_item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailItem.onClick(chat);
            }
        });
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public static int convertDpToPixel(float dp, Context context) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mv_item_card;
        TextView chatTitle;
        TextView chatDate;
        TextView lastMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            mv_item_card = itemView.findViewById(R.id.Mcv_itemChat_chat);
            chatTitle = itemView.findViewById(R.id.Tv_chatTitle_chat);
            chatDate = itemView.findViewById(R.id.Tv_chatDateTime_chat);
            lastMessage = itemView.findViewById(R.id.Tv_lastMessage_chat);


        }
    }


}