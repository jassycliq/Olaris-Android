package tv.olaris.android.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.launch

import tv.olaris.android.databinding.FragmentAddServerBinding
import tv.olaris.android.service.olaris_http_api.model.LoginResponse
import tv.olaris.android.service.olaris_http_api.model.LoginRequest
import java.net.ConnectException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [AddServer.newInstance] factory method to
 * create an instance of this fragment.
 */


class AddServer : Fragment() {

    // TODO: Rename and change types of parameters
    private var _binding: FragmentAddServerBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: AddServerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val model : AddServerViewModel by viewModels()
        viewModel = model

        viewModel.username.observe(this, Observer<String>{
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddServerBinding.inflate(inflater, container, false)
        binding.btnAddServer.setOnClickListener{
            var hasErrors = false

            with(binding.textEditServerUrl){
                if(this.text != null && this.text.toString() != ""){
                    if(!URLUtil.isValidUrl(this.text.toString())){
                        this.error = "This is not a valid URL."
                        hasErrors = true
                    }
                }else{
                   this.error = "Please fill in your server address in URL format."
                    hasErrors = true
                }
            }

            with(binding.textEditUsername){
                if(this.text.toString() == ""){
                    this.error = "Please fill in your username."
                    hasErrors = true
                }
            }

            with(binding.textEditPassword){
                if(this.text.toString() == ""){
                    this.error = "Please fill in your password."
                    hasErrors = true
                }
            }
            val v = this
            Log.d("http", "Starting login: ${hasErrors.toString()}")

            if(!hasErrors){
                Log.d("http", "No errors!")

                lifecycle.coroutineScope.launch{
                    val loginResponse = doLogin(binding.textEditServerUrl.text.toString(),
                            binding.textEditUsername.text.toString(),
                            binding.textEditPassword.text.toString())
                    if(loginResponse.hasError){
                        Log.d("http", "Got error ${loginResponse.message}")
                        Toast.makeText(v.context, "Received error while connecting to server: ${loginResponse.message}", Toast.LENGTH_LONG).show()
                    }else{
                        Log.d("http", "Everything ok! ${loginResponse.jwt}")
                        //ServerDoa.insertServer(Server(binding.textEditServerUrl.text.toString()))
                        Toast.makeText(v.context, "Succesfully connected to server.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            Log.d("http", "Done doing login")
        }
        return binding.root
    }

    private suspend fun doLogin(url: String, username: String, password: String) : LoginResponse {
        val authLoginUrl = url + "/olaris/m/v1/auth"
        Log.d("http", "Login URL: $authLoginUrl")
        try{
            val client = HttpClient(Android) {
                install(JsonFeature){
                    accept(ContentType.Application.Json)
                    serializer = KotlinxSerializer()
                }
                expectSuccess = false
            }
            val loginResponse = client.post<LoginResponse>(authLoginUrl){
                body = LoginRequest(username, password)
                contentType(ContentType.Application.Json)
            }
            Log.d("http", "Login response $loginResponse")
            return loginResponse
        }catch(e: ClientRequestException){
            Log.d("http", "Error: ${e.message}")
            return LoginResponse(true, e.toString())
        }catch(e: ConnectException){
            return LoginResponse(true, e.toString())
        }


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddServer.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddServer().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
