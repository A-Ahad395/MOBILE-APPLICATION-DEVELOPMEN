package com.example.contactbookapp
import androidx.appcompat.widget.SearchView
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var txtEmptyState: TextView

    private val contactList = mutableListOf<Contact>()
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listViewContacts)
        searchView = findViewById(R.id.searchView)
        fabAdd = findViewById(R.id.fabAdd)
        txtEmptyState = findViewById(R.id.txtEmptyState)

        contactList.add(Contact("Karim Ahmed", "01823456789", "karim@gmail.com", "K"))
        contactList.add(Contact("Tanvir Hasan", "01934567890", "tanvir@gmail.com", "T"))
        contactList.add(Contact("Fahim Rahman", "01645678901", "fahim@gmail.com", "F"))
        contactList.add(Contact("Sakib Al Hasan", "01556789012", "sakib@gmail.com", "S"))
        contactList.add(Contact("Mahmudul Hasan", "01767890123", "mahmud@gmail.com", "M"))
        contactList.add(Contact("Arif Hossain", "01878901234", "arif@gmail.com", "A"))
        contactList.add(Contact("Imran Khan", "01989012345", "imran@gmail.com", "I"))
        contactList.add(Contact("Rashed Chowdhury", "01690123456", "rashed@gmail.com", "R"))
        contactList.add(Contact("Jahidul Islam", "01501234567", "jahid@gmail.com", "J"))
        adapter = ContactAdapter(this, ArrayList(contactList))
        listView.adapter = adapter

        updateEmptyState()

        fabAdd.setOnClickListener {
            showAddContactDialog()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            if (contact != null) {
                Toast.makeText(
                    this,
                    "Name: ${contact.name}\nPhone: ${contact.phone}\nEmail: ${contact.email}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            if (contact != null) {
                AlertDialog.Builder(this)
                    .setTitle("Delete Contact")
                    .setMessage("Are you sure you want to delete ${contact.name}?")
                    .setPositiveButton("Delete") { _, _ ->
                        contactList.remove(contact)
                        filterContacts(searchView.query.toString())
                        Toast.makeText(this, "${contact.name} deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            true
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText ?: "")
                return true
            }
        })
    }

    private fun showAddContactDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_contact, null)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPhone = dialogView.findViewById<EditText>(R.id.etPhone)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)

        AlertDialog.Builder(this)
            .setTitle("Add Contact")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                val email = etEmail.text.toString().trim()

                if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty()) {
                    val newContact = Contact(
                        name = name,
                        phone = phone,
                        email = email,
                        initial = name.first().uppercase()
                    )

                    contactList.add(newContact)
                    filterContacts(searchView.query.toString())

                    Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun filterContacts(query: String) {
        val filteredList = if (query.isEmpty()) {
            contactList
        } else {
            contactList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        adapter.clear()
        adapter.addAll(filteredList)
        adapter.notifyDataSetChanged()

        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (adapter.count == 0) {
            txtEmptyState.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            txtEmptyState.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
    }
}