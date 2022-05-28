package com.plutecoder.geoattendance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fragments.MyClockTime;
import settergetter.ClockOnOffGtSt;
import settergetter.StaffGtSt;
import settergetter.UserGtSt;
import utils.Constant;
import utils.PreferenceHelper;
import utils.Typefaces;
import utils.Utility;

public class AdminActivity extends AppCompatActivity {
    ArrayList<StaffGtSt> friendlist;
    private GridLayoutManager lLayout;

    // private RecyclerView recyclerView;
    int check = 0;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    RecyclerView contactrecycler;
    ProgressBar pb;
    Button btnlogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stafflist_activity);
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
        changeActionbarTitle("Staff List");
        btnlogout = findViewById(R.id.btnlogout);

        contactrecycler = findViewById(R.id.contactrecycler);
        pb = findViewById(R.id.progressbar);

        try {

            contactrecycler.setLayoutManager(new LinearLayoutManager(AdminActivity.this, LinearLayoutManager.VERTICAL, false));

        } catch (Exception e) {
            e.printStackTrace();

        }
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PreferenceHelper.storeLoginstatus(AdminActivity.this, false);
                    FirebaseAuth.getInstance().signOut();
                    AdminActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getdata("users");

    }

    public void changeActionbarTitle(String title) {
        //getSupportActionBar().setTitle(title);
        setActionBarTitle(AdminActivity.this, title, getSupportActionBar());
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

    //get all user data
    public void getdata(String url) {
       // System.out.println("-------------" + url);

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
                    // System.out.println("is data----------------" + dataSnapshot.toString());

                    for (DataSnapshot alert : dataSnapshot.getChildren()) {
                         //System.out.println("is alert----------------" + alert.getKey());


                        HashMap<String, Object> yourData = (HashMap<String, Object>) alert.getValue();
                        if (!yourData.get("username").toString().equals("admin")) {
                            StaffGtSt userGtSt = new StaffGtSt();
                            userGtSt.setUid( alert.getKey());
                            userGtSt.setName(yourData.get("name").toString());
                            userGtSt.setDesignation(yourData.get("designation").toString());
                            userGtSt.setStarttime(yourData.get("starttime").toString());
                            userGtSt.setEndtime(yourData.get("endtime").toString());
                            // System.out.println("is data----------------" + yourData.get("username").toString());
                            friendlist.add(userGtSt);
                        }


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
                    //  avLoadingIndicatorView.hide();
                    // Failed to read value
                    // Log.w(TAG,"Failed to read value.",error.toException());
                    pb.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            // avLoadingIndicatorView.hide();
        }
    }

    public void ShowUserList(final ArrayList<StaffGtSt> list) {
        AdminActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.INVISIBLE);
                if (list != null && !list.isEmpty()) {

                    RecyclerViewAdapterHor rcAdapter = new RecyclerViewAdapterHor(AdminActivity.this, list);
                    contactrecycler.setAdapter(rcAdapter);
                }
            }
        });
    }

    public class RecyclerViewAdapterHor extends RecyclerView.Adapter<RecyclerViewHoldershor> {

        private List<StaffGtSt> itemList;
        private Context context;

        public RecyclerViewAdapterHor(Context context, List<StaffGtSt> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public RecyclerViewHoldershor onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stafflistrow, null);
            RecyclerViewHoldershor rcv = new RecyclerViewHoldershor(layoutView);
            return rcv;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(RecyclerViewHoldershor holder, final int position) {
            final StaffGtSt userGtSt = itemList.get(position);

            holder.staffname.setText(userGtSt.getName());
            holder.designamtion.setText(userGtSt.getDesignation());
            try {
                holder.addtiem.setText("End time: " + userGtSt.getEndtime());
                holder.removeitem.setText("Start Time: " + userGtSt.getStarttime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.btnreport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mFirebaseInstance = FirebaseDatabase.getInstance();

                        mFirebaseDatabase = mFirebaseInstance.getReference("clockonoff/"+userGtSt.getUid());
                        mFirebaseDatabase.keepSynced(true);

                        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              // ----------PDF DETUP ______________
                                PdfDocument myPdfDocument = new PdfDocument();
                                 Paint paint = new Paint();
                                paint.setTextSize(12.0f);


                                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(500,884,1).create();
                                PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
                                 Canvas canvas = myPage.getCanvas();

                                canvas.drawText("Prof Name : " + userGtSt.getName() ,30,30,paint);
                                canvas.drawLine(20,55,470,55,paint);
                                canvas.drawText("Sr. No. ",30,75,paint);
                                canvas.drawText("Date  ",104,75,paint);
                                canvas.drawText("In Time  ",265,75,paint);
                                canvas.drawText("Out Time  ",380,75,paint);
                                canvas.drawLine(20,85,470,85,paint);
//            Vertical Lines
                                canvas.drawLine(20,55,20,870,paint);
                                canvas.drawLine(80,55,80,870,paint);

                                canvas.drawLine(250,55,250,870,paint);
                                canvas.drawLine(350,55,350,870,paint);
                                canvas.drawLine(470,55,470,870,paint);

                                canvas.drawLine(20,870,470,870,paint);
                                // ________________End PDF SETUP
                                System.out.println("is data----------------" + dataSnapshot.toString());
                                int c1=40,c2=115,c3=270,c4=385,r=100;
                                int i=0;
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    HashMap<String, Object> yourData = (HashMap<String, Object>) snap.getValue();
                                    ClockOnOffGtSt userGtSt = new ClockOnOffGtSt();
                                    userGtSt.setDate(yourData.get("date").toString());
                                    userGtSt.setIntime((String) yourData.get("intime"));
                                    userGtSt.setIsonleave((boolean) yourData.get("isonleave"));
                                    userGtSt.setIsweeklyoff((boolean) yourData.get("isweeklyoff"));
                                    userGtSt.setOuttime((String) yourData.get("outtime"));

                                    canvas.drawText(i+1+"",c1,r,paint);
                                    canvas.drawText(userGtSt.getDate()+"",c2,r,paint);
                                    canvas.drawText(userGtSt.getIntime()+"",c3,r,paint);
                                    canvas.drawText(userGtSt.getOuttime()+"",c4,r,paint);

                                    r+=25;
                                    i++;
                                }

                               //----------------------------------------------
//
//                                HashMap<String, Object> userData = new HashMap<String, Object> ();
//                                HashMap<String, String> d = new HashMap<String, String>();
////                                   System.out.println("is data----------------" + dataSnapshot.toString());
//
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                                    if(snapshot.getKey().equals(userGtSt.getUid())) {
////                                        snapshot.getValue()
////                                         snapshot.getValue();
//                                        userData = (HashMap<String, Object>) snapshot.getValue();
////                                        System.out.println("--------size-" + yourData);
//
//                                    }
//                                }
//                                for (Map.Entry<String, Object> e : userData.entrySet())
//                                {
//                                     d = (HashMap<String, String>) e.getValue();
//                                    for (Map.Entry<String, String> em : d.entrySet()){
//                                        System.out.println("---------------------------------------");
//                                        System.out.println("Key: " + em.getKey()+" Value: " + em.getValue());
//                                    }
//
//                                }
                                //--------------------------------------------
                                myPdfDocument.finishPage(myPage);
                                File file = new File(AdminActivity.this.getExternalFilesDir("/"),userGtSt.getName()+" Report.pdf");

                                try {
                                    myPdfDocument.writeTo(new FileOutputStream(file));
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                myPdfDocument.close();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    catch (Exception E){
                        E.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),"Attendance Reported Generated !!", Toast.LENGTH_SHORT).show();
                }
            });





            holder.clickcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        StaffGtSt userGtSt = itemList.get(position);
                        Intent i =new Intent(AdminActivity.this, AttendanceListActivity.class);
                        i.putExtra("URL", userGtSt.getUid());
                        i.putExtra("NAME",userGtSt.getName());
                        startActivity(i);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }
    }


    public class RecyclerViewHoldershor extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView staffname, designamtion, addtiem, removeitem;
        public Button btnreport;
        // public ImageView countryPhoto;
        // LinearLayout row;
        CardView clickcard;

        public RecyclerViewHoldershor(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            staffname = (TextView) itemView.findViewById(R.id.staffname);
            designamtion = (TextView) itemView.findViewById(R.id.designation);
            clickcard = itemView.findViewById(R.id.clickcard);
            try {
                btnreport = (Button) itemView.findViewById(R.id.btnreport);
                addtiem = (TextView) itemView.findViewById(R.id.add_item);
                removeitem = (TextView) itemView.findViewById(R.id.remove_item);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //    row=itemView.findViewById(R.id.row);

        }

        @Override
        public void onClick(View view) {
            // Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
