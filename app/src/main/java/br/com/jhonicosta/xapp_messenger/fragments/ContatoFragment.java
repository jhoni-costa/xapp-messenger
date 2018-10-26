package br.com.jhonicosta.xapp_messenger.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.adapter.ContatosAdapter;
import br.com.jhonicosta.xapp_messenger.config.FirebaseConfig;
import br.com.jhonicosta.xapp_messenger.model.Usuario;

public class ContatoFragment extends Fragment {

    private RecyclerView listaContatos;
    private ContatosAdapter adapter;
    private List<Usuario> list = new ArrayList<>();
    private DatabaseReference reference;
    private ValueEventListener listener;

    public ContatoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contato, container, false);

        listaContatos = view.findViewById(R.id.listaContatos);
        reference = FirebaseConfig.getFirebaseDatabase().child("usuarios");

        adapter = new ContatosAdapter(list, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listaContatos.setLayoutManager(layoutManager);
        listaContatos.setHasFixedSize(true);
        listaContatos.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        findAll();
    }

    @Override
    public void onStop() {
        super.onStop();
        reference.removeEventListener(listener);
    }

    public void findAll() {
        listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    list.add(usuario);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
