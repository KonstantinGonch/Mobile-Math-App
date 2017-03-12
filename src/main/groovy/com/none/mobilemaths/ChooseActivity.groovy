package com.none.mobilemaths

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

public class ChooseActivity extends AppCompatActivity {

    private Button determBtn, kramerBtn, gcdBtn, ok1Btn, ok2Btn, ok3Btn, revmatBtn, ok4Btn, binomeBtn, ok5Btn, okTraceBtn, traceBtn
    private EditText matvol1Et, matvol2Et, matvol3Et, fnumEt, snumEt, binomePowerEt, matvolTraceEt
    private TextView matvol1Tv, matvol2Tv, matvol3Tv, binomeTypeTv, binomePowerTv, matvolTraceTv
    private Spinner binomeTypesSpinner
    private Activity thisActivity = this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        determBtn = findViewById(R.id.determbtn) as Button
        kramerBtn = findViewById(R.id.kramerbtn) as Button
        revmatBtn = findViewById(R.id.revmatbtn) as Button
        gcdBtn = findViewById (R.id.gcdBtn) as Button
        binomeBtn = findViewById(R.id.binomebtn) as Button
        traceBtn = findViewById(R.id.tracebtn) as Button
        ok1Btn = findViewById(R.id.ok1btn) as Button
        ok2Btn = findViewById(R.id.ok2btn) as Button
        ok3Btn = findViewById(R.id.ok3btn) as Button
        ok4Btn = findViewById(R.id.ok4btn) as Button
        ok5Btn = findViewById(R.id.ok5btn) as Button
        okTraceBtn = findViewById(R.id.oktrbtn) as Button
        matvol1Tv = findViewById(R.id.matvol1tv) as TextView
        matvol2Tv = findViewById(R.id.matvol2tv) as TextView
        matvol3Tv = findViewById(R.id.matvol3tv) as TextView
        binomeTypeTv = findViewById(R.id.binometypetv) as TextView
        binomePowerTv = findViewById(R.id.binomepowertv) as TextView
        matvolTraceTv = findViewById(R.id.matvoltrtv) as TextView
        matvol2Et = findViewById(R.id.matvol2et) as EditText
        matvol1Et = findViewById(R.id.matvol1et) as EditText
        matvol3Et = findViewById(R.id.matvol3et) as EditText
        fnumEt = findViewById(R.id.fnumEt) as EditText
        snumEt = findViewById(R.id.snumEt) as EditText
        binomePowerEt = findViewById(R.id.binomepoweret) as EditText
        matvolTraceEt = findViewById(R.id.matvoltret) as EditText
        binomeTypesSpinner = findViewById(R.id.binometypespinner) as Spinner
        def krammerCol = [ok2Btn, matvol2Et, matvol2Tv]
        def determinantCol = [ok1Btn, matvol1Et, matvol1Tv]
        def revmatCol = [ok3Btn, matvol3Et, matvol3Tv]
        def nodCol = [ok4Btn, fnumEt, snumEt]
        def binomeCol = [binomeTypeTv, binomePowerTv, binomeTypesSpinner, binomePowerEt, ok5Btn]
        def traceCol = [okTraceBtn, matvolTraceTv, matvolTraceEt]
        def allIntWidgets = krammerCol+determinantCol+revmatCol+nodCol+binomeCol+traceCol

        allIntWidgets.each {View v -> if (v!=null) v.setVisibility(View.GONE)}

        determBtn.setOnClickListener() {v -> allIntWidgets.each() {if (it in determinantCol) it.setVisibility(View.VISIBLE) else it.setVisibility(View.GONE)}}
        kramerBtn.setOnClickListener()  {v -> allIntWidgets.each() {if (it in krammerCol) it.setVisibility(View.VISIBLE) else it.setVisibility(View.GONE)}}
        traceBtn.setOnClickListener() {v -> allIntWidgets.each() {if (it in traceCol) it.setVisibility(View.VISIBLE) else it.setVisibility(View.GONE)}}
        revmatBtn.setOnClickListener()  {v -> allIntWidgets.each() {if (it in revmatCol) it.setVisibility(View.VISIBLE) else it.setVisibility(View.GONE)}}
        gcdBtn.setOnClickListener()  {v -> allIntWidgets.each() {if (it in nodCol) it.setVisibility(View.VISIBLE) else it.setVisibility(View.GONE)}}
        binomeBtn.setOnClickListener() {v -> allIntWidgets.each() {if (it in binomeCol) it.setVisibility(View.VISIBLE) else it.setVisibility(View.GONE)}}
        ok1Btn.setOnClickListener() {v -> if(!matvol1Et.getText().toString().isInteger()) Toast.makeText(thisActivity, R.string.unsuppform,Toast.LENGTH_SHORT).show() else startMatrixActivity(matvol1Et.getText().toString().toInteger(), "determinant")}
        ok2Btn.setOnClickListener() {v -> if(!matvol2Et.getText().toString().isInteger()) Toast.makeText(thisActivity, R.string.unsuppform,Toast.LENGTH_SHORT).show() else startMatrixActivity(matvol2Et.getText().toString().toInteger(), "kramer", true)}
        ok3Btn.setOnClickListener() {v -> if(!matvol3Et.getText().toString().isInteger()) Toast.makeText(thisActivity, R.string.unsuppform,Toast.LENGTH_SHORT).show() else startMatrixActivity(matvol3Et.getText().toString().toInteger(), "revmat")}
        ok4Btn.setOnClickListener() {v -> if(fnumEt.getText().toString().isInteger() && snumEt.getText().toString().isInteger()) revealGcd(MathPack.instance.gcd(fnumEt.getText().toString().toInteger(), snumEt.getText().toString().toInteger())) else Toast.makeText(thisActivity, R.string.unsuppform, Toast.LENGTH_SHORT).show()}
        okTraceBtn.setOnClickListener {v -> if (!matvolTraceEt.getText().isInteger()) Toast.makeText(thisActivity, R.string.unsuppform, Toast.LENGTH_SHORT).show() else startMatrixActivity(matvolTraceEt.getText().toInteger(), "trace")}
        ok5Btn.setOnClickListener() { v ->
            if (!binomePowerEt.getText().isInteger()) Toast.makeText(thisActivity, R.string.unsuppform, Toast.LENGTH_SHORT).show() else {
                Intent intent = new Intent(thisActivity, BinomeActivity.class);
                intent.putExtra("type", (binomeTypesSpinner.getSelectedItem().toString().equals("Две переменных") ? "VV" : "VN"))
                if (binomePowerEt.getText().toInteger() < 2 || !binomePowerEt.getText().isInteger())
                    Toast.makeText(thisActivity, R.string.powererr, Toast.LENGTH_SHORT).show()
                else {
                    intent.putExtra("power", binomePowerEt.getText().toInteger())
                    startActivity(intent)
                }
            }
        }

    }

    def revealGcd(numa)
    {
        AlertDialog.Builder buil=  new AlertDialog.Builder(thisActivity)
        buil.setTitle(R.string.nod)
        buil.setMessage("НОД чисел ${fnumEt.getText().toString().toInteger()} и ${snumEt.getText().toString().toInteger()} = $numa")
        buil.show()
    }

    def startMatrixActivity(int dim, String method, boolean WFM = false)
    {
        if (dim<2) Toast.makeText(thisActivity,R.string.errormatr, Toast.LENGTH_SHORT).show()
        else {
            Intent intent = new Intent(thisActivity, MatrixActActivity.class)
            if (WFM) intent.putExtra("WFM", WFM)
            intent.putExtra("dim", dim)
            intent.putExtra("method", method)
            startActivity(intent)
        }
    }
}
