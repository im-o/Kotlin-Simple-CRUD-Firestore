package com.stimednp.crudfirebasefirestore.activity.addedit

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.stimednp.crudfirebasefirestore.R
import com.stimednp.crudfirebasefirestore.model.Users
import com.stimednp.crudfirebasefirestore.utils.Const.PATH_COLLECTION
import com.stimednp.crudfirebasefirestore.utils.Const.setTimeStamp
import kotlinx.android.synthetic.main.activity_addedit.*

class AddEditActivity : AppCompatActivity() {
    companion object {
        //key untuk intent data
        const val EXTRA_DATA = "extra_data"
        const val REQ_EDIT = "req_edit"
    }

    private var isEdit = false
    private var users: Users? = null
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mUsersCollection = mFirestore.collection(PATH_COLLECTION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addedit)
        //mengambil data yang dibawa dari mainactivity sesuia keynya masing2
        isEdit = intent.getBooleanExtra(REQ_EDIT, false)
        users = intent.getParcelableExtra(EXTRA_DATA)

        btn_save.setOnClickListener { saveData() }
        initView()
    }

    private fun initView() {
        //set view jika data di edit maka akan tampil pada form input
        if (isEdit) {
            btn_save.text = getString(R.string.update)
            ti_name.text = Editable.Factory.getInstance().newEditable(users?.strName)
            ti_address.text = Editable.Factory.getInstance().newEditable(users?.strAddress)
            ti_age.text = Editable.Factory.getInstance().newEditable(users?.intAge.toString())
        }
    }

    private fun saveData() {
        setData(users?.strId)
    }

    private fun setData(strId: String?) {
        createUser(strId).addOnCompleteListener {
            if (it.isSuccessful) {
                if (isEdit) {
                    Toast.makeText(this, "Sukses perbarui data", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Sukses tambah data", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Gagal tambah data", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error Added data ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    //fungsi untuk mengambil inputan data dan menyimpannya pada firestore
    private fun createUser(strId: String?): Task<Void> {
        val writeBatch = mFirestore.batch()
        val path = PATH_COLLECTION + setTimeStamp().toString() //exmp hasil : users-43287845
        val id = strId ?: path
        val name = ti_name.text.toString()
        val addr = ti_address.text.toString()
        val age = ti_age.text.toString()

        val users = Users(id, name, addr, age.toInt())
        writeBatch.set(mUsersCollection.document(id), users) //menyimpan data dengan id yang sudah ditentukan
        return writeBatch.commit()
    }
}
