package br.com.jhonicosta.xapp_messenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.fragments.ContatosFragment;
import br.com.jhonicosta.xapp_messenger.fragments.ConversasFragment;

public class MainActivity extends AppCompatActivity {

    private SmartTabLayout smartTab;
    private ViewPager viewPager;
    private MaterialSearchView searchView;

    private UsuarioController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new UsuarioController(this);

        smartTab = findViewById(R.id.smartTab);
        viewPager = findViewById(R.id.viewPager);


        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add(R.string.fragment_name_contato, ContatosFragment.class)
                        .add(R.string.fragment_name_conversas, ConversasFragment.class)
                        .create()
        );
        viewPager.setAdapter(adapter);
        smartTab.setViewPager(viewPager);

        searchView = findViewById(R.id.search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment conversasFragment = (ConversasFragment) adapter.getPage(1);
                conversasFragment.recarregaListaDeConversas();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                switch (viewPager.getCurrentItem()){
                    case 0:
                        ContatosFragment contatosFragment = (ContatosFragment) adapter.getPage(0);
                        if (newText != null & !newText.isEmpty()) {
                            contatosFragment.pesquisarContatos(newText.toLowerCase());
                        } else {
                            contatosFragment.recarregaContatos();
                        }
                        break;
                    case 1:
                        ConversasFragment conversasFragment = (ConversasFragment) adapter.getPage(1);
                        if (newText != null & !newText.isEmpty()) {
                            conversasFragment.pesquisarConversas(newText.toLowerCase());
                        } else {
                            conversasFragment.recarregaListaDeConversas();
                        }
                        break;
                }



                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSair:
                controller.deslogar();
                break;
            case R.id.menuConfiguracoes:
                startActivity(new Intent(this, ConfiguracoesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
