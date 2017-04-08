package com.none.mobilemaths

import android.app.Activity
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import groovy.transform.TupleConstructor

public class MatrixActActivity extends AppCompatActivity {

    private boolean WFM
    private int dim
    private Activity thisActivity
    private String method
    def widgets
    def freeMembers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)

        WFM = getIntent().getBooleanExtra("WFM", false)
        thisActivity = this
        dim = getIntent().getIntExtra("dim", 2)
        widgets = []
        freeMembers = [0]*dim
        method = getIntent().getStringExtra("method")
        setContentView(generateGrid())
    }

    ScrollView generateGrid()
    {
        ScrollView mainView = new ScrollView(thisActivity)
        LinearLayout layout = new LinearLayout(thisActivity)
        layout.setBackgroundResource(R.drawable.backgr)
        layout.setOrientation(LinearLayout.VERTICAL)
        ScrollView scrollView = new ScrollView(thisActivity)
        TableLayout tableLayout = new TableLayout(thisActivity)
        //GridViewSetup
        if (WFM)
        {
            TableRow tableRow = new TableRow(thisActivity)
            for (i in 1..dim)
                {
                    TextView tv = new TextView(thisActivity)
                    tv.setTextColor(Color.WHITE)
                    tv.setGravity(Gravity.CENTER)
                    tv.setText("x$i")
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f))
                    tableRow.addView(tv)
                }
            TextView tv = new TextView(thisActivity)
            tv.setTextColor(Color.WHITE)
            tv.setGravity(Gravity.CENTER)
            tv.setText(R.string.freemembers)
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f))
            tableRow.addView(tv)
            tableLayout.addView(tableRow)
        }
        for (i in 1..dim)
        {
            TableRow tableRow = new TableRow(thisActivity)
            for (j in 1..(WFM ? dim+1 : dim))
            {
                EditText et = new EditText(thisActivity)
                et.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f))
                et.setHint("$i $j")
                et.setHintTextColor(Color.GRAY)
                et.setTextColor(Color.WHITE)
                et.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER)
                et.setGravity(Gravity.CENTER)
                tableRow.addView(et)
                if (j==dim+1) freeMembers[i-1] = et
                else widgets += et
            }
            tableLayout.addView(tableRow)
        }
        scrollView.addView(tableLayout)
        //end of GridViewSetup
        //Button setup
        Button okBtn = new Button(thisActivity)
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        okBtn.setLayoutParams(params)
        okBtn.setText(R.string.ok)
        okBtn.setGravity(Gravity.CENTER)
        okBtn.setOnClickListener() {
            if (widgets.every() {EditText txt -> txt.getText().toString().isNumber() } && (WFM ? (freeMembers.every(){EditText e -> e.getText().toString().isNumber()}) : true)) {
                def widgets2 = widgets.collect() { EditText et -> et.getText().toString().toDouble() }
                def widg2 = []
                0.step(widgets2.size(), dim) {
                    if (it == widgets2.size()) null else widg2 << widgets2[it..it + (dim - 1)]
                }
                widgets2 = widg2 //widgets2 - матрица значений, freeMembers - список свободных членов(если они есть), ссылки на виджеты
                if (method=="determinant")
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity)
                    builder.setTitle(R.string.fin)
                    builder.setMessage("Определитель данной матрицы равен ${MathPack.instance.determinant(widgets2)}")
                    builder.show()
                }
                if (method=="trace")
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity)
                    builder.setTitle(R.string.fin)
                    builder.setMessage("След данной матрицы равен ${MathPack.instance.trace(widgets2)}")
                    builder.show()
                }
                else if(method=="kramer")
                {
                    def freeMembersc = freeMembers.collect() {EditText item -> item.getText().toString().toDouble()}
                    def delta = MathPack.instance.determinant(widgets2)
                    def deltas = []
                    for (i in 0..<dim)
                    {
                        def temp = widgets2.collect(); temp = MathPack.instance.transpose(temp);
                        temp[i] = freeMembersc; temp = MathPack.instance.transpose(temp);
                        deltas << (MathPack.instance.determinant(temp))
                    }
                    String ans = ""
                    deltas = deltas.collect() {value -> value / delta}
                    deltas.eachWithIndex{ val, ind -> ans+="x${ind+1} = $val \n"}
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity)
                    builder.setTitle(R.string.fin)
                    builder.setMessage(ans)
                    builder.show()
                }
                else if(method=="revmat")
                {
                    double determ = MathPack.instance.determinant(widgets2)
                    if (determ==0.toDouble())
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity)
                        builder.setTitle(R.string.fin)
                        builder.setMessage(R.string.norevmat)
                        builder.show()
                    }
                    else
                    {
                        def factor = (determ - determ.intValue() == 0.toDouble()) ? new Rational(1, determ.intValue()) : 1/determ
                        def newMatrix = []
                        for (i in 0..<dim)
                        {
                            newMatrix << []
                            for (j  in 0..<dim)
                            {
                                if ((i+j)%2==0) newMatrix[i]+=MathPack.instance.determinant(MathPack.instance.minify(i,j,widgets2))
                                else newMatrix[i]+=-(MathPack.instance.determinant(MathPack.instance.minify(i,j,widgets2)))
                            }
                        }
                        newMatrix = MathPack.instance.transpose(newMatrix)
                        newMatrix = newMatrix.collect() {array -> array.collect() {item -> factor.multiply(item)}}
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity)
                        builder.setTitle(R.string.fin)
                        String ans = ""
                        newMatrix.each() {elem -> ans+=elem.toString()+"\n"}
                        builder.setMessage(ans)
                        builder.show()
                    }
                }
                else if (method=="silvester")
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity)
                    builder.setTitle(R.string.fin)
                    builder.setMessage(getResources().getString(R.string.mdefined)+MathPack.instance.defineBySilvester(widgets2))
                    builder.show()
                }
            }
            else
            {
                Toast.makeText(thisActivity,R.string.errinmat,Toast.LENGTH_SHORT).show()
            }
        }
        //end of button setup
        layout.addView(scrollView)
        layout.addView(okBtn)
        mainView.addView(layout)
        return mainView
    }
}

@TupleConstructor
class Rational
{
    int fnum, snum

    def normalize()
    {
        def gc = MathPack.instance.gcd(this.fnum.abs(),this.snum.abs())
        this.fnum/=gc; this.snum/=gc;
    }


    String toString() {
        if (fnum<0 ^ snum<0) return "-(${fnum.abs()}/${snum.abs()})"
        else if(fnum<0 && snum<0) return "${-fnum}/${-snum}"
        else return "${fnum}/${snum}"
    }

    def multiply(double val)
    {
        if (val!=val.intValue().toDouble()) return val*this.fnum/this.snum
        Rational r = new Rational((this.fnum*val).intValue(),this.snum)
        r.normalize()
        if ((r.fnum/r.snum)==(r.fnum/r.snum).intValue().toDouble()) return r.fnum/r.snum
        else if (((r.fnum/r.snum) - (r.fnum/r.snum).intValue()).toString().size()<7) return (r.fnum/r.snum)
        else return r
    }
}


