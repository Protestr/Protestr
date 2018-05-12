package org.protestr.app;

import android.app.Application;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.one.EmojiOneProvider;

public class ProtestrApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new EmojiOneProvider());
    }
}
