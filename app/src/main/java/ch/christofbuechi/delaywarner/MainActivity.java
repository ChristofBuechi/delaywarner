package ch.christofbuechi.delaywarner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ch.christofbuechi.delaywarner.base.dagger.AppComponent;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent.Holder.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, CheckFragment.newInstance(), CheckFragment.class.getSimpleName()).commit();
    }

}
