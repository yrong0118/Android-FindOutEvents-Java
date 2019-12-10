package com.yue.findoutevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends BaseAdapter {
    Context context;
    List<Event> eventData;

    public EventAdapter(Context context) {
        this.context = context;
        eventData = DataService.getEventData();
    }

    @Override
    public int getCount() {
        return eventData.size();
    }

    @Override
    public Event getItem(int position) {
        return eventData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item,
                    parent, false);
        }

        TextView eventTitle = (TextView) convertView.findViewById(
                R.id.event_title);
        TextView eventAddress = (TextView) convertView.findViewById(
                R.id.event_address);
        TextView eventDescription = (TextView) convertView.findViewById(
                R.id.event_description);

        ImageView eventImage = (ImageView) convertView.findViewById(R.id.event_thumbnail);

        Event r = eventData.get(position);
        eventTitle.setText(r.getTitle());
        eventAddress.setText(r.getAddress());
        eventDescription.setText(r.getDescription());

        if (position < 2) {
            eventImage.setImageDrawable(context.getDrawable(R.drawable.event_thumbnail));
        } else if (position >= 2 && position < 4) {
            eventImage.setImageDrawable(context.getDrawable(R.drawable.event_thumbnail));
        } else if (position >= 4 && position < 6) {
            eventImage.setImageDrawable(context.getDrawable(R.drawable.event_thumbnail));
        } else {
            eventImage.setImageDrawable(context.getDrawable(R.drawable.event_thumbnail));
        }

        return convertView;
    }
}
