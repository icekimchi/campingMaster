package com.example.campingmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campingmaster.model.ChatMessage;

import com.example.campingmaster.R;
import com.example.campingmaster.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageTextView.setText(message.getMessage());
        holder.senderTextView.setText(message.getSender());

        // 메시지의 송신자에 따라 말풍선의 위치를 조정
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.chatBalloonLayout.getLayoutParams();
        if (message.getSender().equals("나")) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
        }
        holder.chatBalloonLayout.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout chatBalloonLayout;
        TextView messageTextView;
        TextView senderTextView;
        TextView timestampTextView;
            ViewHolder(View itemView) {
                super(itemView);
                chatBalloonLayout = itemView.findViewById(R.id.chat_balloon_layout);
                messageTextView = itemView.findViewById(R.id.messageTextView);
                senderTextView = itemView.findViewById(R.id.senderTextView);
                timestampTextView = itemView.findViewById(R.id.timestampTextView);
            }
        }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }
}


