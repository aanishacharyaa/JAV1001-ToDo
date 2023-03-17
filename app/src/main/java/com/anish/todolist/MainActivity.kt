package com.anish.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog


import com.anish.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Declare the views and variables that will be used
    private lateinit var listView: ListView
    private lateinit var items: MutableList<String>
    private lateinit var adapter: BaseAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get references to the views from the layout
        val inputField = binding.inputField
        val addButton = binding.addButton
        listView = binding.listView
        val deleteAllButton = binding.deleteAllButton

        // Initialize the list of items and the adapter
        items = mutableListOf()
        adapter = object : BaseAdapter() {

            // Return the number of items in the list
            override fun getCount(): Int {
                return items.size
            }

            // Return the item at a specific position in the list
            override fun getItem(position: Int): Any {
                return items[position]
            }

            // Return the ID of the item at a specific position in the list
            override fun getItemId(position: Int): Long {
                return position.toLong()
            }
            // Inflate the layout for each item in the list and set the text and click listener
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val itemView = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)
                val itemTextView = itemView.findViewById<TextView>(R.id.itemText)
                itemTextView.text = items[position]
                val deleteButton = itemView.findViewById<Button>(R.id.deleteButton)
                deleteButton.setOnClickListener {
                    items.removeAt(position)
                    notifyDataSetChanged()
                }
                return itemView
            }
        }
//sets the adapter for the ListView to display the list items.
        listView.adapter = adapter

        //sets a click listener on the add button to add a new item to the list.
        addButton.setOnClickListener {
            val newItem = inputField.text.toString()
            if (newItem.isNotEmpty()) {
                items.add(newItem)
                adapter.notifyDataSetChanged()
                inputField.setText("")
            }
        }

//sets a click listener on the list items to show an edit dialog when an item is clicked
        listView.setOnItemClickListener { parent, view, position, id ->
            val item = adapter.getItem(position) as String
            val editView = LayoutInflater.from(this).inflate(R.layout.edit_dialog, null)
            val editTextField = editView.findViewById<EditText>(R.id.editTextField)
            editTextField.setText(item)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Edit item")
                .setView(editView)
                .setPositiveButton("Save") { _, _ ->
                    val newItem = editTextField.text.toString()
                    if (newItem.isNotEmpty()) {
                        items[position] = newItem
                        adapter.notifyDataSetChanged()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }
//sets a click listener on the list items to show an edit dialog when an item is clicked
        deleteAllButton.setOnClickListener {
            items.clear()
            adapter.notifyDataSetChanged()
        }
    }
}