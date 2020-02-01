package com.stimednp.crudfirebasefirestore.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stimednp.crudfirebasefirestore.R
import com.stimednp.crudfirebasefirestore.activity.addedit.AddEditActivity
import com.stimednp.crudfirebasefirestore.bind.AllUserAdapter
import com.stimednp.crudfirebasefirestore.model.Users
import com.stimednp.crudfirebasefirestore.utils.Const.PATH_AGE
import com.stimednp.crudfirebasefirestore.utils.Const.PATH_COLLECTION
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter: FirestoreRecyclerAdapter<Users, AllUserAdapter.UsersViewHolder>
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mUsersCollection = mFirestore.collection(PATH_COLLECTION)
    private val mQuery = mUsersCollection.orderBy(PATH_AGE, Query.Direction.ASCENDING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        setupAdapter()
    }

    private fun initView() {
        supportActionBar?.title = "Simple Crud Firestore"
        rv_firedb.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        fab_firedb.setOnClickListener {
            //berpindah ke activity AddEditActivity untuk tambah data
            startActivity(Intent(this, AddEditActivity::class.java).apply {
                putExtra(AddEditActivity.REQ_EDIT, false)
            })
        }
    }

    private fun setupAdapter() {
        //set adapter yang akan menampilkan data pada recyclerview
        val options = FirestoreRecyclerOptions.Builder<Users>()
            .setQuery(mQuery, Users::class.java)
            .build()

//        mAdapter = object : FirestoreRecyclerAdapter<Users, UsersViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
//                return UsersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false))
//            }
//
//            override fun onBindViewHolder(viewHolder: UsersViewHolder, position: Int, model: Users) {
//                viewHolder.bindItem(model)
//                viewHolder.itemView.setOnClickListener {
//                    showDialogMenu(model)
//                }
//            }
//        }
        mAdapter = AllUserAdapter(this, mUsersCollection, options)
        mAdapter.notifyDataSetChanged()
        rv_firedb.adapter = mAdapter
    }

//    private fun showDialogMenu(users: Users) {
//        //dialog popup edit hapus
//        val builder = AlertDialog.Builder(this@MainActivity, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
//        val option = arrayOf("Edit", "Hapus")
//        builder.setItems(option) { dialog, which ->
//            when (which) {
//                //0 -> untuk berpindah ke activity AddEditActivity untuk edit dengan membawa data
//                0 -> startActivity(Intent(this, AddEditActivity::class.java).apply {
//                    putExtra(AddEditActivity.REQ_EDIT, true)
//                    putExtra(AddEditActivity.EXTRA_DATA, users)
//                })
//                1 -> showDialogDel(users.strId)
//            }
//        }
//        builder.create().show()
//    }

//    private fun showDialogDel(strId: String) {
//        //dialog pop delete
//        val builder = AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
//            .setTitle("Hapus Data")
//            .setMessage("Yakin mau hapus?")
//            .setPositiveButton(android.R.string.yes) { dialog, which ->
//                deleteById(strId)
//            }
//            .setNegativeButton(android.R.string.cancel, null)
//        builder.create().show()
//    }

//    private fun deleteById(id: String) {
//        //menghapus data berdasarkan id
//        mUsersCollection.document(id)
//            .delete()
//            .addOnCompleteListener { Toast.makeText(this, "Succes Hapus data", Toast.LENGTH_SHORT).show() }
//            .addOnFailureListener { Toast.makeText(this, "Gagal Hapus data", Toast.LENGTH_SHORT).show() }
//    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }
}
