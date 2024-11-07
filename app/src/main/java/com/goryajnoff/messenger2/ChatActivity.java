package com.goryajnoff.messenger2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";
    private View viewStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageView imageViewSend;
    private String currentUserId;
    private String otherUserId;
    private MessagesAdapter messagesAdapter;
    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);
        messagesAdapter = new MessagesAdapter(currentUserId);
        recyclerViewMessages.setAdapter(messagesAdapter);
        viewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);
        observeViewModel();
        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextMessage.getText().toString().trim();
                if (!text.isEmpty()) {
                    Message message = new Message(text, currentUserId, otherUserId);
                    viewModel.sendMessage(message);
                } else {
                    Toast.makeText(ChatActivity.this, R.string.fill_in_all_the_fields,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(false);
    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });
        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    String title = String.format("%s %s", user.getName(), user.getLastName());
                    textViewTitle.setText(title);
                    int drawId = R.drawable.circle_red;
                    if (user.isOnline()) {
                        drawId = R.drawable.circle_green;
                    }
                    viewStatus.setBackground(ContextCompat.getDrawable(ChatActivity.this, drawId));
                }

            }
        });
        viewModel.getMessageSend().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent) {
                    editTextMessage.setText("");
                }
            }
        });
    }


    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        viewStatus = findViewById(R.id.viewStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSend = findViewById(R.id.imageViewSend);

    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }
}