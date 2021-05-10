package com.qurankareem.alaajami.View;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

/*import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;*/


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import static com.facebook.ads.CacheFlag.ALL;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.qurankareem.alaajami.Adapters.ItemAdapter;
import com.qurankareem.alaajami.Models.Item;
import com.qurankareem.alaajami.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private SearchView searchV;
    private ListView mListView;
    public static ArrayList<Item> listItems;
    private ItemAdapter adapter;
    private AdView mAdView;
    private String quran_sound;
    private String TAG = "MainActivity";
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<String> jcAudios;
    private ArrayList<String> Audios;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ads));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());*/
        AudienceNetworkAds.initialize(getApplicationContext());


        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mListView =  findViewById(R.id.listView);
        loadobjects();

        interstitialads();
        bannerAd();



    }

    public void bannerAd(){
        mAdView = new AdView(this, getString(R.string.fb_adbanner_id), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer  = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(mAdView);
        //Banner listeaner
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e("Error", "Error: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };

        AdView.AdViewLoadConfig loadAdConfig = mAdView.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();

        mAdView.loadAd(loadAdConfig);
    }

    public void interstitialads() {
        mInterstitialAd = new InterstitialAd(this, getString(R.string.fb_interstitial_ad_unit_id));

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


    private void loadobjects(){
        //Create the  objects

        final Item itempos1 = new Item("الفاتحة",1);

        final Item itempos2 = new Item("البقرة",2);

        final Item itempos3 = new Item("آل عمران",3);

        final Item itempos4 = new Item("النساء",4);

        final Item itempos5 = new Item("المآئدة",5);

        final Item itempos6 = new Item("الأنعام",6);

        final Item itempos7 = new Item("الأعراف",7);

        final Item itempos8 = new Item("الأنفال",8);

        final Item itempos9 = new Item("التوبة",9);

        final Item itempos10 = new Item("يونس",10);

        final Item itempos11 = new Item("هود",11);

        final Item itempos12 = new Item("يوسف",12);
        final Item itempos13 = new Item("الرعد",13);
        final Item itempos14 = new Item("إبراهيم",14);
        final Item itempos15 = new Item("الحجر",15);
        final Item itempos16 = new Item("النحل",16);
        final Item itempos17 = new Item("الإسراء",17);
        final Item itempos18 = new Item("الكهف",18);
        final Item itempos19 = new Item("مريم",19);
        final Item itempos20 = new Item("طـه",20);
        final Item itempos21 = new Item("الأنبياء",21);
        final Item itempos22 = new Item("الحج",22);
        final Item itempos23 = new Item("المؤمنون",23);
        final Item itempos24 = new Item("النور",24);
        final Item itempos25 = new Item("الفرقان",25);
        final Item itempos26 = new Item("الشعراء",26);
        final Item itempos27 = new Item("النمل",27);
        final Item itempos28 = new Item("القصص",28);
        final Item itempos29 = new Item("العنكبوت",29);
        final Item itempos30 = new Item("الروم",30);
        final Item itempos31 = new Item("لقمان",31);
        final Item itempos32 = new Item("السجدة",32);
        final Item itempos33 = new Item("الأحزاب",33);
        final Item itempos34 = new Item("سبأ",34);
        final Item itempos35 = new Item("فاطر",35);
        final Item itempos36 = new Item("يس",36);
        final Item itempos37 = new Item("الصافات",37);
        final Item itempos38 = new Item("ص",38);
        final Item itempos39 = new Item("الزمر",39);
        final Item itempos40 = new Item("غافر",40);
        final Item itempos41 = new Item("فصلت",41);
        final Item itempos42 = new Item("الشورى",42);
        final Item itempos43 = new Item("الزخرف",43);
        final Item itempos44 = new Item("الدخان",44);
        final Item itempos45 = new Item("الجاثية",45);
        final Item itempos46 = new Item("الأحقاف",46);
        final Item itempos47 = new Item("محمد",47);
        final Item itempos48 = new Item("الفتح",48);
        final Item itempos49 = new Item("الحجرات",49);
        final Item itempos50 = new Item("ق",50);
        final Item itempos51 = new Item("الذاريات",51);
        final Item itempos52 = new Item("الطور",52);
        final Item itempos53 = new Item("النجم",53);
        final Item itempos54 = new Item("القمر",54);
        final Item itempos55 = new Item("الرحمن",55);
        final Item itempos56 = new Item("الواقعة",56);
        final Item itempos57 = new Item("الحديد",57);
        final Item itempos58 = new Item("المجادلة",58);
        final Item itempos59 = new Item("الحشر",59);
        final Item itempos60 = new Item("الممتحنة",60);
        final Item itempos61 = new Item("الصف",61);
        final Item itempos62 = new Item("الجمعة",62);
        final Item itempos63 = new Item("المنافقون",63);
        final Item itempos64 = new Item("التغابن",64);
        final Item itempos65 = new Item("الطلاق",65);
        final Item itempos66 = new Item("التحريم",66);
        final Item itempos67 = new Item("الملك",67);
        final Item itempos68 = new Item("القلم",68);
        final Item itempos69 = new Item("الحاقة",69);
        final Item itempos70 = new Item("المعارج",70);
        final Item itempos71 = new Item("نوح",71);
        final Item itempos72 = new Item("الجن",72);
        final Item itempos73 = new Item("المزمل",73);
        final Item itempos74 = new Item("المدثر",74);
        final Item itempos75 = new Item("القيامة",75);
        final Item itempos76 = new Item("الإنسان",76);
        final Item itempos77 = new Item("المرسلات",77);
        final Item itempos78 = new Item("النبأ",78);
        final Item itempos79 = new Item("النازعات",79);
        final Item itempos80 = new Item("عبس",80);
        final Item itempos81 = new Item("التكوير",81);
        final Item itempos82 = new Item("الإنفطار",82);
        final Item itempos83 = new Item("المطففين",83);
        final Item itempos84 = new Item("الإنشقاق",84);
        final Item itempos85 = new Item("البرج",85);
        final Item itempos86 = new Item("الطارق",86);
        final Item itempos87 = new Item("الأعلى",87);
        final Item itempos88 = new Item("الغاشية",88);
        final Item itempos89 = new Item("الفجر",89);
        final Item itempos90 = new Item("البلد",90);
        final Item itempos91 = new Item("الشمس",91);
        final Item itempos92 = new Item("الليل",92);
        final Item itempos93 = new Item("الضحى",93);
        final Item itempos94 = new Item("الشرح",94);
        final Item itempos95 = new Item("التين",95);
        final Item itempos96 = new Item("العلق",96);
        final Item itempos97 = new Item("القدر",97);
        final Item itempos98 = new Item("البيِّنة",98);
        final Item itempos99 = new Item("الزلزلة",99);
        final Item itempos100 = new Item("العاديات",100);
        final Item itempos101 = new Item("القارعة",101);
        final Item itempos102 = new Item("التكاثر",102);
        final Item itempos103 = new Item("العصر",103);
        final Item itempos104 = new Item("الهمزة",104);
        final Item itempos105 = new Item("الفيل",105);
        final Item itempos106 = new Item("قريش",106);
        final Item itempos107 = new Item("الماعون",107);
        final Item itempos108 = new Item("الكوثر",108);
        final Item itempos109 = new Item("الكافرون",109);
        final Item itempos110 = new Item("النصر",110);
        final Item itempos111 = new Item("المسد",111);
        final Item itempos112 = new Item("الإخلاص",112);
        final Item itempos113 = new Item("الفلق",113);
        final Item itempos114 = new Item("الناس",114);



        //Add the item objects to an ArrayList
        listItems = new ArrayList<Item>();
        listItems.add(itempos1);
        listItems.add(itempos2);
        listItems.add(itempos3);
        listItems.add(itempos4);
        listItems.add(itempos5);
        listItems.add(itempos6);
        listItems.add(itempos7);
        listItems.add(itempos8);
        listItems.add(itempos9);
        listItems.add(itempos10);
        listItems.add(itempos11);
        listItems.add(itempos12);
        listItems.add(itempos13);
        listItems.add(itempos14);
        listItems.add(itempos15);
        listItems.add(itempos16);
        listItems.add(itempos17);
        listItems.add(itempos18);
        listItems.add(itempos19);
        listItems.add(itempos20);
        listItems.add(itempos21);
        listItems.add(itempos22);
        listItems.add(itempos23);
        listItems.add(itempos24);
        listItems.add(itempos25);
        listItems.add(itempos26);
        listItems.add(itempos27);
        listItems.add(itempos28);
        listItems.add(itempos29);
        listItems.add(itempos30);
        listItems.add(itempos31);
        listItems.add(itempos32);
        listItems.add(itempos33);
        listItems.add(itempos34);
        listItems.add(itempos35);
        listItems.add(itempos36);
        listItems.add(itempos37);
        listItems.add(itempos38);
        listItems.add(itempos39);
        listItems.add(itempos40);
        listItems.add(itempos41);
        listItems.add(itempos42);
        listItems.add(itempos43);
        listItems.add(itempos44);
        listItems.add(itempos45);
        listItems.add(itempos46);
        listItems.add(itempos47);
        listItems.add(itempos48);
        listItems.add(itempos49);
        listItems.add(itempos50);
        listItems.add(itempos51);
        listItems.add(itempos52);
        listItems.add(itempos53);
        listItems.add(itempos54);
        listItems.add(itempos55);
        listItems.add(itempos56);
        listItems.add(itempos57);
        listItems.add(itempos58);
        listItems.add(itempos59);
        listItems.add(itempos60);
        listItems.add(itempos61);
        listItems.add(itempos62);
        listItems.add(itempos63);
        listItems.add(itempos64);
        listItems.add(itempos65);
        listItems.add(itempos66);
        listItems.add(itempos67);
        listItems.add(itempos68);
        listItems.add(itempos69);
        listItems.add(itempos70);
        listItems.add(itempos71);
        listItems.add(itempos72);
        listItems.add(itempos73);
        listItems.add(itempos74);
        listItems.add(itempos75);
        listItems.add(itempos76);
        listItems.add(itempos77);
        listItems.add(itempos78);
        listItems.add(itempos79);
        listItems.add(itempos80);
        listItems.add(itempos81);
        listItems.add(itempos82);
        listItems.add(itempos83);
        listItems.add(itempos84);
        listItems.add(itempos85);
        listItems.add(itempos86);
        listItems.add(itempos87);
        listItems.add(itempos88);
        listItems.add(itempos89);
        listItems.add(itempos90);
        listItems.add(itempos91);
        listItems.add(itempos92);
        listItems.add(itempos93);
        listItems.add(itempos94);
        listItems.add(itempos95);
        listItems.add(itempos96);
        listItems.add(itempos97);
        listItems.add(itempos98);
        listItems.add(itempos99);
        listItems.add(itempos100);
        listItems.add(itempos101);
        listItems.add(itempos102);
        listItems.add(itempos103);
        listItems.add(itempos104);
        listItems.add(itempos105);
        listItems.add(itempos106);
        listItems.add(itempos107);
        listItems.add(itempos108);
        listItems.add(itempos109);
        listItems.add(itempos110);
        listItems.add(itempos111);
        listItems.add(itempos112);
        listItems.add(itempos113);
        listItems.add(itempos114);


        adapter = new ItemAdapter(this, listItems);
        mListView.setAdapter(adapter);

        mListView.setTextFilterEnabled(true);



    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchV = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        searchV.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        searchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                //load
                //adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (TextUtils.isEmpty(query)) {
                    mListView.clearTextFilter();
                } else {
                    mListView.setFilterText(query);
                }
                return true;

            }

        });

        setupSearchView();
        return true;
    }


    private void setupSearchView()
    {

        searchV.setQueryHint(getString(R.string.searchhere));
    }

    /// selected items menu:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int check = item.getItemId();
        switch(check) {

            case R.id.itemMShareApp:
                onClickShareApp();
                break;

            case R.id.itemRating:
                launchMarket();
                break;

            default:
        }
        return super.onOptionsItemSelected(item);

    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            //Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        mInterstitialAd.show();
        if(!searchV.isIconified()){
            searchV.setIconified(true);

        }else{
            super.onBackPressed();
            listItems.clear();
            //load
            loadobjects();
        }

    }


    public void onClickShareApp(){
        Intent sharePst = new Intent(Intent.ACTION_SEND);
        sharePst.setType("text/plain");
        sharePst.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app)+" "+"https://play.google.com/store/apps/details?id="+getPackageName());

        startActivity(Intent.createChooser(sharePst,getString(R.string.choose_app_share)));
    }

}
