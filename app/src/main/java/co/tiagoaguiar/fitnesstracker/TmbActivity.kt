package co.tiagoaguiar.fitnesstracker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleRegistry
import co.tiagoaguiar.fitnesstracker.model.Calc

class TmbActivity : AppCompatActivity() {

private lateinit var lifestyle: AutoCompleteTextView

    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText
    private lateinit var editAge: EditText

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmb)

        editWeight = findViewById(R.id.edit_tbm_weight)
        editHeight = findViewById(R.id.edit_tbm_height)
        editAge = findViewById(R.id.edit_tbm_age)

        val btncSend: Button = findViewById(R.id.btnc_tbm_send)

        btncSend.setOnClickListener{
            if(!validate()){
                Toast.makeText(this, R.string.form_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = editWeight.text.toString().toInt()
            val height = editHeight.text.toString().toInt()
            val age = editAge.text.toString().toInt()

            val result =  calculateTmb(weight,height,age)
            val response = tmbRequest(result)

            val dialog = AlertDialog.Builder(this)



            AlertDialog.Builder(this)
                .setMessage(getString(R.string.imc_response, response))
                .setPositiveButton(android.R.string.ok){ dialog, which ->

                }
                .setNegativeButton(R.string.save){ dialog, which ->

                    Thread{
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(Calc(type = "tmb", res = response))

                        runOnUiThread {

                            openListActivity()

                        }
                    }.start()

                }
                .create()
                .show()

            val service = getSystemService(Context.INPUT_METHOD_SERVICE)  as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)


        }




        lifestyle = findViewById(R.id.auto_lifestyle)
        val items = resources.getStringArray(R.array.tmb_lifestyle)
        lifestyle.setText(items.first())
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        lifestyle.setAdapter(adapter)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search){
            finish()
            openListActivity()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun openListActivity(){
        val intent = Intent(this,ListCalcActivity::class.java)
        intent.putExtra("type","tmb")
        startActivity(intent)
    }

    private fun calculateTmb(weight: Int, height: Int, age: Int): Double{
        return 66 + (13.8 * weight) + (5 * height) - (6.8 * age)
    }

    private fun tmbRequest(tmb: Double): Double{
        val items = resources.getStringArray(R.array.tmb_lifestyle)

        return when {
            lifestyle.text.toString() == items[0] -> tmb * 1.2
            lifestyle.text.toString() == items[1] -> tmb * 1.375
            lifestyle.text.toString() == items[2] -> tmb * 1.55
            lifestyle.text.toString() == items[3] -> tmb * 1.725
            lifestyle.text.toString() == items[4] -> tmb * 1.9
            else -> 0.0
        }


    }


    private fun validate(): Boolean{
        return (editWeight.text.toString().isNotEmpty()
                && editHeight.text.toString().isNotEmpty()
                && !editWeight.text.toString().startsWith("0")
                && !editHeight.text.toString().startsWith("0"))
                && !editAge.text.toString().isNotEmpty()
                && !editAge.text.toString().startsWith("0")

    }

}