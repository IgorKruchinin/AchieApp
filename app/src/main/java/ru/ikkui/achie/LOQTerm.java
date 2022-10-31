package ru.ikkui.achie;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.ikkui.achie.USSM.USSM.LOQ.LOQ;
import ru.ikkui.achie.USSM.USSM.LOQ.LOQNoProfileException;
import ru.ikkui.achie.databinding.ActivityLoqtermBinding;

public class LOQTerm extends AppCompatActivity {

    private LOQ loq;

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoqtermBinding binding;
    private TextView LOQOutput;
    private EditText cmdFld;
    private Button enterCmdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoqtermBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        loq = new LOQ("Achie", this);
        LOQOutput = findViewById(R.id.TermOutput);
        cmdFld = findViewById(R.id.TermInput);
        enterCmdBtn = findViewById(R.id.enterCmdBtn);
        enterCmdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String command = cmdFld.getText().toString();
                LOQOutput.setText(LOQOutput.getText() + command + "\n");
                cmdFld.setText("");
                try {
                    try {
                        loq.parseQuery(command);
                    } catch (LOQNoProfileException e) {
                        LOQOutput.setText(LOQOutput.getText() + e.getMessage());
                    }
                    if (loq.changed()) {
                        switch (loq.getLastFormat()) {
                            case 0:
                                LOQOutput.setText(LOQOutput.getText() + String.valueOf(loq.popInt()));
                                break;
                            case 1:
                                LOQOutput.setText(LOQOutput.getText() + loq.popStr());
                                break;
                            case 3:
                                LOQOutput.setText(LOQOutput.getText() + (loq.getLockStatus() ? "locked" : "unlocked"));
                        }
                    }
                } catch (Exception ex) {
                    LOQOutput.setText(LOQOutput.getText() + ex.getMessage());
                }
                LOQOutput.setText(LOQOutput.getText() + "\n" + loq.getProfName() + " > ");
            }
        });
        LOQOutput.setText(loq.getProfName() + " > ");

    }

}