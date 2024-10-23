package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productTitle: TextView = itemView.findViewById(R.id.product_title)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mypage_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productTitle.text = product.title
        holder.productPrice.text = "${product.price} 원" // 가격 표시
        Glide.with(holder.itemView.context)
            .load(product.image.url) // 이미지 로드
            .into(holder.productImage)

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, GoodsActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.idx) // 상품 ID를 인텐트에 추가
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = products.size
}