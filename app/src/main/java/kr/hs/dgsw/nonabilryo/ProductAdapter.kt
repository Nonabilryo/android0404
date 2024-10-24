package kr.hs.dgsw.nonabilryo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val productList: List<Product>,
    private val itemClickListener: (String) -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productTitle: TextView = itemView.findViewById(R.id.product_title)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)

        fun bind(product: Product) {
            productTitle.text = product.title
            productPrice.text = "${product.price} 원" // 가격 표시
            Glide.with(itemView.context)
                .load(product.image.url) // 이미지 로드
                .into(productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mypage_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            itemClickListener(product.idx) // 클릭 시 상품의 idx를 전달
        }
    }

    override fun getItemCount() = productList.size
}