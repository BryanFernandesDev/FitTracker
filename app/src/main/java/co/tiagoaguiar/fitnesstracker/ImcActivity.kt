package co.tiagoaguiar.fitnesstracker

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import co.tiagoaguiar.fitnesstracker.model.Calc

class ImcActivity : AppCompatActivity() {


    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)


        editWeight = findViewById(R.id.edit_imc_weight)
        editHeight = findViewById(R.id.edit_imc_height)

        val btncSend: Button = findViewById(R.id.btnc_imc_send)

        btncSend.setOnClickListener{
        if(!validate()){
            Toast.makeText(this, R.string.form_error, Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

            val weight = editWeight.text.toString().toInt()
            val height = editHeight.text.toString().toInt()

            var result = calculate(weight, height)
            Log.d("Teste", "resultado: $result")

            val imcResponseId = imcResponse(result)

            val dialog = AlertDialog.Builder(this)

            val title = getString(R.string.imc_response, result)

            dialog.setTitle(title)
            .setMessage(imcResponseId)
            .setPositiveButton(android.R.string.ok) { dialog,which ->
            }
                .setNegativeButton(R.string.save){ dialog, which ->

                    Thread{
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "imc", res = result))

                        runOnUiThread {

                            val intent = Intent(this@ImcActivity,ListCalcActivity::class.java)
                            intent.putExtra("type","imc")
                            startActivity(intent)

                        }
                    }.start()

                }
                .create()
                .show()

            val service = getSystemService(Context.INPUT_METHOD_SERVICE)  as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)


        }
    }
    @StringRes
    private fun imcResponse(imc: Double): Int{
       return when {
            imc < 15.0 ->  R.string.imc_severely_low_weight
            imc < 16.0 ->  R.string.imc_very_low_weight
            imc < 18.5 ->  R.string.imc_low_weight
            imc < 25.0 ->  R.string.normal
            imc < 30.0 ->  R.string.imc_so_high_weight
            imc < 25.0 ->  R.string.imc_so_high_weight
            imc < 40.0 ->  R.string.imc_severly_high_weight
            else ->  R.string.imc_extreme_weight
        }
    }

    private fun validate(): Boolean{
        return (editWeight.text.toString().isNotEmpty()
            && editHeight.text.toString().isNotEmpty()
            && !editWeight.text.toString().startsWith("0")
            && !editHeight.text.toString().startsWith("0"))

    }

    private fun calculate(weight: Int, height: Int): Double{
        return weight/((height/100.0) * (height/100))

    }
}