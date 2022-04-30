package com.plutecoder.geoattendance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fragments.MyClockTime;
import settergetter.ClockOnOffGtSt;
import settergetter.UserGtSt;
import utils.PreferenceHelper;
import utils.Typefaces;

public class AttendanceListActivity extends AppCompatActivity {
    private LinearLayoutManager mLayoutManager;
    ProgressBar pb;
    private JSONArray listFriendID = new JSONArray();


    ArrayList<ClockOnOffGtSt> friendlist;
    private GridLayoutManager lLayout;
    int check = 0;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    RecyclerView contactrecycler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentlistfragment);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        try {
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.gereenapp), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        contactrecycler = findViewById(R.id.contactrecycler);
        pb = findViewById(R.id.progressbar);

        try{

            contactrecycler.setLayoutManager(new LinearLayoutManager(AttendanceListActivity.this, LinearLayoutManager.VERTICAL, false));

        }catch (Exception e){
            e.printStackTrace();

        }

        String url=getIntent().getExtras().getString("URL");
        try {
            String name = getIntent().getExtras().getString("NAME");
            changeActionbarTitle(name+" Attendance");
        }catch (Exception e){
            e.printStackTrace();
        }

try {
    //get attendance of selected user
    getdata("clockonoff/" + url);
}catch (Exception e){
    e.printStackTrace();
}

    }
    public void changeActionbarTitle(String title) {
        //getSupportActionBar().setTitle(title);
        setActionBarTitle(AttendanceListActivity.this, title, getSupportActionBar());
    }

    public void setActionBarTitle(Context context, String title, ActionBar actionBar) {
        try {
            if (Typefaces.get(context, "HelveticaNeue Light") != null) {
                SpannableString SpanString = new SpannableString(title);
                SpanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpanString.setSpan(new utils.TypefaceSpan(context, Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue Light.ttf").toString()), 0, SpanString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                actionBar.setTitle(SpanString);
            } else {
                actionBar.setTitle(title);
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    public void getdata(String url) {
      //  System.out.println("-------------" + url);

        try {
            friendlist = new ArrayList<>();
            pb.setVisibility(View.VISIBLE);
            //
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference(url);
            mFirebaseDatabase.keepSynced(true);
            mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                 //   System.out.println("is data----------------" + dataSnapshot.toString());

                    for (DataSnapshot alert : dataSnapshot.getChildren()) {

                        HashMap<String, Object> yourData = (HashMap<String, Object>) alert.getValue();
                        ClockOnOffGtSt userGtSt = new ClockOnOffGtSt();
                        userGtSt.setDate(isweekendday(yourData.get("date").toString()));
                        userGtSt.setIntime((String) yourData.get("intime"));
                        userGtSt.setIsonleave((boolean) yourData.get("isonleave"));
                        userGtSt.setIsweeklyoff((boolean) yourData.get("isweeklyoff"));
                        userGtSt.setOuttime((String) yourData.get("outtime"));
                        friendlist.add(userGtSt);
                        UserGtSt categoryMainTM = new UserGtSt();
                    }
                    if (friendlist != null && !friendlist.isEmpty()) {
                        pb.setVisibility(View.INVISIBLE);
                        ShowUserList(friendlist);
                    } else {
                        pb.setVisibility(View.INVISIBLE);
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    pb.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            // avLoadingIndicatorView.hide();
        }
    }

    public String isweekendday(String strdate){
        try {
            Date date = stringToDate(strdate);
            System.out.println(new SimpleDateFormat("EEEE", Locale.getDefault()).format(date.getTime()));
            String finaldate = strdate + "\n" + new SimpleDateFormat("EEEE", Locale.getDefault()).format(date.getTime()).toString();
            return finaldate;
        }catch (Exception e){
            e.printStackTrace();
            return strdate;
        }
    }
    public String isweekenddaysunday(String strdate){
        try {
            Date date = stringToDate(strdate);
            System.out.println(new SimpleDateFormat("EEEE", Locale.getDefault()).format(date.getTime()));
            String finaldate =  "\n" + new SimpleDateFormat("EEEE", Locale.getDefault()).format(date.getTime()).toString();
            return finaldate;
        }catch (Exception e){
            e.printStackTrace();
            return strdate;
        }
    }


    public Date stringToDate(String strdate){

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            // Convert string into Date
            Date dateobject = df.parse(strdate);
            if (dateobject != null) {
                // create SimpleDateFormat object with input format
                return dateobject;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void ShowUserList(final ArrayList<ClockOnOffGtSt> list) {
        AttendanceListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.INVISIBLE);
                if (list != null && !list.isEmpty()) {

                    RecyclerViewAdapterHor rcAdapter = new RecyclerViewAdapterHor(AttendanceListActivity.this, list);
                    contactrecycler.setAdapter(rcAdapter);
                }
            }
        });
    }

    public class RecyclerViewAdapterHor extends RecyclerView.Adapter<RecyclerViewHoldershor> {

        private List<ClockOnOffGtSt> itemList;
        private Context context;
        public RecyclerViewAdapterHor(Context context, List<ClockOnOffGtSt> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public RecyclerViewHoldershor onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_viewholder_item, null);
            RecyclerViewHoldershor rcv = new RecyclerViewHoldershor(layoutView);
            return rcv;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(RecyclerViewHoldershor holder, final int position) {
            ClockOnOffGtSt userGtSt=itemList.get(position);
            if(userGtSt.isonleave){
                holder.textViewDate.setBackgroundResource(R.drawable.circularbackgroundorange);
                holder.textViewDate.setText(userGtSt.getDate());
                holder.tvintime.setText("On Leave");
                holder.tvapprovedouttime.setText("-");
                // holder.row.setBackgroundColor(R.color.green);

            }else if(userGtSt.isweeklyoff){
                holder.textViewDate.setBackgroundResource(R.drawable.circularbackgroundorange);

                holder.textViewDate.setText(userGtSt.getDate());
                holder.tvintime.setText("Weekly Off");
                holder.tvapprovedouttime.setText("-");
            }else{
                holder.textViewDate.setBackgroundResource(R.drawable.circularbluebackground);

                holder.textViewDate.setText(userGtSt.getDate());
                holder.tvintime.setText(userGtSt.getIntime());
                holder.tvapprovedouttime.setText(userGtSt.getOuttime());
            }
        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }
    }


    public class RecyclerViewHoldershor extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewDate, tvintime, tvapprovedouttime;
        // public ImageView countryPhoto;
        LinearLayout row;
        public RecyclerViewHoldershor(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            tvintime = (TextView) itemView.findViewById(R.id.tvintime);
            tvapprovedouttime = (TextView) itemView.findViewById(R.id.tvapprovedouttime);
            row=itemView.findViewById(R.id.row);

        }

        @Override
        public void onClick(View view) {
            // Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }


}
