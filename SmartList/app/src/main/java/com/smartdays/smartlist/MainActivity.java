package com.smartdays.smartlist;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SQLiteDatabase db = null;

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the initial fragment
        MainFragment fragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack("main");
        transaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        String DB_PATH = "/data/data/" + getBaseContext().getPackageName() + "/databases/smartlistapp.db";

        db = openOrCreateDatabase(DB_PATH, SQLiteDatabase.CREATE_IF_NECESSARY, null);

        DbHandle dbHandle = new DbHandle();
        dbHandle.criaDB(db);
        db.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            String fragmentName = getSupportFragmentManager().getBackStackEntryAt(count-1).getName();
            //Log.v("DEBUG", String.valueOf(count));
            //Log.v("DEBUG",fragmentName);


            Resources res = getResources();
            String[] firstLevelFragments = res.getStringArray(R.array.level1_fragments);
            if (fragmentName == "main") {
                //super.onBackPressed();
                finish();
            } else {
                if (Arrays.asList(firstLevelFragments).contains(fragmentName)){
                    getSupportFragmentManager().popBackStack("main", 0);
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String nivel = null;

        if (id == R.id.nav_newList) {
            nivel = "novaLista";
            NovaListaFragment fragment = new NovaListaFragment();

            fragmentsCallInterface(nivel, fragment);

        } else if (id == R.id.nav_savedLists) {
            nivel = "verLista";
            VerListaFragment fragment = new VerListaFragment();

            fragmentsCallInterface(nivel, fragment);

        } else if (id == R.id.nav_newShop) {
            nivel = "novaCompra";
            NovaCompraFragment fragment = new NovaCompraFragment();

            fragmentsCallInterface(nivel, fragment);

        } else if (id == R.id.nav_openShop) {
            nivel = "verCompras";
            VerComprasFragment fragment = new VerComprasFragment();

            fragmentsCallInterface(nivel, fragment);
        } else if (id == R.id.nav_stepbystep) {
            CargaSQLite cargaSQLite = new CargaSQLite();
            cargaSQLite.carga(getBaseContext());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fragmentsCallInterface (String nivel, Fragment fragment) {
        if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals(nivel)) {
            Log.v("BACKSTACK_CONTROL", "Same ID");
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(nivel);
            transaction.commit();
        }
    }

    public void callListaItemFragment(Bundle args){
        String nivel = "listaItem";
        ListaItemFragment fragment = new ListaItemFragment();
        fragment.setArguments(args);

        fragmentsCallInterface(nivel, fragment);
    }

    public void callAdicionarProdutosFragment(Bundle args) {
        String nivel = "adicionarProdutos";
        AdicionarProdutosFragment fragment = new AdicionarProdutosFragment();
        fragment.setArguments(args);

        fragmentsCallInterface(nivel, fragment);
    }

    public void callSelecionarListaFragment(Bundle args) {
        String nivel = "selecionarLista";
        SelecionarListaFragment fragment = new SelecionarListaFragment();
        fragment.setArguments(args);

        fragmentsCallInterface(nivel, fragment);
    }

    public void callListaCompraFragment(Bundle args) {
        String nivel = "listaCompra";
        ListaCompraFragment fragment = new ListaCompraFragment();
        fragment.setArguments(args);

        fragmentsCallInterface(nivel, fragment);
    }

    public void callItensCompraFragment(Bundle args) {
        String nivel = "itensCompra";
        ItensCompraFragment fragment = new ItensCompraFragment();
        fragment.setArguments(args);



        fragmentsCallInterface(nivel, fragment);

    }
}
