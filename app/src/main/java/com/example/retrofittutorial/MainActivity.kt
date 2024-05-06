package com.example.retrofittutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.retrofittutorial.databinding.ActivityMainBinding
import com.example.retrofittutorial.databinding.ItemTodoBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodos()
            }
            catch (e: IOException){
                Log.e(TAG, "IOException, you might not have internet connection")
                binding.progressBar.isVisible = false
                return@launch
            }
            catch (e: HttpException){
                Log.e(TAG, "HttpException, unexpected response")
                binding.progressBar.isVisible = false
                return@launch
            }
            if(response.isSuccessful && response.body() != null){
                todoAdapter.todos = response.body()!!
            }
            else{
                Log.e(TAG, "Response not successful")
            }
            binding.progressBar.isVisible = false
        }
    }

    private fun setupRecyclerView() = binding.rvTodos.apply {
        todoAdapter = TodoAdapter()
        adapter = todoAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}