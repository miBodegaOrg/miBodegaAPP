package com.mibodega.mystore.views.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestMessage;
import com.mibodega.mystore.models.Responses.ChatResponse;
import com.mibodega.mystore.models.Responses.MessageResponse;
import com.mibodega.mystore.models.Responses.MessageResponseGpt;
import com.mibodega.mystore.services.IChatServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.MessageAdapter;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterChat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatbotActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private List<MessageResponse>  messageList = new ArrayList<>();
    private ChatResponse chatData;
    private MessageAdapter messageAdapter;
    private String chatID;
    private Config config = new Config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        Bundle recibeDatos = getIntent().getExtras();
        chatID = recibeDatos.getString("ChatID");


        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        loadMessages(chatID);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    MessageResponse message = new MessageResponse("user",messageText);
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                    editTextMessage.setText("");
                    askChatGPT(chatID,messageText);
                }
            }
        });

    }

    public void loadMessages(String id){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IChatServices service = retrofit.create(IChatServices.class);
        Call<ChatResponse> call = service.getChatsById(id,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatResponse> call, @NonNull Response<ChatResponse> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    chatData = response.body();
                    if(chatData!=null){
                        recyclerView.removeAllViews();
                        messageList = chatData.getMessages();
                        messageAdapter = new MessageAdapter(messageList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setAdapter(messageAdapter);
                    }
                    System.out.println("successfull request");
                }

            }

            @Override
            public void onFailure(@NonNull Call<ChatResponse> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public void askChatGPT(String id, String msg){
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(config.getURL_API()).addConverterFactory(GsonConverterFactory.create()).
                build();

        IChatServices service = retrofit.create(IChatServices.class);
        RequestMessage message = new RequestMessage(msg);
        Call<MessageResponseGpt> call = service.requestQuestionGPT(id,message,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<MessageResponseGpt>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponseGpt> call, @NonNull Response<MessageResponseGpt> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    MessageResponseGpt responseGpt = response.body();
                    if(responseGpt!=null){
                        MessageResponse message = new MessageResponse("assistant",responseGpt.getResponse());
                        messageList.add(message);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                    System.out.println("successfull request");
                }

            }

            @Override
            public void onFailure(@NonNull Call<MessageResponseGpt> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }




}