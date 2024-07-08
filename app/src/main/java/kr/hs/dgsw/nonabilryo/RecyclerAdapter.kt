package kr.hs.dgsw.nonabilryo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.nonabilryo.databinding.ItemRecyclerViewBinding

class RecyclerAdapter(private val dataList: List<Item>) : RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    inner class Holder(val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.textView6.text = item.titleText
            binding.textView7.text = item.infoText
            binding.textView8.text = item.priceText
            binding.imageView3.setImageResource(item.imageId)
            binding.textView9.text = item.heartCount.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

