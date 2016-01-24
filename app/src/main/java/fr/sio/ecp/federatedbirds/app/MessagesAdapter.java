package fr.sio.ecp.federatedbirds.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.model.Message;

import static android.support.v4.app.ActivityCompat.startActivity;


/**
 * Created by Michaël on 24/11/2015.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Message> mMessages;
    private static Message mMessage,mMessageclic=null;
    public void setMessages(List<Message> messages) {
        mMessages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        mMessage = message;
        Picasso.with(holder.mUserAvatarView.getContext())
                .load(message.user.avatar)
                .into(holder.mUserAvatarView);

        holder.mTextView.setText(message.text);

    }

    public Message returnMessage(){
        return mMessageclic;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mUserAvatarView;
        private TextView mTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mUserAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mTextView = (TextView) itemView.findViewById(R.id.text);
            mUserAvatarView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView){
                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                mMessageclic=mMessage;
                v.getContext().startActivity(intent);

            }

        }

    }
    

}