package com.smartdays.smartlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class NovaCompraFragment extends Fragment {


    public NovaCompraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nova_compra, container, false);

        if (rootView != null) {
            final ImageView imgCompraInt = (ImageView) rootView.findViewById(R.id.imgCompraInt);
            imgCompraInt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("Tipo_compra", "Inteligente");

                    Bundle args = new Bundle();
                    args.putString("tipoCompra", "A");

                    MainActivity activity = (MainActivity) getActivity();
                    activity.callSelectionarListaFragment(args);
                }
            });

            final ImageView imgMontarInt = (ImageView) rootView.findViewById(R.id.imgMontarInt);
            imgMontarInt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("Tipo_compra", "Montar Inteligente");
                    Bundle args = new Bundle();
                    args.putString("tipoCompra", "B");

                    MainActivity activity = (MainActivity) getActivity();
                    activity.callSelectionarListaFragment(args);
                }
            });

            final ImageView imgCompraSeq = (ImageView) rootView.findViewById(R.id.imgCompraSeq);
            imgCompraSeq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("Tipo_compra", "Sequencial");

                    Bundle args = new Bundle();
                    args.putString("tipoCompra", "C");

                    MainActivity activity = (MainActivity) getActivity();
                    activity.callSelectionarListaFragment(args);
                }
            });
        }

        return rootView;
    }

}
