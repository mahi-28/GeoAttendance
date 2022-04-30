package com.plutecoder.geoattendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import settergetter.IntroList_Been;
import utils.PreferenceHelper;

public class IntroScreenGeo extends AppCompatActivity {
    private IntroPagerAdapter adapter;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    TextView skip;
    private Intent intent;
    String goActivity="home";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introscreen_layout);
        viewPager= (ViewPager) findViewById(R.id.as_vpIntro);
        circleIndicator= (CircleIndicator) findViewById(R.id.indicator);
        skip= (TextView) findViewById(R.id.tv_Skip);
        try {
            ArrayList<IntroList_Been> list = new ArrayList<>();
            list.add(new IntroList_Been(R.drawable.gfirst, getString(R.string.gtext_slider_title1), getString(R.string.gtext_slider_mesg1)));
            list.add(new IntroList_Been(R.drawable.gsecond, getString(R.string.gtext_slider_title2), getString(R.string.gtext_slider_mesg2)));
            list.add(new IntroList_Been(R.drawable.gthird, getString(R.string.gtext_slider_title3), getString(R.string.gtext_slider_mesg3)));
            ShowIntro(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if(i==2){
                    skip.setText("Got it!");
                }else{
                    skip.setText("Skip");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PreferenceHelper.StoreIntroShown(IntroScreenGeo.this,true);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    Intent intent = new Intent(IntroScreenGeo.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    public void ShowIntro(final ArrayList<IntroList_Been> list){
        IntroScreenGeo.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new IntroPagerAdapter(IntroScreenGeo.this, list);
                viewPager.setAdapter(adapter);
                circleIndicator.setViewPager(viewPager);
            }
        });
    }

    public class IntroPagerAdapter extends PagerAdapter {

        private Context mContext;
        private ArrayList<IntroList_Been> list = new ArrayList<>();

        public IntroPagerAdapter(Context context, ArrayList<IntroList_Been> list) {
            mContext = context;
            this.list = list;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = mLayoutInflater.inflate(R.layout.adapter_intro_pager, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.li_ivCommon);
            TextView tv_Title = (TextView) itemView.findViewById(R.id.tv_Title);
            TextView tv_Messsage = (TextView) itemView.findViewById(R.id.tv_Messsage);

            imageView.setImageResource(list.get(position).getImage());

            if (list.get(position).getTitle()!=null && !list.get(position).getTitle().equalsIgnoreCase(""))
            {
                tv_Title.setVisibility(View.VISIBLE);
                tv_Title.setText(list.get(position).getTitle());
            }
            else
            {
                tv_Title.setVisibility(View.GONE);
            }

            if (list.get(position).getMessage()!=null && !list.get(position).getMessage().equalsIgnoreCase(""))
            {
                tv_Messsage.setVisibility(View.VISIBLE);
                tv_Messsage.setText(list.get(position).getMessage());
            }
            else
            {
                tv_Messsage.setVisibility(View.GONE);
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((RelativeLayout) object);
        }


        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

}
