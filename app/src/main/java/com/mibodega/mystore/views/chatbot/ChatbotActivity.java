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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestMessage;
import com.mibodega.mystore.models.Responses.ChatResponse;
import com.mibodega.mystore.models.Responses.MessageResponse;
import com.mibodega.mystore.models.Responses.MessageResponseGpt;
import com.mibodega.mystore.services.IChatServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.adapters.MessageAdapter;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterChat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
    private ProgressBar pgr_loadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        Bundle recibeDatos = getIntent().getExtras();
        chatID = recibeDatos.getString("ChatID");


        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        pgr_loadMessage = findViewById(R.id.Pbar_loadBotMessage_chatbot);

        messageAdapter = new MessageAdapter(this,messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        if(!Objects.equals(chatID, "")){
            loadMessages(chatID);
        }else{
            loadCreateChat();
        }

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
        pgr_loadMessage.setIndeterminate(true);
        pgr_loadMessage.setVisibility(View.VISIBLE);
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
                        messageAdapter = new MessageAdapter(getBaseContext(),messageList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setAdapter(messageAdapter);

                        pgr_loadMessage.setVisibility(View.GONE);
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
    public void loadCreateChat(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de conexión
                        .readTimeout(30, TimeUnit.SECONDS) // Tiempo de lectura
                        .build())
                .build();

        IChatServices service = retrofit.create(IChatServices.class);
        RequestMessage message = new RequestMessage("soy un bodeguero, quiero hacer consultas sobre gestion y deseo que  respondas de forma concreta");
        Call<MessageResponseGpt> call = service.createContextCreateChat(message,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<MessageResponseGpt>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponseGpt> call, @NonNull Response<MessageResponseGpt> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    MessageResponseGpt messageRptContext =response.body();
                    if(messageRptContext!=null){
                        recyclerView.removeAllViews();
                        chatID = messageRptContext.get_id();
                        loadMessages(messageRptContext.get_id());
                    }
                    System.out.println("successfull request");
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<MessageResponseGpt> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }
    public void askChatGPT(String id, String msg){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getURL_API())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de conexión
                        .readTimeout(30, TimeUnit.SECONDS) // Tiempo de lectura
                        .build())
                .build();
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


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}