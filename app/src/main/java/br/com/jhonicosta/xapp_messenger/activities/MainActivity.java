package br.com.jhonicosta.xapp_messenger.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import br.com.jhonicosta.xapp_messenger.R;
import br.com.jhonicosta.xapp_messenger.controller.UsuarioController;
import br.com.jhonicosta.xapp_messenger.fragments.ContatoFragment;
import br.com.jhonicosta.xapp_messenger.fragments.ConversasFragment;

public class MainActivity extends AppCompatActivity {

    private SmartTabLayout smartTab;
    private ViewPager viewPager;

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

        smartTabConfig();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSair:
                controller.deslogar();
                break;
            case R.id.menuConfiguracoes:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void smartTabConfig() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add(R.string.fragment_name_contato, ContatoFragment.class)
                        .add(R.string.fragment_name_conversas, ConversasFragment.class)
                        .create()
        );
        viewPager.setAdapter(adapter);
        smartTab.setViewPager(viewPager);
    }
}
