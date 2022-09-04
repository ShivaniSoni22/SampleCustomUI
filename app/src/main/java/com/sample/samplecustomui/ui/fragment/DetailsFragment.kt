package com.sample.samplecustomui.ui.fragment

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sample.samplecustomui.R

class DetailsFragment : Fragment() {

    var name:String = ""
    var mobileNumber:String = ""
    var city:String = ""

    val args: DetailsFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name= args.name
        mobileNumber= args.mobileNumber
        city= args.city
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txtBack = view.findViewById<TextView>(R.id.txt_back)
        val txtName = view.findViewById<TextView>(R.id.txt_name)
        val txtMobileNumber = view.findViewById<TextView>(R.id.txt_mobile_number)
        val txtCity = view.findViewById<TextView>(R.id.txt_city)
        txtName.text= Html.fromHtml("<b>Name: </b> $name")
        txtMobileNumber.text= Html.fromHtml("<b>Mobile Number: </b> $mobileNumber")
        txtCity.text= Html.fromHtml("<b>City: </b> $city")

        txtBack.setOnClickListener {
            findNavController().navigate(
                DetailsFragmentDirections.actionBottomSheetFragmentToCustomUiFragment(
                    isAnimationVisible = false, isCustomUiVisible = true
                )
            )
        }
    }

}