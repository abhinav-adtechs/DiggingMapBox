package co.swisapp.abhinav.diggingmapbox;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class VideoPlayActivity extends AppCompatActivity {

    Intent videoIntent = getIntent() ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_play);

        init() ;
        setinit() ;
        getIntentData() ;

    }

    private void setinit() {

    }

    private void init() {
    }

    private void getIntentData() {

        videoIntent.getStringExtra("imageUrl") ;
        videoIntent.getStringExtra("videoUrl");
    }


}


class DownloadVideo extends AsyncTask<String, String, String>{

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {

        String videoUrl ;/*use sharedpreferences as the class is an asynctask, don't download on async*/


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
