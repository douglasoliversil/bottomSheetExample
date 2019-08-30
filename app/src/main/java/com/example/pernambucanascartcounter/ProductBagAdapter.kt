package com.example.pernambucanascartcounter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_bag_product_recycler_item.view.*
import kotlinx.android.synthetic.main.layout_item_quantity_selector.view.*
import java.text.NumberFormat

class ProductBagAdapter(
    private val items: MutableList<BagItem>,
    private val dataHasChanged: () -> Unit
) : RecyclerView.Adapter<ProductBagAdapter.ProductBagViewHolder>() {

    override fun onBindViewHolder(holder: ProductBagViewHolder, position: Int) {
        with(items[position]) {
            holder.view.itemDescription.text = this.description
            holder.view.itemPrice.text = NumberFormat.getCurrencyInstance().format(this.price.toDouble())
            holder.view.itemCount.setText(this.quantity.toString())
            holder.view.itemCount.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.startsWith(ZERO)!!) {
                        holder.view.itemCount.setText(ONE)
                        Toast.makeText(
                            holder.view.context,
                            "Valor não pode ser menor que 1.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.length?.let { holder.view.itemCount.setSelection(it) }
                }

            })
            holder.view.addItem.setOnClickListener {
                this.quantity++
                holder.view.itemCount.setText(this.quantity.toString())
            }
            holder.view.removeItem.setOnClickListener {
                if (this.quantity > 1) this.quantity--
                holder.view.itemCount.setText(this.quantity.toString())
            }
            holder.view.deleteItem.setOnClickListener {
                items.removeAt(position)
                updateDataSet(dataHasChanged)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBagViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_bag_product_recycler_item, parent, false)
        return ProductBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateDataSet(function: () -> Unit) {
        super.notifyDataSetChanged()
        function()
    }

    fun getTotalPrice(): Double {
        var countTotalPrice = 0.0
        items.forEach {
            countTotalPrice += it.price.toDouble()
        }
        return countTotalPrice
    }

    class ProductBagViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val ZERO = "0"
        private const val ONE = "1"

    }

}
