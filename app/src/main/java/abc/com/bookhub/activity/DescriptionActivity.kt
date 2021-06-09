package abc.com.bookhub.activity

import abc.com.bookhub.R
import abc.com.bookhub.database.BookDatabase
import abc.com.bookhub.database.BookEntity
import abc.com.bookhub.util.ConnectionManager
import android.app.Activity
import android.app.AlertDialog
import android.app.VoiceInteractor
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName :TextView
    lateinit var txtBookAuthor :TextView
    lateinit var txtBookPrice :TextView
    lateinit var txtBookRating :TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFav :Button
    lateinit var progressLayout :RelativeLayout
    lateinit var progressBar: ProgressBar

    lateinit var toolbar: Toolbar

    var bookId : String?="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor= findViewById(R.id.txtBookAuthor)
        txtBookRating= findViewById(R.id.txtBookRating)
        txtBookPrice=findViewById(R.id.txtBookPrice)
        imgBookImage=findViewById(R.id.imgBookImage)
        txtBookDesc= findViewById(R.id.txtBookDesc)
        btnAddToFav= findViewById(R.id.btnAddToFav)
        progressBar=findViewById(R.id.progressBar)
        progressLayout=findViewById(R.id.progressLayout)
        progressBar.visibility=View.VISIBLE
        progressLayout.visibility=View.VISIBLE

        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?. title="Book Details"

        if (intent != null)
        {
            bookId= intent. getStringExtra("book_id")
        }
        else{
            finish()
            Toast.makeText(this, "Some Unexpected error occurred", Toast.LENGTH_SHORT).show()
        }

        if (bookId =="100")
        {
            finish()
            Toast.makeText(this, "Some Unexpected error occurred", Toast.LENGTH_SHORT).show()
        }

        val queue= Volley.newRequestQueue(this)
        val url= "http://13.235.250.119/v1/book/get_book/"
        val jsonParams= JSONObject()
        jsonParams.put("book_id", bookId)

        if(ConnectionManager().checkConnectivity(this)){
            val jsonRequest= object :JsonObjectRequest(Request.Method.POST ,url, jsonParams, Response.Listener {

                try {
                    val success= it.getBoolean("success")
                    if (success)
                    {
                        val bookJsonObject= it.getJSONObject("book_data")
                         progressLayout.visibility=View.GONE
val bookImageUrl= bookJsonObject.getString("image")
                        //Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(imgBookImage)
                        txtBookName.text=bookJsonObject.getString("name")
                        txtBookAuthor.text=bookJsonObject.getString("author")
                        txtBookAuthor.text=bookJsonObject.getString("price")
                        txtBookPrice.text=bookJsonObject.getString("rating")
                        txtBookDesc.text=bookJsonObject.getString("description")


                        val bookEntity=BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                           txtBookAuthor.text.toString(),
                            txtBookDesc.text.toString(),
                           txtBookRating.text.toString(),
                            txtBookAuthor.text.toString(),
                            bookImageUrl

                        )
                        val checkFav= DBAsyncTask(applicationContext,bookEntity,1).execute()
                        val isFav=checkFav.get()
                        if (isFav)
                        {
                            btnAddToFav.text= "Remove from favourites"
                            val favColor= ContextCompat.getColor(applicationContext,R.color.black)
                            btnAddToFav.setBackgroundColor(favColor)
                        }else{
                            btnAddToFav.text= "Add to favourites"
                            val nofavColor= ContextCompat.getColor(applicationContext,R.color.red)
                            btnAddToFav.setBackgroundColor(nofavColor)
                        }
btnAddToFav.setOnClickListener {
    if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get())
    {
        val asyc= DBAsyncTask(applicationContext,bookEntity,2).execute()
        val result =asyc.get()
        if(result)
        {
            Toast.makeText(this,"Book Added to FAvorites",Toast.LENGTH_SHORT).show()
            btnAddToFav.text="Remove from favourites"
            val favcolor=ContextCompat.getColor(applicationContext,R.color.black)
            btnAddToFav.setBackgroundColor(favcolor)
        }
        else{
            Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()
        }
    }
    else{
        val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
        val result=async.get()
        if (result){
            Toast.makeText(this,"Book Removed from favorites",Toast.LENGTH_SHORT).show()
            btnAddToFav.text= "Add to Favourites"
            val nofavColor= ContextCompat.getColor(applicationContext,R.color.red)
            btnAddToFav.setBackgroundColor(nofavColor)

        }
        else{
            Toast.makeText(this, " some error occurred !!! ", Toast.LENGTH_SHORT).show()
        }
    }
}
                    }
                    else{
                        Toast.makeText(this, " some error occurred !!! ", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e: Exception){
                    Toast.makeText(this, " Some   Error occurred", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, " Volley error $it", Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers= HashMap<String, String>()
                    headers["Content-type"]= "application/json"
                    headers["token"]="f2f7aa9432a3a7"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
else    {
            val dialog= AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not  Found /n Please On Inernet")
            dialog.setPositiveButton("Open Settings"){text,listener ->
                val settingintent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingintent)
                finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }

    }
    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int): AsyncTask<Void, Void, Boolean>()
    {
        /*
mode 1 -> check  DB if the book is favourite or not
mode 2-> save the book in to DB  as favourite
mode 3 -> Remove the favourite book

           **/

        val db = Room.databaseBuilder(context, BookDatabase:: class.java, "books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when    (mode)
            {
                1->{


                    val book:BookEntity?= db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book !=null
                    // Check DB  if the book is favurite or not
                }

                2-> {
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                    // Save the book into DB  as favourite
                }

                3-> {
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                    // Remove the favourite book
                }
            }



            return false
        }


    }
}