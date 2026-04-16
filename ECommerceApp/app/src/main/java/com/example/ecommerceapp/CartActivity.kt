package com.example.ecommerceapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class CartActivity : AppCompatActivity() {

    companion object {
        var cartList = mutableListOf<Product>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartToolbar: MaterialToolbar = findViewById(R.id.cartToolbar)
        val recyclerViewCart: RecyclerView = findViewById(R.id.recyclerViewCart)
        val txtTotalPrice: TextView = findViewById(R.id.txtTotalPrice)
        val btnCheckout: Button = findViewById(R.id.btnCheckout)

        cartToolbar.setNavigationOnClickListener {
            finish()
        }

        recyclerViewCart.layoutManager = LinearLayoutManager(this)
        recyclerViewCart.adapter = ProductAdapter(cartList) {}

        val total = cartList.sumOf { it.price }
        txtTotalPrice.text = "Total: ৳ $total"

        btnCheckout.setOnClickListener {
            Toast.makeText(this, "Checkout clicked", Toast.LENGTH_SHORT).show()
        }
    }
}