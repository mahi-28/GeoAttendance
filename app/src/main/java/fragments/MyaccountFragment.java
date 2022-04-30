package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.plutecoder.geoattendance.MainActivity;
import com.plutecoder.geoattendance.R;

import utils.PreferenceHelper;
import utils.Typefaces;

public class MyaccountFragment extends Fragment {
    View view;
    Button btnlogout;
TextView txtname,txtmycodecontent;
RelativeLayout invitefriendrelative;
Button btnshare;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_activity, container, false);
        btnlogout = view.findViewById(R.id.btnlogout);
        txtname=view.findViewById(R.id.txtname);
        txtmycodecontent=view.findViewById(R.id.txtmycodecontent);
        invitefriendrelative=view.findViewById(R.id.invitefriend);
        btnshare=view.findViewById(R.id.sharecode);
        try {
            if (Typefaces.get(getActivity(), "HelveticaNeue Light") != null) {
                btnlogout.setTypeface(Typefaces.get(getActivity(), "HelveticaNeue Light"));
                txtname.setTypeface(Typefaces.get(getActivity(), "HelveticaNeue Light"));
//                EdtEmail.setTypeface(Typefaces.get(getActivity(), "HelveticaNeue Light"));
//                EdtPass.setTypeface(Typefaces.get(getActivity(), "HelveticaNeue Light"));
//                btnForgotPass.setTypeface(Typefaces.get(getActivity(), "HelveticaNeueLt"));
//                txtsignup.setTypeface(Typefaces.get(getActivity(), "HelveticaNeue Light"));
                txtname.setText( PreferenceHelper.getusername(MainActivity.activity));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PreferenceHelper.storeLoginstatus(getActivity(), false);
                    FirebaseAuth.getInstance().signOut();
                    getActivity().finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String shareling = PreferenceHelper.getusercode(getActivity());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, shareling);
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Contact Things");
                    startActivity(Intent.createChooser(intent, "Share"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
       // chkdata();
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        System.out.println("fragment on resume");
       // chkdata();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);


        MenuItem edit = menu.findItem(R.id.actionedit);
        edit.setVisible(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionedit:
//                Intent i =new Intent(getActivity(),EditProfileActivity.class);
//                startActivity(i);
                break;


            case android.R.id.home:
                //  ((ProviderProfile) getActivity()).Backclick();

                return true;
            default:
                break;
        }
        return false;
    }

}
