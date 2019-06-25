package moe.imiku.guoi.Page;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import moe.imiku.guoi.MessageTable;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class Message extends PageLoader {
    private LinearLayout message_field;

    public Message(Context context) {
        super(context, R.layout.page_message);
    }

    @Override
    protected PageLoader subLoad() {
        message_field = findViewById(R.id.message_field);
        MessageTable.setListener((obj) ->
                message_field.addView(createView((MessageTable.Message)obj)));

        for (MessageTable.Message message : MessageTable.getAllMessages()) {
            message_field.addView(createView(message));
        }

        return this;
    }

    private View createView (MessageTable.Message message) {
        View view = View.inflate(context, R.layout.item_message, null);
        ((TextView) view.findViewById(R.id.message)).setText(message.getMessage());
        ((TextView) view.findViewById(R.id.time)).setText(message.getTime());
        return view;
    }

    @Override
    protected PageLoader subUnLoad() {
        return this;
    }
}
