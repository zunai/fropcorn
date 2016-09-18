package com.sourcey.materiallogindemo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventAdapter extends BaseAdapter{


    Context context;
    ArrayList<Event> listData;

    public EventAdapter(Context context,ArrayList<Event> listData){
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class ViewHolder {
        private TextView textViewCityName;
        private TextView textViewDate;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.event_item,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewCityName = (TextView) view.findViewById(R.id.txtViewEventName);
            view.setTag(viewHolder);
            viewHolder.textViewDate = (TextView) view.findViewById(R.id.date);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Event ev = listData.get(position);
        String eventName = ev.getName();
        String eventstartdate = ev.getStarttime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


        Date date = new Date();
        try {
             date = simpleDateFormat.parse(eventstartdate);

        } catch (ParseException e) {

        }

        viewHolder.textViewCityName.setText(eventName);
        viewHolder.textViewDate.setText(date.toString());
        return view;
    }
}