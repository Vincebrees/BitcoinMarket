package com.github.vincebrees.bitcoinmarket.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObserver()
    }

    open fun initUI() {}
    open fun initViewModel() {}
    open fun initObserver() {}
}