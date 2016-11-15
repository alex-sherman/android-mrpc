package net.vector57.mrpc;

import android.os.AsyncTask;

public abstract class TransportThread extends Thread {
    protected MRPC mrpc;
    AsyncTask<String, Boolean, Void> sendTask() {
        return new AsyncTask<String, Boolean, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                //TODO: Resend while past repliers have not responded
                for(int i = 0; i < 3; i++)
                    send(params[0]);
                return null;
            }
        };
    }
    public TransportThread(MRPC mrpc) {
        this.mrpc = mrpc;
        setDaemon(true);
    }

    @Override
    public void run() {
        while(true) {
            String recvd = poll();
            Message message = Message.FromJson(recvd);
            if(message != null) {
                onRecv(message);
            }
        }
    }
    protected abstract String poll();
    protected abstract Boolean send(String message);
    protected void onRecv(Message message) { mrpc.onReceive(message); }
    public void sendAsync(Message message) {
        sendTask().execute(message.toJSON());
    }
}