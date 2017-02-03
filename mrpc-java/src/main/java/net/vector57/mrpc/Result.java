package net.vector57.mrpc;

import com.google.gson.JsonElement;

import java.util.Set;

/**
 * Created by Vector on 11/12/2016.
 */

public class Result {
    public static final long TIMEOUT = 1000;
    public static final long RESEND_DELAY = 200;
    public static abstract class Callback {
        public void onResult(Message.Response response) {
            if(response.error == null)
                this.onSuccess(response.result);
            else
                this.onFailure(response.error);
        };
        public void onSuccess(JsonElement value) {};
        public void onFailure(JsonElement value) {};
    }

    public Message.Response response;
    public Message.Request request;
    private long creationTime;
    private long lastSent;
    public Callback callback;
    private Set<String> requiredResponses;

    public boolean isCompleted() { return System.currentTimeMillis() - creationTime > TIMEOUT; }
    public boolean needsResend() {
        return !requiredResponses.isEmpty() && System.currentTimeMillis() - lastSent > RESEND_DELAY;
    }
    public void markSent() { lastSent = System.currentTimeMillis(); }

    public Result(Set<String> requiredResponses, Message.Request request, Callback callback) {
        this.requiredResponses = requiredResponses;
        creationTime = lastSent = System.currentTimeMillis();
        this.request = request;
        this.callback = callback;
    }
    public void resolve(final Message.Response message) {
        requiredResponses.remove(message.src);
        this.response = message;
        if(this.callback != null)
            callback.onResult(message);
    }
}