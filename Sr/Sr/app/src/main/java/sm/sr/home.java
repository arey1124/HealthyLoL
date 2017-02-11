package sm.sr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import org.json.JSONArray;
import org.json.JSONObject;

public class home extends AppCompatActivity {

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        UpdateTask a=new UpdateTask();
        String text =
                "I know the times are difficult! Our sales have been "
                        + "disappointing for the past three quarters for our data analytics "
                        + "product suite. We have a competitive data analytics product "
                        + "suite in the industry. But we need to do our job selling it! "
                        + "We need to acknowledge and fix our sales challenges. "
                        + "We can’t blame the economy for our lack of execution! "
                        + "We are missing critical sales opportunities. "
                        + "Our product is in no way inferior to the competitor products. "
                        + "Our clients are hungry for analytical tools to improve their "
                        + "business outcomes. Economy has nothing to do with it.";
        a.execute(text);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

     class UpdateTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
            service.setUsernameAndPassword("51db7ecd-ad14-43ff-818e-945c1a98ec42", "RfhDLxdwM1Ys");
            ToneAnalysis tone = service.getTone(params[0], null).execute();
            Log.e("E_",tone.toString());

            result=tone.toString();

            try {
                JSONObject o = new JSONObject(result);
                JSONObject document_tone = o.getJSONObject("document_tone");

                JSONArray tone_categories = document_tone.getJSONArray("tone_categories");
                JSONArray tones = tone_categories.getJSONObject(0).getJSONArray("tones");

                for(int i=0;i<tones.length();i++)
                {
                    JSONObject temp=tones.getJSONObject(i);
                    String tone_name=temp.getString("tone_name");
                    String score=temp.getString("score");
                    Log.e(tone_name,score);
                }

                tones = tone_categories.getJSONObject(1).getJSONArray("tones");

                for(int i=0;i<tones.length();i++)
                {
                    JSONObject temp=tones.getJSONObject(i);
                    String tone_name=temp.getString("tone_name");
                    String score=temp.getString("score");
                    Log.e(tone_name,score);
                }
                tones = tone_categories.getJSONObject(2).getJSONArray("tones");
                for(int i=0;i<tones.length();i++)
                {
                    JSONObject temp=tones.getJSONObject(i);
                    String tone_name=temp.getString("tone_name");
                    String score=temp.getString("score");
                    Log.e(tone_name,score);
                }








                //Log.e("gg",sds);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            //System.out.println(tone);
            return null;
        }
    }
}


