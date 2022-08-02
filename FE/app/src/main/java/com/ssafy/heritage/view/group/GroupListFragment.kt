package com.ssafy.heritage.view.group

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssafy.heritage.R
import com.ssafy.heritage.adpter.GroupListAdapter
import com.ssafy.heritage.adpter.OnItemClickListener
import com.ssafy.heritage.base.BaseFragment
import com.ssafy.heritage.databinding.FragmentGroupListBinding
import com.ssafy.heritage.viewmodel.GroupViewModel

private const val TAG = "GroupListFragment___"

class GroupListFragment :
    BaseFragment<FragmentGroupListBinding>(R.layout.fragment_group_list),  OnItemClickListener{

    private val groupListAdapter: GroupListAdapter by lazy { GroupListAdapter(this) }
    private val groupViewModel by viewModels<GroupViewModel>()

    override fun init() {
        groupViewModel.getGroupList()
        initAdapter()
        initObserver()
        initClickListener()
    }
    private fun initAdapter() = with(binding) {
        recyclerviewGroupList.adapter = groupListAdapter
    }

    private fun initObserver(){
        groupViewModel.groupList.observe(viewLifecycleOwner){
            groupListAdapter.submitList(it)
        }
    }

    private fun initClickListener() {
        binding.apply {
            fabCreateGroup.setOnClickListener {
                findNavController().navigate(R.id.action_groupListFragment_to_groupModifyFragment)
            }
        }
    }
    override fun onItemClick(position: Int) {
        Log.d(TAG, groupListAdapter.currentList[position].toString())

        val action = GroupListFragmentDirections.actionGroupListFragmentToGroupInfoFragment(groupListAdapter.currentList[position])
        findNavController().navigate(action)
    }

}