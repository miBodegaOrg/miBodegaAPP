package com.mibodega.mystore.views.chatbot;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mibodega.mystore.R;
import com.mibodega.mystore.models.Requests.RequestMessage;
import com.mibodega.mystore.models.Responses.ChatResponse;
import com.mibodega.mystore.models.Responses.MessageResponse;
import com.mibodega.mystore.models.Responses.MessageResponseGpt;
import com.mibodega.mystore.models.common.ChatMessage;
import com.mibodega.mystore.services.IChatServices;
import com.mibodega.mystore.shared.Config;
import com.mibodega.mystore.shared.DBConfig;
import com.mibodega.mystore.shared.DBfunctionsTableData;
import com.mibodega.mystore.shared.Utils;
import com.mibodega.mystore.shared.adapters.MessageAdapter;
import com.mibodega.mystore.shared.adapters.RecyclerViewAdapterChat;
import com.mibodega.mystore.views.products.ProductEditActivity;

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


public class ChatBotGlobalFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private ProgressBar pgr_loadMessage;
    private Button buttonSend;
    private List<ChatMessage> messageList = new ArrayList<>();
    private ChatResponse chatData;
    private MessageAdapter messageAdapter;
    private String chatID="";
    private Config config = new Config();
    private ArrayList<ChatResponse> chatList = new ArrayList<>();
    private DBfunctionsTableData dbfunctions = new DBfunctionsTableData();
    private Utils utils = new Utils();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_chat_bot_global, container, false);
        pgr_loadMessage = root.findViewById(R.id.pbar_loadBotMessage_chatbot_global);

        recyclerView = root.findViewById(R.id.recyclerView);
        editTextMessage = root.findViewById(R.id.editTextMessage);
        buttonSend = root.findViewById(R.id.buttonSend);
        loadMessages();
        messageAdapter = new MessageAdapter(getContext(),messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter);
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                        ChatMessage message = new ChatMessage(1,messageText,"user",utils.getDateTimeDDMMYYYYHHMMSS());
                        messageList.add(message);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                        editTextMessage.setText("");
                        dbfunctions.insert_message_sqlite(getContext(),message);
                        askChatGPT(messageText);
                }
            }
        });
        return root;

    }

    public void loadMessages(){
       messageList = (ArrayList<ChatMessage>) dbfunctions.get_messages_sqlite(getContext());
        if(messageList.size()>0){
                recyclerView.removeAllViews();
                messageAdapter = new MessageAdapter(getContext(),messageList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(messageAdapter);
                recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                pgr_loadMessage.setVisibility(View.GONE);

        }else{
            pgr_loadMessage.setVisibility(View.GONE);
        }


    }
    public void askChatGPT(String msg){
        pgr_loadMessage.setIndeterminate(true);
        pgr_loadMessage.setVisibility(View.VISIBLE);
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
        Call<MessageResponseGpt> call = service.createContextCreateChat(message,"Bearer "+config.getJwt());
        System.out.println(config.getJwt());
        call.enqueue(new Callback<MessageResponseGpt>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponseGpt> call, @NonNull Response<MessageResponseGpt> response) {
                System.out.println(response.toString());
                if(response.isSuccessful()){
                    MessageResponseGpt responseGpt = response.body();
                    if(responseGpt!=null){
                        pgr_loadMessage.setVisibility(View.GONE);
                        ChatMessage message = new ChatMessage(1,responseGpt.getResponse(),"assistant", utils.getDateTimeDDMMYYYYHHMMSS());
                        messageList.add(message);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                        dbfunctions.insert_message_sqlite(getContext(),message);

                    }
                    System.out.println("successfull request");
                }else{
                    Utils utils = new Utils();
                    if(!utils.isConnectedToInternet(getContext())){
                        Dialog dialog = utils.getAlertCustom(getContext(),"danger","Error","No hay conexión a internet. Por favor, conéctese a una red",false);
                        dialog.show();
                    }

                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error response body: " + errorBody);
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.getString("message");
                        //Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        System.out.println(errorMessage);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                pgr_loadMessage.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponseGpt> call, @NonNull Throwable t) {
                System.out.println("errror "+t.getMessage());
                if(!utils.isConnectedToInternet(getContext())){
                    Utils utils = new Utils();
                    Dialog dialog = utils.getAlertCustom(getContext(),"danger","Error","No hay conexión a internet. Por favor, conéctese a una red",false);
                    dialog.show();
                }
                pgr_loadMessage.setVisibility(View.GONE);
            }
        });
    }

}