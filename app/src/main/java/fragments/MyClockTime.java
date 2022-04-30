package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plutecoder.geoattendance.MainActivity;
import com.plutecoder.geoattendance.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import settergetter.CategoryMainTM;
import settergetter.ClockOnOffGtSt;
import settergetter.UserGtSt;
import utils.AppController;
import utils.Constant;
import utils.PreferenceHelper;

public class MyClockTime extends Fragment {

    View view;
    private LinearLayoutManager mLayoutManager;
    ProgressBar pb;
    private JSONArray listFriendID = new JSONArray();
    ArrayList<ClockOnOffGtSt> friendlist;
    private GridLayoutManager lLayout;
    int check = 0;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    RecyclerView contactrecycler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.studentlistfragment, container, false);

        contactrecycler = view.findViewById(R.id.contactrecycler);
        pb = view.findViewById(R.id.progressbar);

        try{

            contactrecycler.setLayoutManager(new LinearLayoutManager(MainActivity.activity, LinearLayoutManager.VERTICAL, false));

        }catch (Exception e){
            e.printStackTrace();

        }

getdata("clockonoff/"+PreferenceHelper.getuseruid(getActivity()));

        return view;
    }

    public void getdata(String url) {
         System.out.println("-------------" + url);

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
                    System.out.println("is data----------------" + dataSnapshot.toString());

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
        MainActivity.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.INVISIBLE);
                if (list != null && !list.isEmpty()) {

                    RecyclerViewAdapterHor rcAdapter = new RecyclerViewAdapterHor(getActivity(), list);
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
