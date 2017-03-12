package com.none.mobilemaths

import android.app.Activity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast;

public class BinomeActivity extends AppCompatActivity {

    private EditText xargEt
    private EditText yargEt
    private TextView yOpt
    private Button confButton
    private Activity thisActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binome);

        xargEt = findViewById(R.id.xarget) as EditText
        yargEt = findViewById(R.id.yarget) as EditText
        yOpt = findViewById(R.id.yopt) as TextView
        confButton = findViewById(R.id.confbtn) as Button
        thisActivity = this

        String type = getIntent().getStringExtra("type")
        int power = getIntent().getIntExtra("power", 0)

        yOpt.setText(type=="VV" ? "y" : "")
        confButton.setOnClickListener {v ->
            if (!xargEt.getText().isNumber() || !yargEt.getText().isNumber())
            {
                Toast.makeText(thisActivity, R.string.unsuppform, Toast.LENGTH_SHORT).show()
            }
            else {
                double xarg = xargEt.getText().toDouble()
                double yarg = yargEt.getText().toDouble()
                TextView tv = new TextView(thisActivity)
                tv.setText(Html.fromHtml(MathPack.instance.binome(type, xarg, yarg, power)))
                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity)
                builder.setTitle(R.string.fin)
                builder.setView(tv)
                builder.show()
            }
        }

    }
}
