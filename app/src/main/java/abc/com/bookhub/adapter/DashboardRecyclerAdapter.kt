package abc.com.bookhub.adapter

import abc.com.bookhub.R
import abc.com.bookhub.activity.DescriptionActivity
import abc.com.bookhub.model.Book
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.lang.reflect.Array.get
import java.util.*

class DashboardRecyclerAdapter ( val context: Context, val  itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtBookName :TextView =view.findViewById(R.id.txtBookName )
        val txtBookAuthor: TextView=view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice :TextView =view.findViewById(R.id.txtBookPrice )
        val txtBookRating: TextView=view.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView=view.findViewById(R.id.imgBookImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardRecyclerAdapter.DashboardViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardRecyclerAdapter.DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardRecyclerAdapter.DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating

//  Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)



     // Picasso.get().load(book.bookImage).into(holder.imgBookImage)
      //  holder.imgBookImage.setImageResource(book.bookImage)
holder.llContent.setOnClickListener{
  val intent= Intent (context, DescriptionActivity::class.java)
    intent.putExtra("book_id", book.bookId)
    context.startActivity(intent)
}
    }

    override fun getItemCount(): Int {
        return 20
    }

}