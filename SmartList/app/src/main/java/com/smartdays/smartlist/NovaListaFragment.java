package com.smartdays.smartlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class NovaListaFragment extends Fragment {

    public static final String TAG = "NovaListaFragment";
    public NovaListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nova_lista, container, false);

        if (rootView != null) {
            Button btnSaveList = (Button) rootView.findViewById(R.id.btnSaveList);
            btnSaveList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG, "Funcionou");
                }
            });
        }

        return rootView;
    }

    public void onBackPressed(){

    }

}
