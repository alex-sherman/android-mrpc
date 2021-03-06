package net.vector57.mrpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.MalformedJsonException;

/**
 * Created by Vector on 11/12/2016.
 */

public abstract class Message {
    public int id;
    public String src;
    public String dst;

    public static class Request extends Message {
        public JsonElement value;
        public Request(int id, String src, String dst, Object value) {
            super(id, src, dst);
            this.value = MRPC.gson().toJsonTree(value);
        }
    }
    public static class Response extends Message {
        public Response(int id, String src, String dst) {
            super(id, src, dst);
        }
        public JsonElement result;
        public JsonElement error;
    }

    public Message(int id, String src, String dst) {
        this.id = id;
        this.src = src;
        this.dst = dst;
    }

    public static Message FromJson(String string) {
        try {
            return MRPC.gson().fromJson(string, Message.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toJSON() {
        return MRPC.gson().toJson(this);
    }

    public Boolean isValid() {
        return id != 0 && src != null && dst != null;
    }
}
