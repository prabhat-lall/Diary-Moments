package com.example.mynotes

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.adapter.NotesAdapter
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.model.Notes
import com.example.mynotes.utils.PasswordManager
import com.example.mynotes.viewmodel.NotesViewModel
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Calendar


const val TAG = "homeFragment"
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val viewModel: NotesViewModel by viewModels()
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    var oldNotesList = arrayListOf<Notes>()

    private val REQUEST_STORAGE_PERMISSION = 101
    private val REQUEST_POST_NOTIFICATION_PERMISSION = 102

    private var mainMenu : Menu? =null
    lateinit var adapter: NotesAdapter
    lateinit var passwordManager: PasswordManager

    private lateinit var layoutManager : GridLayoutManager
    private lateinit var headerView:View
    private lateinit var headerProfileImageView:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreateView")
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headerView = binding.navView.getHeaderView(0)!!
        passwordManager = PasswordManager(requireContext())
        Log.d(TAG,"onViewCreated")
        if(passwordManager.isAutoThemeOn()){
            executeOperationBasedOnTime()
        }else {
            setAppTheme(passwordManager.getTheme())
        }
        headerProfileImageView = headerView.findViewById<ImageView>(R.id.iv_profile)
        setProfilePhotoAtProfile()
        //Todo setting name in tv_quote
        headerView.findViewById<TextView>(R.id.tv_quote).text = passwordManager.getName().toString()

        setHasOptionsMenu(true)
        // Restore scroll position if savedInstanceState is not null

        setUpAdapter()
        //checkPermissions()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissionsForPostNotification()
        }
        //onScrollStatusBarColor()
        handleClickListeners()
        handleDrawerNavigation()
        // setStatusBarColor()
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.materialToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar?.title = ""
        actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()



    }

    private fun setProfilePhotoAtProfile() {
        val imgUri = createImageUri()
        Log.d(TAG, "onViewCreated: $imgUri")
        if (imgUri != null)
            headerProfileImageView.setImageURI(imgUri)
        else
            headerProfileImageView.setImageResource(R.drawable.app_icon_sky)
        headerProfileImageView.clipToOutline = true
    }

    private fun setAppTheme(themeNumber:Int) {
        val headerView = binding.navView.getHeaderView(0)!!
        val bgImageView = headerView.findViewById<ImageView>(R.id.iv_drawer_header)
        if(themeNumber == 0){
            binding.root.setBackgroundColor(resources.getColor(R.color.background_100))
            binding.ivMainBg.setImageResource(R.drawable.landscape1)
            bgImageView.setBackgroundResource(R.drawable.landscape1)
        }else if (themeNumber == 1){
            binding.root.setBackgroundColor(resources.getColor(R.color.background_200))
            binding.ivMainBg.setImageResource(R.drawable.landscape2)
            bgImageView.setBackgroundResource(R.drawable.landscape2)
        }else if (themeNumber == 2){
            binding.root.setBackgroundColor(resources.getColor(R.color.background_300))
            binding.ivMainBg.setImageResource(R.drawable.landscape3)
            bgImageView.setBackgroundResource(R.drawable.landscape3)
        }else if (themeNumber == 3){
            binding.root.setBackgroundColor(resources.getColor(R.color.background_400))
            binding.ivMainBg.setImageResource(R.drawable.landscape4)
            bgImageView.setBackgroundResource(R.drawable.landscape4)
        }else if (themeNumber == 4){
            binding.root.setBackgroundColor(resources.getColor(R.color.background_500))
            binding.ivMainBg.setImageResource(R.drawable.landscape5)
           // binding.navView.getHeaderView(0).setBackgroundResource(R.drawable.landscape5)
            bgImageView.setBackgroundResource(R.drawable.landscape5)
        }
    }

    private fun handleClickListeners() {
        binding.btnAddNotes.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_homeFragment_to_createNotesFragment)
        }
        binding.materialToolbar.setNavigationOnClickListener {
            Log.d("NavigationClickListener", "Navigation icon clicked")
           // Toast.makeText(requireContext(), "Navigation clicked", Toast.LENGTH_SHORT).show()
        }

        headerView.findViewById<ImageView>(R.id.iv_profile).setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_profileSettingFragment)
        }



    }
    private fun createImageUri(): Uri? {
        val image = File(requireActivity().applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            "com.example.mynotes.fileprovider",
            image
        )
    }

    private fun setUpAdapter() {
        viewModel.getNotes().observe(viewLifecycleOwner) { notesList ->
            oldNotesList = notesList as ArrayList<Notes>
            notesList.reverse()
            layoutManager = GridLayoutManager(requireContext(), 1)
            binding.rvAllNotes.layoutManager = layoutManager
            adapter = NotesAdapter(requireContext(), notesList,::showDeleteMenu,::goToEditFragment)
            binding.rvAllNotes.adapter = adapter
            if(passwordManager.getNumberOfDiary().toString() != notesList.size.toString()){
                passwordManager.setNumberOfDiary(notesList.size.toString())
            }
        }


    }


    private fun handleDrawerNavigation() {
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_theme -> {
                    navigateToThemeFragment()
                    true
                }
                R.id.nav_share -> {
                    handleShare()
                }
                R.id.nav_faq -> {navigateToFaqFragment()
                    true
                }
                R.id.nav_diary_lock -> {navigateToLockFragment()
                true}
                R.id.nav_Settings ->{
                    navigateToSettingFragment()
                    true
                }
                R.id.nav_about ->{
                    navigateToAboutFragment()
                    true
                }
                R.id.nav_notification ->{
                    navigateToNotificationFragment()
                    true
                }
                else -> true
            }
        }
    }

    private fun navigateToNotificationFragment() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_notificationFragment)
    }

    private fun navigateToAboutFragment() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_aboutFragment)
    }

    private fun navigateToSettingFragment() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_settingFragment)
    }

    private fun navigateToThemeFragment() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_chooseThemeFragment)
    }

    private fun handleShare(): Boolean {

        val intent = Intent(Intent.ACTION_SEND)
        val shareBody =
            "I've been using Thought Canvas Diary to capture my thoughts and cherished memories. Join me in this journey. Download the app here: Android: https://www.indusappstore.com/"
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.share_subject)
        )
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(intent, "Share body"))
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mainMenu = menu
        inflater.inflate(R.menu.delete_menu, menu)
        showDeleteMenu(false)
        val menuSearch = menu.findItem(R.id.menu_search)
        val searchView = menuSearch.actionView as android.widget.SearchView
        searchView.queryHint = "Search in Diaries"
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesFiltering(newText)
                return true
            }




        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.menu_search -> {

            }
            R.id.menu_sort -> {
                sortNotes()
            }
            R.id.menu_delete ->{ delete()}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortNotes() {
        if (passwordManager.isLatestFirst()) {
            viewModel.sortedDesc().observe(viewLifecycleOwner) {
                binding.rvAllNotes.layoutManager = GridLayoutManager(requireContext(), 1)
                adapter = NotesAdapter(requireContext(), it, ::showDeleteMenu,::goToEditFragment)
                binding.rvAllNotes.adapter = adapter
            }

            Toast.makeText(requireContext(), "Latest First", Toast.LENGTH_SHORT).show()
            passwordManager.setLatestFirst(false)
            viewModel.setFlag(false)
        } else {
            viewModel.sortedAsc().observe(viewLifecycleOwner) {
                binding.rvAllNotes.layoutManager = GridLayoutManager(requireContext(), 1)
                adapter = NotesAdapter(requireContext(), it, ::showDeleteMenu,::goToEditFragment)
                binding.rvAllNotes.adapter = adapter
            }
            Toast.makeText(requireContext(), "Oldest First", Toast.LENGTH_SHORT).show()
            passwordManager.setLatestFirst(true)
            viewModel.setFlag(true)
        }
    }

    private fun delete() {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Do you want to delete the items")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Yes"){_,_->
            //adapter.deleteSelectedItem()
            adapter.itemSelectedList.forEach {
                viewModel.deleteNotes(it)
                adapter.notifyDataSetChanged()
            }
            showDeleteMenu(false)

        }
        alertDialog.setNegativeButton("No"){_,_->
        }
        alertDialog.show()
    }

    private fun showDeleteMenu(show:Boolean){
        mainMenu?.findItem(R.id.menu_delete)?.isVisible = show
        mainMenu?.findItem(R.id.menu_search)?.isVisible = !show
        mainMenu?.findItem(R.id.menu_sort)?.isVisible = !show
        //binding.materialToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

//        if(binding.materialToolbar.navigationIcon?.isVisible == !show){
//            binding.materialToolbar.setNavigationIcon(R.drawable.baseline_menu_24)
//        }

       // binding.materialToolbar.visibility = View.GONE



    }

    private fun goToEditFragment(data:Notes){
        val action = HomeFragmentDirections.actionHomeFragmentToEditNotesFragment(data)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun notesFiltering(newText: String?) {
        val newFilteredList = arrayListOf<Notes>()
        for (i in oldNotesList) {
            if (i.notes.contains(newText!!) || i.date.contains(newText!!) ||
                i.subTitle.contains(newText!!) || i.title.contains(newText!!)
            ){
                newFilteredList.add(i)
            }
        }
        adapter.filterNotes(newFilteredList)
    }


    private fun navigateToSearchFragment() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_searchFragment)
    }
    private fun navigateToFaqFragment() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_faqFragment)
    }

    private fun navigateToLockFragment() {
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_lockFragment)
    }


    private fun onScrollStatusBarColor() {
        binding.rvAllNotes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 10) {
                    binding.materialToolbar.setBackgroundResource(R.color.statusBarColor)

                } else if (dy < 5) {
                    binding.materialToolbar.setBackgroundResource(R.color.transparent)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun setStatusBarColor() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.statusBarColor)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart")
    }
    override fun onResume() {
        super.onResume()
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        Log.d(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG,"onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }


    fun executeOperation1() {
        // Your operation for the first interval
        setAppTheme(0)
    }

    fun executeOperation2() {
        // Your operation for the second interval
        setAppTheme(1)
    }

    fun executeOperation3() {
        // Your operation for the third interval
        setAppTheme(2)
    }

    fun executeOperation4() {
        // Your operation for the fourth interval
        setAppTheme(3)
    }

    fun executeOperation5() {
        // Your operation for the fourth interval
        setAppTheme(4)
    }

    // Function to execute the operation based on the current time
    fun executeOperationBasedOnTime() {
        // Define time intervals in hours (24-hour format) and corresponding operations
        val intervals = listOf(
            Pair(0, 4) to ::executeOperation5,  // Fifth part: 00:00 am - 04:00 am
            Pair(4, 9) to ::executeOperation1,  // First part: 04:00 am - 09:00 am
            Pair(9, 13) to ::executeOperation2, // Second part: 09:00 am - 01:00 pm
            Pair(13, 16) to ::executeOperation3, // Third part: 01:00 am - 04:00 pm
            Pair(16, 20) to ::executeOperation4, // Fourth part: 04:00 pm - 08:00 pm
            Pair(18, 24) to ::executeOperation5// Fifth part: 08:00 pm - 00:00 am
        )

        // Get the current time and check which interval it falls into
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)

        intervals.forEach { (timeInterval, operation) ->
            val (startTime, endTime) = timeInterval
            if (currentHour in startTime until endTime) {
                // Toast.makeText(requireContext(), "$startTime - $endTime",Toast.LENGTH_SHORT).show()
                operation()
                return
            }
        }
    }


    private fun checkPermissions() {
        val readExternalStorageCheck = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (readExternalStorageCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_STORAGE_PERMISSION
            )
        }
    }

    private fun checkPermissionsForPostNotification() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.POST_NOTIFICATIONS
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_POST_NOTIFICATION_PERMISSION
            )
        }
    }

    // Function to handle permission result (replace with your actual logic)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your functionality
                Toast.makeText(requireContext(), "Storage permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied, inform user (optional)
                Toast.makeText(requireContext(), "Storage permission is required to access files", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_POST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your functionality
                Toast.makeText(requireContext(), "Post Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied, inform user (optional)
                Toast.makeText(requireContext(), "Post Notification permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }



}