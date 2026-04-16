package com.example.contactbookapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ContactAdapter(
    context: Context,
    private val contactList: MutableList<Contact>
) : ArrayAdapter<Contact>(context, 0, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_contact, parent, false)

        val contact = contactList[position]

        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtPhone = view.findViewById<TextView>(R.id.txtPhone)
        val txtAvatar = view.findViewById<TextView>(R.id.txtAvatar)
        val imgCall = view.findViewById<ImageView>(R.id.imgCall)

        txtName.text = contact.name
        txtPhone.text = contact.phone
        txtAvatar.text = contact.initial

        // Optional: call icon click
        imgCall.setOnClickListener {
            // future: call functionality
        }

        return view
    }
}