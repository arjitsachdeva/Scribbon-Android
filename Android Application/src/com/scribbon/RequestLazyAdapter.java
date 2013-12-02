package com.scribbon;

 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
 
public class RequestLazyAdapter extends BaseAdapter {
 
    private Activity activity;
    private String[] fNames, fEmails, fPics, fTypes;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
 
    public RequestLazyAdapter(Activity a, String[] names, String[] emails, String[] pp, String[] uids, String[] types) {
        activity = a;
        fNames = names;
        fEmails = emails;
        fPics = pp;
        fTypes = types;
        
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return fPics.length;
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.request, null);
 
        ImageView thumb_image = (ImageView)vi.findViewById(R.id.ivPP);
        TextView name = (TextView)vi.findViewById(R.id.tvFName);
        TextView email = (TextView)vi.findViewById(R.id.tvFEmail);
        Button accept = (Button)vi.findViewById(R.id.bAccept);
        Button reject = (Button)vi.findViewById(R.id.bReject);
        TextView pending = (TextView)vi.findViewById(R.id.tvPending);
 
        // Setting all values in listview
        name.setText(fNames[position]);
        email.setText(fEmails[position]);
        
        if(fTypes[position].equals("o")){
        	accept.setVisibility(View.GONE);
        	reject.setVisibility(View.GONE);
        	pending.setVisibility(View.VISIBLE);
        }
        else{
        	accept.setVisibility(View.VISIBLE);
        	reject.setVisibility(View.VISIBLE);
        	pending.setVisibility(View.GONE);
        }
        imageLoader.DisplayImage(fPics[position], thumb_image);
        return vi;
    }
}