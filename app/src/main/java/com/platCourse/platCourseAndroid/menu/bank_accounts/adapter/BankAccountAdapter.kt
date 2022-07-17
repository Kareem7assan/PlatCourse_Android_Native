package com.platCourse.platCourseAndroid.menu.bank_accounts.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rowaad.app.data.model.bank_accounts_model.RecordsItem
import com.platCourse.platCourseAndroid.R
import com.platCourse.platCourseAndroid.databinding.BankAccountBinding
import java.util.*

class BankAccountAdapter : RecyclerView.Adapter<BankAccountAdapter.BankAccountVH>() {

    private var data: MutableList<RecordsItem> = ArrayList()

    var onClickItem: ((RecordsItem, Int) -> Unit)? = null

    var selectedItemPosition = -1

    fun updateSelectedItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankAccountVH {
        return BankAccountVH(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.bank_account, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BankAccountVH, position: Int) = holder.bind(data[position])

    fun swapData(data: List<RecordsItem>) {
        this.data = data as MutableList<RecordsItem>
        notifyDataSetChanged()
    }

    fun removeWithIndex(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    inner class BankAccountVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:RecordsItem) = with(BankAccountBinding.bind(itemView)) {
            tvBankName.text=item.bankName
            tvAccNum.text=item.accountNumber
            tvIbanNum.text=item.ibenNumber
            transformBtn.setOnClickListener { onClickItem?.invoke(item,adapterPosition) }
        }
    }
}