package abc.com.bookhub.fragment

import abc.com.bookhub.R
import abc.com.bookhub.adapter.FavouriteRecyclerAdapter
import abc.com.bookhub.database.BookDatabase
import abc.com.bookhub.database.BookEntity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class FavouritesFragment : Fragment() {
    lateinit var recyclerFavorite:RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    var dbBookList= listOf<BookEntity>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favourites, container, false)
      recyclerFavorite=view.findViewById(R.id.recyclerFavourite)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        layoutManager=GridLayoutManager(activity as Context, 2)
        dbBookList=RetrieveFavourites(activity as Context).execute().get()

        if (activity != null)
        {
            progressLayout.visibility=View.GONE
            recyclerAdapter= FavouriteRecyclerAdapter(activity as Context, dbBookList)
            recyclerFavorite.adapter=recyclerAdapter
            recyclerFavorite.layoutManager=layoutManager
        }
        return view
    }

class  RetrieveFavourites(val context: Context): AsyncTask<Void, Void,List<BookEntity>>(){
    override fun doInBackground(vararg params: Void?): List<BookEntity> {
      val db= Room.databaseBuilder(context, BookDatabase:: class.java, "book-db").build()
        return db.bookDao().getAllBooks()
    }

}
    }
