package com.yue.findoutevents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<Event> eventList;
    private DatabaseReference databaseReference;
    private Context context;

    /**
     * Constructor for EventListAdapter
     *
     * @param events events that are showing on screen
     */
    public EventListAdapter(List<Event> events, final Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        eventList = events;
        this.context = context;
    }

    /**
     * By calling this method, each ViewHolder will be initiated and passed to OnBindViewHolder
     * for rendering
     * @param parent parent view
     * @param viewType we might have multiple view types
     * @return ViewHolder created
     */
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.event_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    /**
     * OnBindViewHolder will render created view holder on screen
     * @param holder View Holder created for each position
     * @param position position needs to show
     */



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final  Event event = eventList.get(position);
        holder.title.setText(event.getTitle());
        String[] locations = event.getAddress().split(",");
        holder.location.setText(locations[1]+","+locations[2]);
        holder.description.setText(event.getDescription());
        holder.time.setText(String.valueOf(event.getTime()));
        holder.time.setText(Utils.timeTransformer(event.getTime()));
        holder.good_number.setText(String.valueOf(event.getLike()));

        if (event.getImgUri() != null) {
            final String url = event.getImgUri();
            holder.imgview.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Bitmap>(){
                @Override
                protected Bitmap doInBackground(Void... params) {
                    return Utils.getBitmapFromURL(url);
                }
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    holder.imgview.setImageBitmap(bitmap);
                }
            }.execute();
        } else {
            holder.imgview.setVisibility(View.GONE);
        }

        //When user likes the event, push like number to firebase database
        holder.img_view_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Event recordedevent = snapshot.getValue(Event.class);
                            if (recordedevent.getId().equals(event.getId())) {
                                int number = recordedevent.getLike();
                                holder.good_number.setText(String.valueOf(number + 1));
                                snapshot.getRef().child("like").setValue(number + 1);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, CommentActivity.class);
//                String eventId = event.getId();
//                intent.putExtra("EventID", eventId);
//                context.startActivity(intent);
//            }
//        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                String eventId = event.getId();
                intent.putExtra("EventID", Utils.COMMENT_KEY);
                context.startActivity(intent);
            }
        });


    }


    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return eventList.size();
    }



    /**
     * Use ViewHolder to hold view widget, view holder is required to be used in recycler view
     * https://developer.android.com/training/improving-layouts/smooth-scrolling.html
     * describe the advantage of using view holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView location;
        public TextView description;
        public TextView time;
        public ImageView imgview;

        public ImageView img_view_good;
        public ImageView img_view_comment;

        public TextView good_number;
        public TextView comment_number;

        public View  layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            title = (TextView)itemView.findViewById(R.id.event_item_title);
            location = (TextView)itemView.findViewById(R.id.event_item_location);
            description = (TextView)itemView.findViewById(R.id.event_item_description);
            time = (TextView)itemView.findViewById(R.id.event_item_time);
            imgview = (ImageView)itemView.findViewById(R.id.event_item_img);
            img_view_good = (ImageView) itemView.findViewById(R.id.event_good_img);
            img_view_comment = (ImageView) itemView.findViewById(R.id.event_comment_img);
            good_number = (TextView) itemView.findViewById(R.id.event_good_number);
            comment_number = (TextView) itemView.findViewById(R.id.event_comment_number);

        }


    }


}


