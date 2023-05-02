package com.moling.micabrowser.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.moling.micabrowser.databinding.ActivityNoteBinding;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class NoteActivity extends Activity {
    private ActivityNoteBinding binding;
    private TextView mTextNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTextNote = binding.textNote;

        try (InputStream input = getAssets().open("release-notes.txt")) {
            InputStreamReader inputReader = new InputStreamReader(input, StandardCharsets.UTF_8);
            int n;
            StringBuilder sb = new StringBuilder();
            while ((n = inputReader.read()) != -1) {
                sb.append((char) n);
            }
            mTextNote.setText(sb.toString());
        } catch (IOException e) {}
    }
}
