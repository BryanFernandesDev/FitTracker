package co.tiagoaguiar.fitnesstracker

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(

            MainItem(id = 1,
            drawbleId = R.drawable.ic_baseline_wine_bar_24,
            textStringId = R.string.Label_imc,
                color = Color.GREEN
                    )
        )
        mainItems.add(

                MainItem(id = 2,
                    drawbleId = R.drawable.ic_launcher_foreground,
                    textStringId = R.string.Label_imc,
                    color = Color.RED
                )
                )
        mainItems.add(

                MainItem(id = 3,
                    drawbleId = R.drawable.ic_baseline_visibility_24,
                    textStringId = R.string.label_tmb,
                    color = Color.YELLOW
                )
                )


        val adapter = MainAdapter(mainItems) {id ->

                when (id){
                    1 -> {
                        val intent = Intent(this@MainActivity, ImcActivity::class.java)
                        startActivity(intent)
                    }
                    2 ->{

                        val intent = Intent(this@MainActivity, TmbActivity::class.java)
                        startActivity(intent)
                    }
                    3 ->{
                        //abrir o item 3
                    }
                }
                Log.i("teste","clicou no id: $id")            }

            //implementando via objeto anonimo

//
//        val adapter = MainAdapter(mainItems, object : OnItemClickListener{
//            //implementando via objeto anonimo
//            override fun onClick(id: Int) {
//                when (id){
//                    1 -> {
//                        val intent = Intent(this@MainActivity, ImcActivity::class.java)
//                        startActivity(intent)
//                    }
//                    2 ->{
//
//                        //abrir outra activity
//                    }
//                    3 ->{
//                        //abrir o item 3
//                    }
//                }
//                Log.i("teste","clicou no id: $id")            }
//
//        })



        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)




    }


    private inner class MainAdapter (
        private val mainItems: List<MainItem>,
        //private val onItemClickListener:OnItemClickListener
    private val onItemClickListener: (Int) -> Unit
        ): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

        //1 - Qual é o xml que será inserido na recyclerview (item)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
           val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)

        }

        //2 - Metodo disparado toda vez que houver uma rolagem na terra e for necessario trocar
        //o conteudo da celula
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

            val  itemCurrent = mainItems[position]
            holder.bind(itemCurrent)

        }

        //3 - Informar quantas celulas  esta listagem terá
        override fun getItemCount(): Int {
            return mainItems.size
        }

        //classe da celula do xml
        private inner class MainViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView){
            fun bind(item: MainItem){
                val img: ImageView = itemView.findViewById(R.id.item_img_icon)
                val name: TextView = itemView.findViewById(R.id.item_txt_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container_imc)

                img.setImageResource(item.drawbleId)
                name.setText(item.textStringId)
                container.setBackgroundColor(item.color)

                container.setOnClickListener{
                    //aqui com invoke funcção, com onClick é uma interface
                    onItemClickListener.invoke(item.id)
                }
            }
        }
    }


// Usando implementação de interace via activity
//    override fun onClick(id: Int) {
//
//            when (id){
//                1 -> {
//                    val intent = Intent(this, ImcActivity::class.java)
//                    startActivity(intent)
//                }
//                2 ->{
//
//                    //abrir outra activity
//                }
//                3 ->{
//                    //abrir o item 3
//                }
//            }
//        Log.i("teste","clicou no id: $id")

    }
