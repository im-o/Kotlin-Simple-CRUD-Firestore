package com.stimednp.crudfirebasefirestore.bind

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.stimednp.crudfirebasefirestore.R
import com.stimednp.crudfirebasefirestore.activity.addedit.AddEditActivity
import com.stimednp.crudfirebasefirestore.model.Users
import kotlinx.android.synthetic.main.item_list_user.view.*

/**
 * Created by rivaldy on 2/1/2020.
 * Find me on my lol Github :D -> https://github.com/im-o
 */

class AllUserAdapter(
    private val context: Context,
    private val collection: CollectionReference,
    options: FirestoreRecyclerOptions<Users>
    ) : FirestoreRecyclerAdapter<Users, AllUserAdapter.UsersViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false))
    }

    override fun onBindViewHolder(viewHolder: UsersViewHolder, position: Int, users: Users) {
        viewHolder.bindItem(users)
        viewHolder.itemView.setOnClickListener {
            showDialogMenu(users)
        }
    }
    class UsersViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(users: Users) {
            view.apply {
                val name = "Nama   : ${users.strName}"
                val addr = "Alamat : ${users.strAddress}"
                val age = "Umur    : ${users.intAge.toString()}"

                tv_name.text = name
                tv_adress.text = addr
                tv_age.text = age
            }
        }
    }

    private fun showDialogMenu(users: Users) {
        //dialog popup edit hapus
        val builder = AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        val option = arrayOf("Edit", "Hapus")
        builder.setItems(option) { dialog, which ->
            when (which) {
                //0 -> untuk berpindah ke activity AddEditActivity untuk edit dengan membawa data
                0 -> context.startActivity(Intent(context, AddEditActivity::class.java).apply {
                    putExtra(AddEditActivity.REQ_EDIT, true)
                    putExtra(AddEditActivity.EXTRA_DATA, users)
                })
                1 -> showDialogDel(users.strId)
            }
        }
        builder.create().show()
    }
    private fun showDialogDel(strId: String) {
        //dialog pop delete
        val builder = AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
            .setTitle("Hapus Data")
            .setMessage("Yakin mau hapus?")
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                deleteById(strId)
            }
            .setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }
    private fun deleteById(id: String) {
        //menghapus data berdasarkan id
        collection.document(id)
            .delete()
            .addOnCompleteListener { Toast.makeText(context, "Succes Hapus data", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(context, "Gagal Hapus data", Toast.LENGTH_SHORT).show() }
    }

}