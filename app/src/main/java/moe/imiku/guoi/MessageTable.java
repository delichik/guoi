package moe.imiku.guoi;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import moe.imiku.guoi.listener.GlobalValueChangedListener;

public class MessageTable {

    private static GlobalValueChangedListener listener = null;
    private static ArrayList<Message> message_list = new ArrayList<>();

    public static void sendMessage (Context context, String msg) {
        Message message = new Message();
        message.setMessage(msg);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        message.setTime(formatter.format(curDate));
        message_list.add(message);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        if (listener != null) listener.onChanged(message);
    }

    public static Message[] getAllMessages () {
        return message_list.toArray(new Message[]{});
    }

    public static class Message {
        private String message;
        private String time;

        public String getMessage() {
            return message;
        }

        void setMessage(String message) {
            this.message = message;
        }

        public String getTime() {
            return time;
        }

        void setTime(String time) {
            this.time = time;
        }
    }

    public static void setListener (GlobalValueChangedListener listener) {
        MessageTable.listener = listener;
    }

}
