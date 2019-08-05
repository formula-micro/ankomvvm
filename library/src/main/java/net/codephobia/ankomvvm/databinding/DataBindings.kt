package net.codephobia.ankomvvm.databinding

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange

/**
 * Created by benimario on 15/03/2019.
 */
fun EditText.bindString(lifecycleOwner: LifecycleOwner,
                        string: MutableLiveData<String>?,
                        onTextChanged: ((editText: EditText) -> Unit)? = null) = string?.let {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString() != string.value) {
                string.value = s.toString()
                setSelection(start + count)
                onTextChanged?.invoke(this@bindString)
            }
        }
    })

    string.observe(lifecycleOwner, Observer { str ->
        if (str != text.toString()) {
            setText(str)
            setSelection(str.length)
        }
    })

    setText(string.value)
}

fun TextView.bindString(lifecycleOwner: LifecycleOwner,
                        string: MutableLiveData<Any>?) = string?.let {
    string.observe(lifecycleOwner, Observer { str ->
        if (str != text.toString()) {
            text = str.toString()
        }
    })

    text = string.value.toString()
}

fun EditText.onChange(onTextChanged: (editText: EditText) -> Unit) =
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(this@onChange)
        }
    })

fun Button.bindEnabled(lifecycleOwner: LifecycleOwner,
                       boolean: MutableLiveData<Boolean>?,
                       onStatusChange: ((Button, Boolean) -> Unit)? = null) = boolean?.let {
    boolean.observe(lifecycleOwner, Observer { enabled ->
        isEnabled = enabled
        onStatusChange?.invoke(this, enabled)
    })
}

fun Switch.bindChecked(lifecycleOwner: LifecycleOwner,
                       boolean: MutableLiveData<Boolean>?,
                       onStatusChange: ((Switch, Boolean) -> Unit)? = null) = boolean?.let {
    boolean.observe(lifecycleOwner, Observer { checked ->
        if (isChecked != checked) {
            isChecked = checked
        }
    })

    onCheckedChange { buttonView, isChecked ->
        if (isChecked != boolean.value) {
            boolean.value = isChecked
            onStatusChange?.invoke(this@bindChecked, isChecked)
        }
    }
}

fun <T> MutableLiveData<MutableList<T>>.add(item: T) {
    this.value?.add(item)
    this.value = this.value
}

fun <T: RecyclerView.ViewHolder, F> RecyclerView.Adapter<T>.bindItem(
    lifecycleOwner: LifecycleOwner,
    dataset: MutableLiveData<List<F>>?
) {
    dataset?.observe(lifecycleOwner, Observer {
        notifyDataSetChanged()
    })
}
