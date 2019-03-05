package com.example.proyecto1;

import android.os.Bundle;

import com.example.proyecto1.fragments.NotesFragment;
import com.example.proyecto1.utilities.MainToolbar;

public class MainActivity extends MainToolbar implements NotesFragment.listenerDelFragment {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load main activity with fragments
        setContentView(R.layout.main_activity);
        // load top toolbar
        loadToolbar();
    }

    public void selectNote(String elemento){

    }
}
