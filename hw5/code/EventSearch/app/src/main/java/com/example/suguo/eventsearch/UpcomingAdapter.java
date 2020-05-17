package com.example.suguo.eventsearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingHolder> {

    private ArrayList<UpcomingEvent> upcomingEventArrayList;
    private ListItemClickListener clickListener;

    public UpcomingAdapter(ArrayList<UpcomingEvent> upcomingEvents, ListItemClickListener listener) {
        this.upcomingEventArrayList = upcomingEvents;
        this.clickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickItemIndex);
    }

    @NonNull
    @Override
    public UpcomingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_list_item, parent, false);
        UpcomingHolder upcomingHolder = new UpcomingHolder(view);
        return upcomingHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingHolder holder, int position) {
        holder.displayNameTextView.setText(upcomingEventArrayList.get(position).getDisplayName());
        holder.artistTextView.setText(upcomingEventArrayList.get(position).getArtist());
        holder.dateTimeTextView.setText(upcomingEventArrayList.get(position).getDateTime());
        holder.typeTextView.setText(upcomingEventArrayList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return upcomingEventArrayList.size();
    }


    public class UpcomingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView displayNameTextView;
        private TextView artistTextView;
        private TextView dateTimeTextView;
        private TextView typeTextView;

        public UpcomingHolder(View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.upcomingFragment_textView_displayName);
            artistTextView = itemView.findViewById(R.id.upcomingFragment_textView_artist);
            dateTimeTextView = itemView.findViewById(R.id.upcomingFragment_textView_dateTime);
            typeTextView = itemView.findViewById(R.id.upcomingFragment_textView_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListItemClick(clickedPosition);
        }
    }
}
