package com.example.apna_khata

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home_frage.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home_frage : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var databse : DatabaseReference
    private lateinit var array:MutableList<Datas>
    lateinit var firebaseFirestore: FirebaseFirestore
    var cashintotal:Int = 0
    var cashoutTotal:Int = 0
    var cashintotalm:Int = 0
    var cashoutTotalm:Int = 0
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val cashin = view.findViewById<CardView>(R.id.cashinCard)
        val cashout = view.findViewById<CardView>(R.id.cashoutCard)
        val showcashin = view.findViewById<TextView>(R.id.showcashin)
        val showcashout = view.findViewById<TextView>(R.id.showcashout)
        val remaining = view.findViewById<TextView>(R.id.remaining)
        val remainmm = view.findViewById<TextView>(R.id.remainmontly)
        val totalb = view.findViewById<TextView>(R.id.totalbl)
        val loadover = view.findViewById<RelativeLayout>(R.id.loadingOverlay)
        val prog = view.findViewById<ProgressBar>(R.id.loadingProgressBar)
        val lay = view.findViewById<LinearLayout>(R.id.layout)
        lay.visibility = View.GONE
        loadover.visibility = View.VISIBLE
        prog.visibility = View.VISIBLE
        cashin.setOnClickListener {
            val intent = Intent(requireContext(),Cash_in_out::class.java)
            intent.putExtra("valued","Cash In")
            startActivity(intent)
        }
        cashout.setOnClickListener {
            val intent = Intent(requireContext(),Cash_in_out::class.java)
            intent.putExtra("valued","Cash Out")
            startActivity(intent)
        }
        array = mutableListOf()
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid
        val userDataReference = database.getReference("User").child(userId.toString()).child("List")
        databse = FirebaseDatabase.getInstance().getReference("User")
        val datab = databse.child(userId.toString())
        userDataReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val dataId = childSnapshot.key.toString()
                    val cashin = childSnapshot.child("cashIn").getValue(String::class.java)
                    val cashout = childSnapshot.child("cashOut").getValue(String::class.java)
                    val reason = childSnapshot.child("reason").getValue(String::class.java)
                    val date = childSnapshot.child("date").getValue(String::class.java)
                    val time = childSnapshot.child("time").getValue(String::class.java)
                    val chec = childSnapshot.child("check").getValue(Boolean::class.java)
                    if (cashin != null && cashout != null && reason != null && date != null && time != null && dataId != null && chec != null) {
                        if (cashin == "Cash In") {
                            cashintotal += cashout?.toInt() ?: 0
                        } else cashoutTotal += cashout?.toInt() ?: 0
                        val datas = Datas(
                            cashin.toString(),
                            cashout.toString(),
                            reason.toString(),
                            date.toString(),
                            time.toString(),
                            dataId
                        )
                        if(chec.toString() == "true") {
                            if (cashin == "Cash In") {
                                cashintotalm += cashout?.toInt() ?: 0
                            }
                            else cashoutTotalm += cashout?.toInt()?:0
                        }
                        array.add(datas)
                    }
                }
                showcashin.text = "Total cash In:  " + cashintotal.toString()
                showcashout.text = "Total cash Out: " + cashoutTotal.toString()
                remaining.text = "Remaining : " + (cashintotal - cashoutTotal).toString()
                val recyclerView = view.findViewById<RecyclerView>(R.id.recycle)
                //View for proggres
                //Adapter
                val adapter = adapter(requireContext(), array)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                //Click the adapter
                adapter.setItemclickListener(object :adapter.onItemclicklitener{
                    override fun onItemclick(position: Int) {
                        val intent = Intent(requireContext(),view_list::class.java)
                        val current = array[position]
                        if(current.cashIn == "Cash In"){
                            intent.putExtra("Cashinorcashout","Cash In")
                            intent.putExtra("CashMoney",current.cashOut)
                        }else {
                            intent.putExtra("Cashinorcashout","Cash Out")
                            intent.putExtra("CashMoney",current.cashOut)
                        }
                        intent.putExtra("ReasonFor",current.Reason)
                        intent.putExtra("LinkofItem",current.iD)
                        startActivity(intent)
                    }
                })
                //Write the data
                val data = database.getReference("User").child(userId.toString()).child("Total")
                val d = mapOf("cashintotal" to cashintotal, "cashouttotal" to cashoutTotal)
                data.setValue(d).addOnSuccessListener {}.addOnFailureListener {}
                //Read it

                firebaseFirestore.collection("Users").document(userId.toString())
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val totalMoney = documentSnapshot.getString("Totalmoney").toString()
                            val monthlyMoney = documentSnapshot.getString("monthmoney").toString()
                            val check = documentSnapshot.getBoolean("check")
                            val mm = monthlyMoney.toInt()
                            val tm = totalMoney.toInt()
                            if (monthlyMoney != null) {
                                remainmm.text = "Monthly balance :₹"+(mm+cashintotalm - cashoutTotalm).toString()
                            }
                            else remainmm.text = "Monthly balance :₹"+(cashintotalm - cashoutTotalm).toString()
                            if(totalMoney != null){
                                totalb.text = "Total balance :₹"+(tm+cashintotal - cashoutTotal).toString()
                            }
                            else totalb.text = "Total balance :₹"+(cashintotal - cashoutTotal).toString()
                        } else {
                            remainmm.text = "Monthly balance :₹"+(cashintotalm - cashoutTotalm).toString()
                            totalb.text = "Total balance :₹"+(cashintotal - cashoutTotal).toString()
                            Log.d(ContentValues.TAG, "No details found for user $userId")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error getting user details: ", exception)
                    }
                loadover.visibility = View.GONE
                prog.visibility = View.GONE
                lay.visibility = View.VISIBLE
                //Total money
               // val datat = database.getReference("User").child(userId.toString())

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Toast.makeText(requireContext(), "Failed to read data: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home_frage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}