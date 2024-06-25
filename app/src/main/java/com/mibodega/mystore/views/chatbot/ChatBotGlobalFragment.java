package com.mibodega.mystore.views.chatbot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChatBotGlobalFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private List<MessageResponse> messageList = new ArrayList<>();
    private ChatResponse chatData;
    private MessageAdapter messageAdapter;
    private String chatID="";
    private Config config = new Config();
    private ArrayList<ChatResponse> chatList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_chat_bot_global, container, false);

        loadChats();
        recyclerView = root.findViewById(R.id.recyclerView);
        editTextMessage = root.findViewById(R.id.editTextMessage);
        buttonSend = root.findViewById(R.id.buttonSend);

        messageAdapter = new MessageAdapter(getContext(),messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    if(!Objects.equals(chatID, "")){
                        MessageResponse message = new MessageResponse("user",messageText);
                        messageList.add(message);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                        editTextMessage.setText("");
                        askChatGPT(chatID,messageText);
                    }

                }
            }
        });
        return root;

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
                        messageAdapter = new MessageAdapter(getContext(),messageList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(messageAdapter);
                        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
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

    public void loadChats(){
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
                        chatID = chatList.get(chatList.size()-1).get_id();
                        loadMessages(chatID);
                    }
                    System.out.println("successfull request");

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<ChatResponse>> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
            }
        });
    }


}