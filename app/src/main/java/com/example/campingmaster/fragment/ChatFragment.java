package com.example.campingmaster.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campingmaster.BuildConfig;
import com.example.campingmaster.R;
import com.example.campingmaster.adapter.MessageAdapter;
import com.example.campingmaster.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvWelcome;
    private EditText etMsg;
    private ImageButton btnSend;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private static final String API_KEY = BuildConfig.GPT_API;
    private static final String API_ENDPOINT = BuildConfig.GPT_ENDPOINT;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_main, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        tvWelcome = view.findViewById(R.id.tv_welcome);
        etMsg = view.findViewById(R.id.et_msg);
        btnSend = view.findViewById(R.id.btn_send);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = etMsg.getText().toString().trim();
                if (question.isEmpty()) return;
                addToChat(question, Message.SENT_BY_ME);
                etMsg.setText("");
                callAPI(question);
                tvWelcome.setVisibility(View.GONE);
            }
        });

        return view;
    }

    void addToChat(String message, String sentBy) {
        getActivity().runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    void callAPI(String question) {
        messageList.add(new Message("...", Message.SENT_BY_BOT));

        JSONObject json = new JSONObject();
        try {
            json.put("model", "gpt-3.5-turbo");
            JSONArray messagesArray = new JSONArray();

            // 페르소나 정의 추가
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "당신은 '캠핑의 고수'라는 이름의 친절하고 유머러스한 캠ㅗㅑ핑 전문가입니다. 캠핑장 추천, 캠핑 장비, 캠핑 요리, 안전 수칙 등에 대한 풍부한 지식을 가지고 있으며, 비공식적이고 친근한 어조로 사용자와 대화합니다. 사용자에게 질문을 던지며 대화를 유도하세요.");
            messagesArray.put(systemMessage);

            JSONObject messageObject = new JSONObject();
            messageObject.put("role", "user");
            messageObject.put("content", question);
            messagesArray.put(messageObject);

            json.put("messages", messagesArray);
            json.put("temperature", 0.8);
            json.put("max_tokens", 1024);
            json.put("top_p", 1);
            json.put("frequency_penalty", 0.5);
            json.put("presence_penalty", 0.5);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray choices = jsonResponse.getJSONArray("choices");
                        String result = choices.getJSONObject(0).getJSONObject("message").getString("content");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        addResponse("Failed to parse response");
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 필요한 경우 자원 해제
    }
}
