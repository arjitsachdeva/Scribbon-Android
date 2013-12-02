package com.scribbon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsLazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    public String[] fNames;
    
    public FriendsLazyAdapter(Activity a, String[] d, String[] names) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        fNames = names;
    }
    

    public View getView(int position, View convertView, ViewGroup parent){
		View vi = convertView;
        if(convertView == null)
        vi = inflater.inflate(R.layout.friends, null);
        TextView text=(TextView)vi.findViewById(R.id.tvName);
        ImageView image=(ImageView)vi.findViewById(R.id.iAvatar);
        text.setText(" " + fNames[position]);
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