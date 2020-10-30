package com.example.taskstodo.taskList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskstodo.R
import com.example.taskstodo.data.Task
import com.example.taskstodo.databinding.FragmentTaskListBinding
import kotlinx.coroutines.CoroutineScope

class TaskListFragment : Fragment() {

    private lateinit var taskListViewModel: TaskListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTaskListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task_list, container, false
        )

        setFragmentResultListener("requestKey") { key, bundle ->
            bundle.getString("bundleKey")?.let {
                val word = Task(it)
                taskListViewModel.insert(word)
            }
        }

        taskListViewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        binding.viewModel = taskListViewModel
        binding.lifecycleOwner = this

        val adapter = WordListAdapter(TaskListItemListener { id ->
            view?.findNavController()?.navigate(TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(id))
        })

        binding.recyclerviewlist.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewlist.adapter = adapter

        taskListViewModel.allTasks.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.submitList(it) }
        })

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task: Task? = adapter.getWordAtPosition(position)
                task?.let {
                    taskListViewModel.delete(it)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewlist)

        binding.fab.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_taskListFragment_to_taskAddFragment)
        }
        return binding.root
    }
}