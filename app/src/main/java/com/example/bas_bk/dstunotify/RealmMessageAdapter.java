package com.example.bas_bk.dstunotify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BAS_BK on 31.08.2016.
 */
public class RealmMessageAdapter extends RecyclerView.Adapter<RealmMessageAdapter.ViewHolder> {
    //private static RealmResults<Message> mMessages;
    private static ArrayList<Message> mMessages;
    private static Context mContext;
    private Realm realm = Realm.getDefaultInstance();
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sender;
        public TextView theme;
        public TextView time;
        public TextView messagePart;
        public CheckBox cbSelect;

        public ViewHolder(final View itemView){
            super(itemView);
            sender = (TextView) itemView.findViewById(R.id.sender);
            theme = (TextView) itemView.findViewById(R.id.Theme);
            time = (TextView) itemView.findViewById(R.id.time);
            messagePart = (TextView) itemView.findViewById(R.id.messagePart);
            cbSelect = (CheckBox) itemView.findViewById(R.id.add2fav);

        }
    }
    //Отображаем сообщеньки прочитанными
    public static void DisplayAsWatched(View view){
        ((TextView) view.findViewById(R.id.sender)).setTypeface(null, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.Theme)).setTypeface(null, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.messagePart)).setTypeface(null, Typeface.NORMAL);
        ((TextView) view.findViewById(R.id.time)).setTypeface(null, Typeface.NORMAL);
    }
    //Обратный процесс ^
    public static void DisplayAsUnWatched(View view){
        ((TextView) view.findViewById(R.id.sender)).setTypeface(null, Typeface.BOLD);
        ((TextView) view.findViewById(R.id.Theme)).setTypeface(null, Typeface.BOLD);
        ((TextView) view.findViewById(R.id.messagePart)).setTypeface(null, Typeface.BOLD);
        ((TextView) view.findViewById(R.id.time)).setTypeface(null, Typeface.BOLD);
    }

    public RealmMessageAdapter(Context context, ArrayList<Message> messages){
        mMessages = messages;
        mContext = context;
    }

    @Override
    public RealmMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View messageView = inflater.inflate(R.layout.item_list, parent, false);

        return new ViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(final RealmMessageAdapter.ViewHolder viewHolder, int position){

        final Message message = mMessages.get(position);

        TextView sender = viewHolder.sender;
        sender.setText(message.getSender());
        TextView theme = viewHolder.theme;
        theme.setText(message.getTheme());
        TextView time = viewHolder.time;
        time.setText(message.getTime());
        TextView messagePart = viewHolder.messagePart;
        messagePart.setText(message.getText());

        viewHolder.cbSelect.setOnCheckedChangeListener(null);

        viewHolder.cbSelect.setChecked(message.isSelected());

        viewHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                realm.beginTransaction();
                message.setSelect(isChecked);
                realm.commitTransaction();
            }
        });

        if (message.isWatched()){
            DisplayAsWatched(viewHolder.itemView);
        }
        else {
            DisplayAsUnWatched(viewHolder.itemView);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(mContext,MessageActivity.class);
                intent.putExtra("Message", mMessages.get(position));
                mContext.startActivity(intent);
                notifyItemChanged(position);
                realm.beginTransaction();
                mMessages.get(position).watch();
                realm.commitTransaction();
            }
        });
    }

    @Override
    public int getItemCount(){
        return mMessages.size();
    }
}
