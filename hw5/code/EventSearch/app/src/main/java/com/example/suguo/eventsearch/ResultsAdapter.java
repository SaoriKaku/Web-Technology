package com.example.suguo.eventsearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultHolder> {

    private ArrayList<EventResult> mdata;
    private ListItemClickListener mclickListener;

    public ResultsAdapter(ArrayList<EventResult> data, ListItemClickListener clickListener) {
        mdata = data;
        mclickListener = clickListener;
    }

    public interface ListItemClickListener {
        void onListItemDetailsClick(int listItemIndex);
        void onListItemFavoriteClick(int listItemIndex);
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_result_list_item, parent, false);
        ResultHolder holder = new ResultHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        EventResult currentEventResult = mdata.get(position);
        // the left icon: segment
        if (currentEventResult.getSegment().equals("Music")) {
            holder.segmentImageView.setImageResource(R.drawable.music_icon);
        }
        else if (currentEventResult.getSegment().equals("Sports")) {
            holder.segmentImageView.setImageResource(R.drawable.sport_icon);
        }
        else if (currentEventResult.getSegment().equals("Art") ) {
            holder.segmentImageView.setImageResource(R.drawable.art_icon);
        }
        else if (currentEventResult.getSegment().equals("Films")) {
            holder.segmentImageView.setImageResource(R.drawable.film_icon);
        }
        else {
            holder.segmentImageView.setImageResource(R.drawable.miscellaneous_icon);
        }
        // the middle details: eventName, venueName, dateTime
        holder.eventNameTextView.setText(currentEventResult.getEventName());
        holder.venueNameTextView.setText(currentEventResult.getVenueName());
        holder.dateTimeTextView.setText(currentEventResult.getDateTime());
        // the right icon: inFavorites
        if (currentEventResult.getInFavorites()) {
            holder.inFavoritesImageView.setImageResource(R.drawable.heart_fill_red);
        }
        else {
            holder.inFavoritesImageView.setImageResource(R.drawable.heart_outline_black);
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class ResultHolder extends RecyclerView.ViewHolder {
        private ImageView segmentImageView;
        private LinearLayout detailsLinearLayout;
        private TextView eventNameTextView;
        private TextView venueNameTextView;
        private TextView dateTimeTextView;
        private ImageView inFavoritesImageView;

        public ResultHolder(View itemView) {
            super(itemView);
            segmentImageView = (ImageView)itemView.findViewById(R.id.resultsAdapter_imageView_segment);
            detailsLinearLayout = (LinearLayout)itemView.findViewById(R.id.resultsAdapter_linearLayout_details_click);
            eventNameTextView = (TextView)itemView.findViewById(R.id.resultsAdapter_textView_eventName);
            venueNameTextView = (TextView)itemView.findViewById(R.id.resultsAdapter_textView_venueName);
            dateTimeTextView = (TextView)itemView.findViewById(R.id.resultsAdapter_textView_dateTime);
            inFavoritesImageView = (ImageView)itemView.findViewById(R.id.resultsAdapter_imageView_favorite_click);

            detailsLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickPosition = getAdapterPosition();
                    mclickListener.onListItemDetailsClick(clickPosition);
                }
            });

            inFavoritesImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickPosition = getAdapterPosition();
                    mclickListener.onListItemFavoriteClick(clickPosition);
                }
            });
        }
    }

}