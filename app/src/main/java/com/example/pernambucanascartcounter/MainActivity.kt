package com.example.pernambucanascartcounter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pernambucanascartcounter.R.drawable.ic_arrow_down
import com.example.pernambucanascartcounter.R.drawable.ic_arrow_up
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.footer_layout_bag_items_count.*
import kotlinx.android.synthetic.main.footer_layout_bag_items_summary.*
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var btnSheet: BottomSheetBehavior<View>
    private lateinit var items: MutableList<BagItem>
    private lateinit var mAdapater: ProductBagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mockList()

        btnSheet = from(bagFooterCounter)

        mAdapater = ProductBagAdapter(items) { updateTotals() }

        updateTotals()

        with(productList) {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapater
        }

        setupListeners()
    }

    private fun updateTotals() {
        countText.text = mAdapater.itemCount.toString()
        summaryCost.text = NumberFormat.getCurrencyInstance().format(mAdapater.getTotalPrice())
    }

    private fun setupListeners() {
        addProduct.setOnClickListener {
            items.add(BagItem("Teste de inserção", "50.00"))
            mAdapater.updateDataSet { updateTotals() }
        }
        dropDownUp.setOnCheckedChangeListener { _, selected ->
            if (selected)
                btnSheet.setState(STATE_EXPANDED)
            else
                btnSheet.setState(STATE_COLLAPSED)
        }
        btnSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(p0: View, p1: Float) {}

            override fun onStateChanged(view: View, state: Int) {
                if (state == STATE_EXPANDED)
                    dropDownUp.setButtonDrawable(ic_arrow_down)
                else
                    dropDownUp.setButtonDrawable(ic_arrow_up)
            }

        })
        removeAll.setOnClickListener {
            items.clear()
            mAdapater.updateDataSet { updateTotals() }
        }
    }

    private fun mockList(): MutableList<BagItem> {
        items = mutableListOf(
            BagItem("Samsung Evo 1", "10.00"),
            BagItem("Samsung Evo 2", "20.00"),
            BagItem("Samsung Evo 3", "30.60"),
            BagItem("Samsung Evo 4", "40.00"),
            BagItem("Samsung Evo 5", "50.00")
        )
        return items
    }
}
