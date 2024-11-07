package com.goryajnoff.messenger2;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {
    private String currentUserId;

    public ChatViewModelFactory(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
    }

    private String otherUserId;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(currentUserId,otherUserId);
    }
}
