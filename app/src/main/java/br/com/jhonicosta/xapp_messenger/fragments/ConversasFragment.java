package br.com.jhonicosta.xapp_messenger.fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.adapter.ConversasAdapter;
import br.com.jhonicosta.xapp_messenger.config.FirebaseConfig;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.model.Conversa;

public class ConversasFragment extends Fragment {

    private RecyclerView listaConversas;
    private ConversasAdapter adapter;
    private List<Conversa> list = new ArrayList<>();

    private DatabaseReference reference;
    private DatabaseReference conversasRef;
    private ChildEventListener eventListener;

    public ConversasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        listaConversas = view.findViewById(R.id.listaConversas);

        reference = FirebaseConfig.getFirebaseDatabase();

        adapter = new ConversasAdapter(list, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listaConversas.setLayoutManager(layoutManager);
        listaConversas.setHasFixedSize(true);
        listaConversas.setAdapter(adapter);

        String idUsuario = new UsuarioController(getActivity()).getIdUser();

        conversasRef = reference.child("conversas").child(idUsuario);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(eventListener);
    }

    public void recuperarConversas() {
        String idUsuario = new UsuarioController(getActivity()).getIdUser();

        DatabaseReference conversasRef = reference.child("conversas").child(idUsuario);

        eventListener = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Conversa conversa = dataSnapshot.getValue(Conversa.class);
                list.add(conversa);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
