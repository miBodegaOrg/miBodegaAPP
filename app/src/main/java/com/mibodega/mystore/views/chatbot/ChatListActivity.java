package com.mibodega.mystore.views.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Responses.ChatResponse;
import com.mibodega.mystore.models.Responses.PagesProductResponse;
import com.mibodega.mystore.models.Responses.ProductResponse;
import com.mibodega.mystore.services.IChatServices;
import com.mibodega.mystore.services.IProductServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterChat;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterProduct;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatListActivity extends AppCompatActivity {

    private TextInputEditText txt_search;
    private FloatingActionButton btn_datePicker;
    private FloatingActionButton btn_newChat;
    private RecyclerView rv_chatsList;
    private Config config = new Config();
    private ArrayList<ChatResponse> chatList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        txt_search = findViewById(R.id.Edt_searchChat_chat);
        btn_datePicker = findViewById(R.id.FBtn_datePicker_chat);
        btn_newChat = findViewById(R.id.FBtn__newChat_chat);
        rv_chatsList = findViewById(R.id.Rv_chatList_chat);

        btn_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle enviarDatos = new Bundle();
                enviarDatos.putString("ChatID","");
                Intent move = new Intent(getApplicationContext(), ChatbotActivity.class);
                move.putExtras(enviarDatos);
                startActivity(move);
            }
        });

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

       // loadChats();

    }

    public void loadChats(){/*
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IChatServices service = retrofit.create(IChatServices.class);
        Call<List<ChatResponse>> call = service.getChats("Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChatResponse>> call, @NonNull Response<List<ChatResponse>> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    chatList = (ArrayList<ChatResponse>) response.body();
                    if(chatList!=null){
                        rv_chatsList.removeAllViews();
                        RecyclerViewAdapterChat listAdapter = new RecyclerViewAdapterChat(getBaseContext(), chatList, new RecyclerViewAdapterChat.OnDetailItem() {
                            @Override
                            public void onClick(ChatResponse chat) {
                                Bundle enviarDatos = new Bundle();
                                enviarDatos.putString("ChatID",chat.get_id());
                                Intent move = new Intent(getApplicationContext(), ChatbotActivity.class);
                                move.putExtras(enviarDatos);
                                startActivity(move);
                            }
                        });

                        rv_chatsList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        rv_chatsList.setAdapter(listAdapter);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<ChatResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChats();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}