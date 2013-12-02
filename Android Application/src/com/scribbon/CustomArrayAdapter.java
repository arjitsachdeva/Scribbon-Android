package com.scribbon;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class CustomArrayAdapter extends ArrayAdapter {
    
    private Activity activity;
    private String[] iNames, iMessages, iTimes, iUids;
    private String iUID;
    private static LayoutInflater inflater = null;
    
    public CustomArrayAdapter(Activity a, int d, String[] names, String[] uids, String[] messages, String[] times, String uid) {
    	super(a,d);
        activity = a;
        iNames = names;
        iMessages = messages;
        iTimes = times;
        iUids = uids;
        iUID = uid;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return iNames.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        View vi = convertView;
        vi = inflater.inflate(R.layout.list_item, null);
        
        Resources res = getContext().getResources();
        Drawable chat = res.getDrawable(R.drawable.in);
        Drawable reply = res.getDrawable(R.drawable.out);
        
        TextView message = (TextView)vi.findViewById(R.id.tvMessage);
        TextView name = (TextView)vi.findViewById(R.id.tvName);
	    TextView time = (TextView)vi.findViewById(R.id.tvTime);
	    
        message.setText(iMessages[position]);
	    name.setText(iNames[position]);
        time.setText(iTimes[position]);
        
        if(iUID.equals(iUids[position])) {
	    	name.setGravity(Gravity.LEFT);
	    	time.setGravity(Gravity.RIGHT);
	    	vi.setBackgroundDrawable(chat);
	    	
	    } else {
	    	name.setGravity(Gravity.RIGHT);
	    	time.setGravity(Gravity.LEFT);
	    	vi.setBackgroundDrawable(reply);
	    }
	    
        return vi;
    }
}