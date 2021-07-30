
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.alchemi.R


/**
 * A simple [Fragment] subclass.
 */
class NoInternetFragment : Fragment() {

    lateinit var refresh_btn:Button
    lateinit var v:View
    lateinit var tv:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_no_internet, container, false)
        tv=v.findViewById(R.id.ops_tv)
        refresh_btn=v.findViewById(R.id.refresh_btn)

        refresh_btn.setOnClickListener {
           // (activity as FosterBaseActivity?)?.clickOnRefreshBtn()
        }
        return v
    }


}
