package com.hermes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hermes.storage.ContactPOJO;
import com.hermes.storage.LocalStorage;

public class AddNewContact extends AppCompatActivity {
    Button saveButton;
    ImageView cancelButton;
    EditText name, number, message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        name = findViewById(R.id.edit1);
        number = findViewById(R.id.edit2);
        message = findViewById(R.id.edit3);
        saveButton = findViewById(R.id.saveContact);
        cancelButton = findViewById(R.id.bt_cancel);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                LocalStorage.saveContact(view, new ContactPOJO(name.getText().toString(), number.getText().toString(), message.getText().toString()));
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
}