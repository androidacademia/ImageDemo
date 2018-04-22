package broadcast.androidacademia.com.imagedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDemo extends AppCompatActivity {
    private ImageView imageView;
    private String imageStr = "https://images.vexels.com/media/users/3/139556/isolated/preview/1718a076e29822051df8bcf8b5ce1124-android-logo-by-vexels.png";
    private ProgressBar progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_demo);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        
    }

    public void download(View view) {
        if (NetworkConnection.getConnection(this)){
            ImageTask imageTask = new ImageTask();
            imageTask.execute(imageStr);
            
        }else {
            Toast.makeText(this, "not connected....", Toast.LENGTH_SHORT).show();
        }
    }

    public class ImageTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            //logic here......
            Bitmap bitmap = null;
            String strUrl = strings[0];
            //user network operation....
            try {
                URL url = new URL(strUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.GONE);
            if (imageView !=null){
                imageView.setImageBitmap(bitmap);
            }

        }
    }
}
