package com.example.ecommerceapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var loadingLayout: LinearLayout
    private lateinit var toolbar: MaterialToolbar

    private lateinit var adapter: ProductAdapter

    private val allProducts = mutableListOf<Product>()
    private val filteredProducts = mutableListOf<Product>()
    private val cartItems = mutableListOf<Product>()

    private var isGridMode = false
    private var currentCategory = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.topToolbar)
        recyclerView = findViewById(R.id.recyclerViewProducts)
        searchView = findViewById(R.id.searchView)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        loadingLayout = findViewById(R.id.loadingLayout)

        setSupportActionBar(toolbar)

        val chipAll: Chip = findViewById(R.id.chipAll)
        val chipElectronics: Chip = findViewById(R.id.chipElectronics)
        val chipClothing: Chip = findViewById(R.id.chipClothing)
        val chipBooks: Chip = findViewById(R.id.chipBooks)
        val chipFood: Chip = findViewById(R.id.chipFood)
        val chipToys: Chip = findViewById(R.id.chipToys)

        showLoading(true)
        loadDummyProducts()

        filteredProducts.addAll(allProducts)

        adapter = ProductAdapter(filteredProducts) { product ->
            if (!product.inCart) {
                product.inCart = true
                cartItems.add(product)
            }
            updateCartBadge()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        showLoading(false)
        updateEmptyState()

        chipAll.setOnClickListener { filterByCategory("All") }
        chipElectronics.setOnClickListener { filterByCategory("Electronics") }
        chipClothing.setOnClickListener { filterByCategory("Clothing") }
        chipBooks.setOnClickListener { filterByCategory("Books") }
        chipFood.setOnClickListener { filterByCategory("Food") }
        chipToys.setOnClickListener { filterByCategory("Toys") }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText.orEmpty(), currentCategory)
                return true
            }
        })

        setupSwipeAndDrag()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        updateCartBadge()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuCart -> {
                CartActivity.cartList = cartItems
                startActivity(Intent(this, CartActivity::class.java))
                true
            }

            R.id.menuToggle -> {
                toggleViewMode()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleViewMode() {
        isGridMode = !isGridMode
        adapter.isGridMode = isGridMode

        recyclerView.layoutManager = if (isGridMode) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }

        adapter.notifyDataSetChanged()
    }

    private fun filterByCategory(category: String) {
        currentCategory = category
        filterProducts(searchView.query.toString(), category)
    }

    private fun filterProducts(query: String, category: String) {
        val result = allProducts.filter { product ->
            val matchesCategory = category == "All" || product.category.equals(category, true)
            val matchesSearch = product.name.contains(query, true)
            matchesCategory && matchesSearch
        }

        filteredProducts.clear()
        filteredProducts.addAll(result)
        adapter.updateData(filteredProducts)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (filteredProducts.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun showLoading(show: Boolean) {
        loadingLayout.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun updateCartBadge() {
        supportActionBar?.subtitle = "Cart: ${cartItems.size}"
    }

    private fun setupSwipeAndDrag() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                adapter.moveItem(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedProduct = adapter.getProductAt(position)
                adapter.removeItem(position)
                filteredProducts.removeAt(position)
                allProducts.remove(removedProduct)
                updateEmptyState()

                Snackbar.make(recyclerView, "${removedProduct.name} deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        allProducts.add(position, removedProduct)
                        filteredProducts.add(position, removedProduct)
                        adapter.restoreItem(removedProduct, position)
                        updateEmptyState()
                    }
                    .show()
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    private fun loadDummyProducts() {
        allProducts.add(
            Product(1, "Laptop", 85000.0, 4.5f, "Electronics", R.drawable.laptop)
        )
        allProducts.add(
            Product(2, "T-Shirt", 200.0, 4.0f, "Clothing", R.drawable.tshirt)
        )
        allProducts.add(
            Product(3, "Story Book", 150.0, 4.8f, "Books", R.drawable.book)
        )
        allProducts.add(
            Product(4, "Burger", 80.0, 4.2f, "Food", R.drawable.burger)
        )
        allProducts.add(
            Product(5, "Toy Car", 1200.0, 4.1f, "Toys", R.drawable.toy_car)
        )
        allProducts.add(
            Product(6, "Headphone", 600.0, 4.4f, "Electronics", R.drawable.headphone)
        )
    }
}