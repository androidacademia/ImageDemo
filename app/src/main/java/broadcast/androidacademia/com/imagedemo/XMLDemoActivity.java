package broadcast.androidacademia.com.imagedemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class XMLDemoActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmldemo);
        textView = findViewById(R.id.textViewOut);
    }

    public void download(View view) {
        if (NetworkConnection.getConnection(this)){
            //do the operation
            XMLTask xmlTask = new XMLTask();
            xmlTask.execute("https://www.w3schools.com/xml/note.xml");
        }else {
            Toast.makeText(this, "not connected....", Toast.LENGTH_SHORT).show();
        }
    }

    public class XMLTask extends AsyncTask<String,Void,Note>{

        @Override
        protected Note doInBackground(String... strings) {
            Note note = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                /////////////Conversion begin///////////////////////////////
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(inputStream,null);
                int eventType = xmlPullParser.getEventType();//START_DOCUMENT
                while (eventType!=XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            note = new Note();
                            break;
                        case XmlPullParser.START_TAG:
                            if (xmlPullParser.getName().equals("to")){
                                //xmlPullParser.getA
                                note.setTo(xmlPullParser.nextText());
                            }else if (xmlPullParser.getName().equals("from")){
                                note.setFrom(xmlPullParser.nextText());
                            }else if (xmlPullParser.getName().equals("heading")){
                                note.setHeading(xmlPullParser.nextText());
                            }else  if (xmlPullParser.getName().equals("body")){
                                note.setBody(xmlPullParser.nextText());
                            }
                            break;
                    }
                    eventType = xmlPullParser.next();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return note;
        }

        @Override
        protected void onPostExecute(Note note) {
            super.onPostExecute(note);

            if (note!=null){
                textView.setText("TO : "+note.getTo()+"\n" +
                        "from :"+note.getFrom()+"\n" +
                        "heading : "+note.getHeading()+"\n" +
                        "body : "+note.getBody());
            }

        }
    }
}
