package cs340.client.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import cs340.client.R;
import cs340.client.presenter.GamePresenter;
import cs340.client.presenter.IGamePresenter;
import model.EventMessage;

public class HistoryView extends AbstractGameView implements IHistoryView {

    private IGamePresenter presenter;
    private RecyclerView historyRecycler;
    private RecyclerView chatRecycler;
    private EditText chatText;
    private Button sendButton;
    int gameHistorySize = 0;
    int chatHistorySize = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_chat_view);

        chatText = (EditText) findViewById(R.id.chat_editText);

        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                displayMessage(chatText.getText().toString());
                presenter.sendChat(chatText.getText().toString());
                chatText.setText("");
            }
        });

        historyRecycler = (RecyclerView) findViewById(R.id.game_history_recycler);
        historyRecycler.setLayoutManager(new LinearLayoutManager(this));

        chatRecycler = (RecyclerView) findViewById(R.id.chat_recycler);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));

//        updateChat(Arrays.asList("cool", "whip", "is", "cool"));
//        updateGameHistory(Arrays.asList("cool", "is", "whip", "cool"));

        presenter = GamePresenter.getInstance();
        presenter.attach(this);
    }

//    @Override
    public void updateGameHistory(List<EventMessage> gameHistory) {
        if(gameHistorySize != gameHistory.size()){
            Collections.reverse(gameHistory);
            Adapter adapter = new Adapter(gameHistory);
            historyRecycler.setAdapter(adapter);
            gameHistorySize = gameHistory.size();
        }
    }

//    @Override
    public void updateChat(List<EventMessage> chatHistory) {
        if(chatHistorySize != chatHistory.size()){
            Collections.reverse(chatHistory);
            Adapter adapter = new Adapter(chatHistory);
            chatRecycler.setAdapter(adapter);
            chatHistorySize = chatHistory.size();
        }
    }

    @Override
    public void update() {
    }

    private class EventHolder extends RecyclerView.ViewHolder {
        private TextView data;

        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.history_chat_entry, parent, false));

            data = (TextView) itemView.findViewById(R.id.event_data);
        }

        public void bind(EventMessage data) {

            this.data.setText(data.toString());
        }
    }

    private class Adapter extends RecyclerView.Adapter<EventHolder> {

        private List<EventMessage> eventList;

        public Adapter(List<EventMessage> events) {
            this.eventList = events;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(HistoryView.this);

            return new EventHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EventHolder eventHolder, int i) {
            eventHolder.bind(eventList.get(i));
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }
    }
}
