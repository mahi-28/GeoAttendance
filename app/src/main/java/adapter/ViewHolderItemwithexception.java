package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plutecoder.geoattendance.R;


/**
 * Created by Administrator on 11/28/2016.
 */
public class ViewHolderItemwithexception extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ViewHolderItemwithexception(View itemview, Context context) {
        super(itemview);
        inTypeExcetionicon=(ImageView) itemView.findViewById(R.id.inTypeExcetionicon);
        outTypeExcetionicon=(ImageView)itemView.findViewById(R.id.outTypeExcetionicon);
        tvdate=(TextView)itemView.findViewById(R.id.textViewDate);
        tvapprovedintime=(TextView)itemView.findViewById(R.id.tvapprovedintime);
        tvapprovedouttime=(TextView)itemView.findViewById(R.id.tvapprovedouttime);
        Lin = (LinearLayout) itemView.findViewById(R.id.L1);
        Lout = (LinearLayout) itemView.findViewById(R.id.L2);
        tvintime=(TextView)itemView.findViewById(R.id.tvintime);
        tvstanddardintime=(TextView)itemView.findViewById(R.id.textView3);
        tvouttime=(TextView)itemView.findViewById(R.id.tvouttime);
        tvstandardouttime=(TextView)itemView.findViewById(R.id.tvstandardouttime);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"arial.ttf");
        tvdate.setTypeface(tf, Typeface.NORMAL);
        tvintime.setTypeface(tf, Typeface.BOLD);
        tvstanddardintime.setTypeface(tf, Typeface.NORMAL);
        tvouttime.setTypeface(tf, Typeface.BOLD);
        tvstandardouttime.setTypeface(tf, Typeface.NORMAL);
        ((TextView)itemView.findViewById(R.id.in)).setTypeface(tf, Typeface.NORMAL);
        ((TextView)itemView.findViewById(R.id.out)).setTypeface(tf, Typeface.NORMAL);
        tvdate.setTypeface(tf, Typeface.NORMAL);
        tvdate.setTypeface(tf, Typeface.NORMAL);
        tvapprovedouttime.setTypeface(tf, Typeface.NORMAL);
        tvapprovedintime.setTypeface(tf, Typeface.NORMAL);
    }

    private TextView tvdate,tvintime,tvstanddardintime,tvouttime,tvstandardouttime,tvapprovedintime,tvapprovedouttime;
    private LinearLayout Lin,Lout;

    private ImageView inTypeExcetionicon,outTypeExcetionicon;

    public void setTvdate(TextView tvdate) {
        this.tvdate = tvdate;
    }

    public void setTvintime(TextView tvintime) {
        this.tvintime = tvintime;
    }

    public void setTvstanddardintime(TextView tvstanddardintime) {
        this.tvstanddardintime = tvstanddardintime;
    }

    public void setTvouttime(TextView tvouttime) {
        this.tvouttime = tvouttime;
    }

    public void setTvstandardouttime(TextView tvstandardouttime) {
        this.tvstandardouttime = tvstandardouttime;
    }

    public void setTvapprovedintime(TextView tvapprovedintime) {
        this.tvapprovedintime = tvapprovedintime;
    }

    public void setTvapprovedouttime(TextView tvapprovedouttime) {
        this.tvapprovedouttime = tvapprovedouttime;
    }

    public void setLin(LinearLayout lin) {
        Lin = lin;
    }

    public void setLout(LinearLayout lout) {
        Lout = lout;
    }

    public void setInTypeExcetionicon(ImageView inTypeExcetionicon) {
        this.inTypeExcetionicon = inTypeExcetionicon;
    }

    public void setOutTypeExcetionicon(ImageView outTypeExcetionicon) {
        this.outTypeExcetionicon = outTypeExcetionicon;
    }

    public TextView getTvdate() {
        return tvdate;
    }

    public TextView getTvintime() {
        return tvintime;
    }

    public TextView getTvstanddardintime() {
        return tvstanddardintime;
    }

    public TextView getTvouttime() {
        return tvouttime;
    }

    public TextView getTvstandardouttime() {
        return tvstandardouttime;
    }

    public TextView getTvapprovedintime() {
        return tvapprovedintime;
    }

    public TextView getTvapprovedouttime() {
        return tvapprovedouttime;
    }

    public LinearLayout getLin() {
        return Lin;
    }

    public LinearLayout getLout() {
        return Lout;
    }

    public ImageView getInTypeExcetionicon() {
        return inTypeExcetionicon;
    }

    public ImageView getOutTypeExcetionicon() {
        return outTypeExcetionicon;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.L1)
        {
            int position=0;
            switch (position)
            {
                case 1:

            }



        }else if(v.getId()==R.id.L2)
        {

        }
    }
}
