package br.com.jhonicosta.xapp_messenger.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.jhonicosta.xapp_messenger.R;


public class ContatoFragment extends Fragment {

    public ContatoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contato, container, false);
    }
}
