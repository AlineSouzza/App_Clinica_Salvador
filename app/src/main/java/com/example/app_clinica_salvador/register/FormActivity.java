package com.example.app_clinica_salvador.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.app_clinica_salvador.MainActivity;
import com.example.app_clinica_salvador.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    private TextInputEditText inputName;
    private TextInputEditText inputAge;
    private TextInputEditText inputTemp;
    private TextInputEditText inputCough;
    private TextInputEditText inputHead;
    private TextInputEditText inputCountry;
    private Button buttonSend;


    private String name;
    private int age;
    private int temp;
    private int cough;
    private int head;
    private int country;

    private String situation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        inputName = (TextInputEditText) findViewById(R.id.input_name);
        inputAge = (TextInputEditText) findViewById(R.id.input_age);
        inputTemp = (TextInputEditText) findViewById(R.id.input_temp);
        inputCough = (TextInputEditText) findViewById(R.id.input_cough);
        inputHead = (TextInputEditText) findViewById(R.id.input_head);
        inputCountry = (TextInputEditText) findViewById(R.id.input_country);
        buttonSend = (Button) findViewById(R.id.button_send);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validar o preenchimento dos campos
                if (validade()) {
                    //verificar a situação do paciente
                    checkSituation();
                } else {
                    new AlertDialog.Builder(FormActivity.this)
                            .setTitle("Dados Obrigatórios")
                            .setMessage("Os campos: nome, idade e temperatura são obrigatórios!")
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                }

            }
        });
    }

    private boolean validade() {
        boolean pass = true;

        if (inputName.getText().toString().isEmpty()) pass = false;
        else name = inputName.getText().toString();

        if (inputAge.getText().toString().isEmpty()) pass = false;
        else age = Integer.parseInt(inputAge.getText().toString());

        if (inputTemp.getText().toString().isEmpty()) pass = false;
        else temp = Integer.parseInt(inputTemp.getText().toString());

        if (inputCough.getText().toString().isEmpty()) cough = 0;
        else cough = Integer.parseInt(inputCough.getText().toString());

        if (inputHead.getText().toString().isEmpty()) head = 0;
        else head = Integer.parseInt(inputHead.getText().toString());

        if (inputCountry.getText().toString().isEmpty()) country = 0;
        else country = Integer.parseInt(inputCountry.getText().toString());

        return pass;
    }

    private void checkSituation(){
        situation = "";

        if ((age > 60) || (age < 10)) {
            if ((cough > 5) || (head > 3) || (temp > 37)) {
                situation = "quarentena";
            }
        }

        if ((cough > 5) && (head > 5) && (temp > 37)) {
            if (country < 6)//corrigir, pois
                situation = "internado";
            else
                situation = "quarentena";
        }

        if(situation.equals("")){
            situation = "liberado";
        }

        saveUser();

    }

    private void saveUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> users = new HashMap<>();
        users.put("name", name);
        users.put("age", age);
        users.put("temp", temp);
        users.put("cough", cough);
        users.put("head", head);
        users.put("country", country);
        users.put("situation", situation);

        db.collection("users")
                .add(users)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        new AlertDialog.Builder(FormActivity.this)
                                .setTitle("Sucesso")
                                .setMessage("O paciente foi cadastrado com sucesso!\nA situação do paciente é:"+situation)
                                .setPositiveButton(android.R.string.yes, null)
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new AlertDialog.Builder(FormActivity.this)
                                .setTitle("Falha")
                                .setMessage("Falha ao cadastrar o paciente.")
                                .setPositiveButton(android.R.string.yes, null)
                                .show();
                    }
                });
    }

}