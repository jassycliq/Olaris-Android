package tv.olaris.android.ui.addServer

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import tv.olaris.android.OlarisApplication
import tv.olaris.android.databases.Server
import tv.olaris.android.databinding.FragmentAddServerBinding
import tv.olaris.android.service.http.OlarisHttpService
import tv.olaris.android.util.*

class AddServerFragment : Fragment() {
    private var _binding: FragmentAddServerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddServer.setOnClickListener{
            var hasErrors = false
            hideKeyboard()

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
            with(binding.textEditServerName){
                if(this.text.toString() == ""){
                    this.error = "Please fill in your server name."
                    hasErrors = true
                }
            }

            Log.d("http", "Starting login: ${hasErrors.toString()}")

            if(!hasErrors){
                Log.d("http", "No errors!")

                lifecycle.coroutineScope.launch{
                    val loginResponse = OlarisHttpService(binding.textEditServerUrl.text.toString().removeSuffix("/")).LoginUser(
                        binding.textEditUsername.text.toString(),
                        binding.textEditPassword.text.toString())

                    if(loginResponse.hasError){
                        Log.d("http", "Got error ${loginResponse.message}")
                        Snackbar.make(view, "Error: ${loginResponse.message}", Snackbar.LENGTH_LONG).show()
                    }else {
                        Log.d("http", "Everything ok! ${loginResponse.jwt}")
                        Toast.makeText(view.context, "Succesfully added server.", Toast.LENGTH_LONG).show()

                        OlarisApplication.applicationContext().serversRepository.insertServer(
                            Server(
                                binding.textEditServerUrl.text.toString(),
                                binding.textEditUsername.text.toString(),
                                binding.textEditPassword.text.toString(),
                                binding.textEditServerName.text.toString(),
                                loginResponse.jwt.toString()
                            )
                        )

                     val action =
                            AddServerFragmentDirections.actionFragmentAddServerToFragmentServerList()
                        view.findNavController().navigate(action)
                    }
                }
            }
            Log.d("http", "Done doing login")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddServerBinding.inflate(inflater, container, false)

        return binding.root
    }
}
