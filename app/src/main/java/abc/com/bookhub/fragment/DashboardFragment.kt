package abc.com.bookhub.fragment

import abc.com.bookhub.R
import abc.com.bookhub.adapter.DashboardRecyclerAdapter
import abc.com.bookhub.model.Book
import abc.com.bookhub.util.ConnectionManager
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.UrlRewriter
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.net.URL


class DashboardFragment : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnCheckInternet: Button
    val bookInfoList= arrayListOf<Book>()
   // lateinit var bookInfoList: ArrayListOf<Book>

//

//    val bookInfoList = arrayListOf<Book>(
//            Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
//            Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
//            Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
//            Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
//            Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
//            Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
//            Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
//            Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
//            Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
//            Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
//    )

    lateinit var recyclerAdapter: DashboardRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view= inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)


        // check internet through click button
        btnCheckInternet=view.findViewById(R.id.btnCheckInternet)
        btnCheckInternet.setOnClickListener {


            if (ConnectionManager().checkConnectivity(activity as Context))
            {
                // set Dialog on click button
val dialog=AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("OK"){text,listener ->
                    //Do nothing
                }
                dialog.setNegativeButton("Cancel"){text,listener->
                    //Do nothing
                }
                dialog.create()
                dialog.show()
            }
            else
            {

            }
        }

        layoutManager=LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)

        if (ConnectionManager().checkConnectivity(activity as Context))
        {
            val url = "http://13.235.250.119/v1/book/fetch_books/"

// Request a string response from the provided URL.
            val jsonObjectRequest= object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                // We will handel this response

                // try catche for resolve JSON exception and response can be corrupted

                try {
                    val success =it.getBoolean("success")
                    if (success)
                    {
                        val data=it.getJSONArray("data")
                        for (i in 0 until data.length())
                        {
                            val bookJsonObject= data.getJSONObject(i)
                            val bookObject= Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")

                            )

                            bookInfoList.add(bookObject)
                            recyclerAdapter= DashboardRecyclerAdapter(activity as Context, bookInfoList)
                            recyclerDashboard.adapter=recyclerAdapter
                            recyclerDashboard.layoutManager= layoutManager

                            recyclerDashboard.addItemDecoration(
                                    DividerItemDecoration(
                                            recyclerDashboard.context,
                                            (layoutManager as LinearLayoutManager).orientation
                                    )
                            )
                        }
                    }

                    else{
                        Toast.makeText(activity as Context, " some error occurred !!! ", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e: JSONException)
                {
                    Toast.makeText(activity as Context, " Some Unexpected Error occurred", Toast.LENGTH_SHORT).show()
                }


            },Response.ErrorListener {
                // here the handle error response

               Toast.makeText(activity as Context,"Volley error occurred !!!", Toast.LENGTH_SHORT).show()
            }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers= HashMap<String, String>()
                    headers[" "] ="application/json"
                    headers["token"]="f2f7aa9432a3a7"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }
else
        {
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not  Found /n Please On Inernet")
            dialog.setPositiveButton("Open Settings"){text,listener ->
                val settingintent =Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingintent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
               ActivityCompat.finishAffinity(activity as Activity )
            }
            dialog.create()
            dialog.show()
        }

        return view


    }


    }
