package com.qurankareem.ahmedalajami.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;*/

import com.qurankareem.ahmedalajami.Ads.AdsHelper;
import com.qurankareem.ahmedalajami.Models.Item;
import com.qurankareem.ahmedalajami.R;
import com.qurankareem.ahmedalajami.View.MainActivity;
import com.qurankareem.ahmedalajami.View.ReadFileActivity;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter implements Filterable {

    public ArrayList<Item> ItemArrayList;
    public ArrayList<Item> origItem;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public static boolean click=false;
    private String TAG = "ItemAdapter";
    AdsHelper ads;
    Activity activity;
    MainActivity activityMain;


    private static class ViewHolder {
        TextView tvtitle;
        ImageView imgvItem;
        LinearLayout lyitem;
    }

    public ItemAdapter(Context context, ArrayList<Item> itemsArrayList, Activity p_activity, MainActivity m) {
        super();
        this.mContext = context;
        this.ItemArrayList = itemsArrayList;
        this.activity = p_activity;
        ads = new AdsHelper(activity);
        activityMain = m;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //get the  information
        final String title = ItemArrayList.get(position).getTitle();
        final int id = ItemArrayList.get(position).getId();
        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.adapter_view_layout, parent, false);
            holder= new ViewHolder();
            holder.tvtitle =  convertView.findViewById(R.id.tvtitleItem);
            holder.lyitem =  convertView.findViewById(R.id.lyitem);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.tvtitle.setText(title);

        holder.lyitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click=true;
                Intent ReadFile = new Intent(mContext, ReadFileActivity.class);
                ReadFile.putExtra("pos",String.valueOf(id));
                ReadFile.putExtra("title",title);
                mContext.startActivity(ReadFile);


                //load so show the ads InterstitialAd
                activityMain.showInter();

            }
        });



        return convertView;
    }


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Item> results = new ArrayList<Item>();
                if (origItem == null)
                    origItem = ItemArrayList ;
                if (constraint != null) {
                    if (origItem != null && origItem.size() > 0) {
                        for (final Item g : origItem) {
                            if (g.getTitle().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                ItemArrayList = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return ItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Required for setting up the Universal Image loader Library
     */

}


























