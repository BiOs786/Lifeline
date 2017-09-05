package codebros.areyouat.com.lifeline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

public class HomeActivity extends AppCompatActivity {

    View container;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        ContentFragment cf = new ContentFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.container_content, cf)
                .commit();

    }


    public void addContentFragment(View view)
    {

        scrollView.post(new Runnable() {
            @Override
            public void run() {

                View view = findViewById(R.id.container_content);
                scrollView.smoothScrollTo(0, view.getTop());
            }
        });

    }

    public void loginAsHospital(View view) {

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void loginAsGuest(View view) {

        Intent intent = new Intent(getApplicationContext(), FindActivity.class);
        startActivity(intent);

    }
}
