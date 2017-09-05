package codebros.areyouat.com.lifeline;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by BiOs on 04-09-2017.
 */

public class ContentFragment extends Fragment {

    public ContentFragment()
    {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vison_mission, container, false);
    }

}
