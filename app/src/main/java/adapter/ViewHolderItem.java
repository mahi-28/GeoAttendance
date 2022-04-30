package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plutecoder.geoattendance.R;

/**
 * Created by
 */
public class ViewHolderItem  extends RecyclerView.ViewHolder {
    private TextView tvdate,tvintime,tvstanddardintime,tvouttime,tvstandardouttime,tvapprovedintime,tvapprovedouttime;
    private LinearLayout Lin,Lout;

    public LinearLayout getLin() {
        return Lin;
    }

    public void setLin(LinearLayout lin) {
        Lin = lin;
    }

    public LinearLayout getLout() {
        return Lout;
    }

    public void setLout(LinearLayout lout) {
        Lout = lout;
    }

    public ViewHolderItem(View itemView, Context context) {
        super(itemView);
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

    public TextView getTvdate() {
        return tvdate;
    }

    public void setTvdate(TextView tvdate) {
        this.tvdate = tvdate;
    }

    public TextView getTvintime() {
        return tvintime;
    }

    public void setTvintime(TextView tvintime) {
        this.tvintime = tvintime;
    }

    public TextView getTvstanddardintime() {
        return tvstanddardintime;
    }

    public void setTvstanddardintime(TextView tvstanddardintime) {
        this.tvstanddardintime = tvstanddardintime;
    }

    public TextView getTvouttime() {
        return tvouttime;
    }

    public void setTvouttime(TextView tvouttime) {
        this.tvouttime = tvouttime;
    }

    public TextView getTvstandardouttime() {
        return tvstandardouttime;
    }

    public void setTvstandardouttime(TextView tvstandardouttime) {
        this.tvstandardouttime = tvstandardouttime;
    }

    public TextView getTvapprovedintime() {
        return tvapprovedintime;
    }

    public void setTvapprovedintime(TextView tvapprovedintime) {
        this.tvapprovedintime = tvapprovedintime;
    }

    public TextView getTvapprovedouttime() {
        return tvapprovedouttime;
    }

    public void setTvapprovedouttime(TextView tvapprovedouttime) {
        this.tvapprovedouttime = tvapprovedouttime;
    }


}
