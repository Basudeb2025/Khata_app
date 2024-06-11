package com.example.apna_khata

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter(private val activity: Context, private val array:MutableList<Datas>):RecyclerView.Adapter<adapter.viewholder>(){

    private lateinit var myListener:onItemclicklitener
    interface onItemclicklitener{
        fun onItemclick(position: Int)
    }
    fun setItemclickListener(listener:onItemclicklitener){
        myListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter.viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom,parent,false)
        return viewholder(view,myListener)
    }
    override fun onBindViewHolder(holder: viewholder, position: Int) {
         val current = array[position]
         var m:String = ""
         var date:String = ""
         var year:String = ""
         var time:String =""
         time += current.Time[0]
         time += current.Time[1]
         time += current.Time[2]
         time += current.Time[3]
         time += current.Time[4]
         m += current.date[3]
         m += current.date[4]
         date += current.date[0]
         date += current.date[1]
         year += current.date[6]
         year += current.date[7]
         year += current.date[8]
         year += current.date[9]
         val month:String = numberTomonth(m.toInt())
         holder.hdate.text = "${date} ${month} ${year}, ${time}"
         if(current.cashIn == "Cash In"){
             holder.cashinin.text = current.cashOut
             holder.cashoutin.text = " "
         }else {
             holder.cashinin.text = " "
             holder.cashoutin.text = current.cashOut
         }
         holder.reason.text = current.Reason.toString()
    }

    override fun getItemCount(): Int {
        return array.size
    }
    class viewholder(itemview:View,listener: onItemclicklitener):RecyclerView.ViewHolder(itemview) {
        val hdate = itemview.findViewById<TextView>(R.id.Dateid)
        val cashinin = itemview.findViewById<TextView>(R.id.cashInId)
        val cashoutin = itemview.findViewById<TextView>(R.id.CashOutId)
        val reason = itemview.findViewById<TextView>(R.id.Reason)
        val layout = itemview.findViewById<LinearLayout>(R.id.lay)
        init {
            itemview.setOnClickListener {
                listener.onItemclick(adapterPosition)
            }
        }
    }
    private fun numberTomonth(month:Int):String{
        if(month == 1) return "January"
        else if(month == 2) return "Frebuary"
        else if(month == 3) return "March"
        else if(month == 4) return "April"
        else if(month == 5) return "May"
        else if(month == 6) return "June"
        else if(month == 7) return "July"
        else if(month == 8) return "August"
        else if(month == 9) return "September"
        else if(month == 10) return "Actobor"
        else if(month == 11) return "November"
        return "December"
    }
}


