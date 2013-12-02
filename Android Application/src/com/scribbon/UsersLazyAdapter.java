package com.scribbon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UsersLazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    public String[] fNames;
    public String[] fEmails;
    
    public UsersLazyAdapter(Activity a, String[] d, String[] names, String[] emails) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        fNames = names;
        fEmails = emails;
    }
    

    public View getView(int position, View convertView, ViewGroup parent){
		View vi = convertView;
        if(convertView == null)
        vi = inflater.inflate(R.layout.users, null);
        ImageView image =(ImageView)vi.findViewById(R.id.iAvatar);
        TextView name = (TextView)vi.findViewById(R.id.tvName);
        //TextView email = (TextView)vi.findViewById(R.id.tvEmail);
        name.setText(" " + fNames[position]);
        imageLoader.DisplayImage(data[position], image);
    	return vi;
    }
    
    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    

}