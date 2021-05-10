package com.qurankareem.alaajami.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import static com.facebook.ads.CacheFlag.ALL;

import com.qurankareem.alaajami.Models.Item;
import com.qurankareem.alaajami.R;
import com.qurankareem.alaajami.View.ReadFileActivity;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter implements Filterable {

    public ArrayList<Item> ItemArrayList;
    public ArrayList<Item> origItem;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private InterstitialAd mInterstitialAd;
    public static boolean click=false;
    private String TAG = "ItemAdapter";
    private static class ViewHolder {
        TextView tvtitle;
        ImageView imgvItem;
        LinearLayout lyitem;
    }

    public ItemAdapter(Context context, ArrayList<Item> itemsArrayList) {
        super();
        this.mContext = context;
        this.ItemArrayList = itemsArrayList;

        /*// Interstitial ads
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.interstitial_ads));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());*/

        interstitialads();
    }

    public void interstitialads() {
        mInterstitialAd = new InterstitialAd(mContext, mContext.getString(R.string.fb_interstitial_ad_unit_id));

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                interstitialads();

                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        mInterstitialAd.loadAd(mInterstitialAd.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .withCacheFlags(ALL)
                .build());

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
                if(mInterstitialAd.isAdLoaded())
                    mInterstitialAd.show();

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
