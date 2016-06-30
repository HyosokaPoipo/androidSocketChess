package poipo.hyosoka.com.socketchess;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map<String, Integer> mapID = new HashMap<String,Integer>();
    getServerData threadProcess;
    TextView myTest ;
    boolean status = false, isRunning = false;
    TextView bidakTV;
    checkInternetStatus check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papan_gridlayout);
        setID();
        myTest = (TextView) findViewById(R.id.testajjah);
        check = new checkInternetStatus();
        check.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start) {
            if(isRunning)
            {
                Toast.makeText(getApplicationContext(),"Socket is already running",Toast.LENGTH_SHORT).show();
            }else {
                getServerData th = new getServerData("xinuc.org",7387);
                th.execute();
                isRunning = true;
                status = true;
            }
            return true;
        }else if(id == R.id.action_stop)
        {
            isRunning = false;
            status = false;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class getServerData extends AsyncTask<Void,Void,Void>
    {

        String dstAddress;
        int destPort;
        String response = " ";

        getServerData(String addr, int port)
        {
            dstAddress = addr;
            destPort = port;
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            Socket socket = null;

            try {
                socket = new Socket(dstAddress, destPort);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                //inputStream.read() akan terblock bila tidak ada respond data
                while((bytesRead = inputStream.read(buffer)) != -1)
                {
                    if(bytesRead < 0)
                    {
                        //Toast.makeText(getApplicationContext(), "null respon from server", Toast.LENGTH_SHORT).show();
                      break;}

                    Log.i("while", "di dalam while");
                    Log.i("bytesRead ", Integer.toString(bytesRead));
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString();
                    Log.i("response ", response);
                    break;
                }


                Log.i("while", "di luar while");

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                Log.i("doInBackground", "UnknownHostException"+e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("doInBackground", "IOException"+e.toString());
            }finally
            {
                if(socket != null)
                {
                    try {
                        socket.close();
                        Log.i("doInBackground", "Closing socket....");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        //Log.i("doInBackground", "IOException for socket.close() "+e.toString());
                    }
                }
            }


            return null;
        }//end of doInBackground


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            Log.i("onPostExecute**** ", response);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resetBidak();
            myTest.setText(response);
            setBidak(response);

            if(status) {
                    getServerData test = new getServerData("xinuc.org", 7387);
                    test.execute();
                    isRunning = true;
            }else
            {
                status = false;
                isRunning = false;
                Toast.makeText(getApplicationContext(),"Thread stopped", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }





    }//end of getServerData



    private void setBidak(String key)
    {
        String[] data = key.trim().split(" ");
        for(int i=0; i<data.length; i++)
        {
            bidakTV = null;
            bidakTV = (TextView)findViewById(Integer.parseInt(mapID.get(data[i].substring(1)).toString()));
            switch (data[i].charAt(0))
            {
                case 'K': //white king
                        bidakTV.setBackgroundResource(R.drawable.white_king);
                    break;
                case 'Q': //white queen
                    bidakTV.setBackgroundResource(R.drawable.white_queen);
                    break;
                case 'B': //white bishop
                    bidakTV.setBackgroundResource(R.drawable.white_bishop);
                    break;
                case 'N' : //white knight
                    bidakTV.setBackgroundResource(R.drawable.white_knight);
                    break;
                case 'R': //white rook
                    bidakTV.setBackgroundResource(R.drawable.white_rook);
                    break;
                case 'k' : //black king
                    bidakTV.setBackgroundResource(R.drawable.black_king);
                    break;
                case 'q' : //black queen
                    bidakTV.setBackgroundResource(R.drawable.black_queen);
                    break;
                case 'b' : //black bishop
                    bidakTV.setBackgroundResource(R.drawable.black_bishop);
                    break;
                case 'n' : //black knight
                    bidakTV.setBackgroundResource(R.drawable.black_knight);
                    break;
                case 'r' : //black rook
                    bidakTV.setBackgroundResource(R.drawable.black_rook);
                    break;
                default :
                    Toast.makeText(getApplicationContext(),"Undefined Bidak : "+data[i],Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }



    private void setID()
    {
        mapID.put("a1",R.id.bidak1a);
        mapID.put("a2",R.id.bidak2a);
        mapID.put("a3",R.id.bidak3a);
        mapID.put("a4",R.id.bidak4a);
        mapID.put("a5",R.id.bidak5a);
        mapID.put("a6",R.id.bidak6a);
        mapID.put("a7",R.id.bidak7a);
        mapID.put("a8",R.id.bidak8a);

        mapID.put("b1",R.id.bidak1b);
        mapID.put("b2",R.id.bidak2b);
        mapID.put("b3",R.id.bidak3b);
        mapID.put("b4",R.id.bidak4b);
        mapID.put("b5",R.id.bidak5b);
        mapID.put("b6",R.id.bidak6b);
        mapID.put("b7",R.id.bidak7b);
        mapID.put("b8",R.id.bidak8b);

        mapID.put("c1",R.id.bidak1c);
        mapID.put("c2",R.id.bidak2c);
        mapID.put("c3",R.id.bidak3c);
        mapID.put("c4",R.id.bidak4c);
        mapID.put("c5",R.id.bidak5c);
        mapID.put("c6",R.id.bidak6c);
        mapID.put("c7",R.id.bidak7c);
        mapID.put("c8",R.id.bidak8c);

        mapID.put("d1",R.id.bidak1d);
        mapID.put("d2",R.id.bidak2d);
        mapID.put("d3",R.id.bidak3d);
        mapID.put("d4",R.id.bidak4d);
        mapID.put("d5",R.id.bidak5d);
        mapID.put("d6",R.id.bidak6d);
        mapID.put("d7",R.id.bidak7d);
        mapID.put("d8",R.id.bidak8d);


        mapID.put("e1",R.id.bidak1e);
        mapID.put("e2",R.id.bidak2e);
        mapID.put("e3",R.id.bidak3e);
        mapID.put("e4",R.id.bidak4e);
        mapID.put("e5",R.id.bidak5e);
        mapID.put("e6",R.id.bidak6e);
        mapID.put("e7",R.id.bidak7e);
        mapID.put("e8",R.id.bidak8e);


        mapID.put("f1",R.id.bidak1f);
        mapID.put("f2",R.id.bidak2f);
        mapID.put("f3",R.id.bidak3f);
        mapID.put("f4",R.id.bidak4f);
        mapID.put("f5",R.id.bidak5f);
        mapID.put("f6",R.id.bidak6f);
        mapID.put("f7",R.id.bidak7f);
        mapID.put("f8",R.id.bidak8f);

        mapID.put("g1",R.id.bidak1g);
        mapID.put("g2",R.id.bidak2g);
        mapID.put("g3",R.id.bidak3g);
        mapID.put("g4",R.id.bidak4g);
        mapID.put("g5",R.id.bidak5g);
        mapID.put("g6",R.id.bidak6g);
        mapID.put("g7",R.id.bidak7g);
        mapID.put("g8",R.id.bidak8g);


        mapID.put("h1",R.id.bidak1h);
        mapID.put("h2",R.id.bidak2h);
        mapID.put("h3",R.id.bidak3h);
        mapID.put("h4",R.id.bidak4h);
        mapID.put("h5",R.id.bidak5h);
        mapID.put("h6",R.id.bidak6h);
        mapID.put("h7",R.id.bidak7h);
        mapID.put("h8",R.id.bidak8h);
    }


    private void resetBidak()
    {
        for(Map.Entry<String, Integer> entry: mapID.entrySet())
        {
            bidakTV = (TextView)findViewById(entry.getValue());
            bidakTV.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    public boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(900);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (java.net.SocketTimeoutException e) {
            return false;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }


    private  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("", "Error checking internet connection", e);
            }
        } else {
            Log.d("", "No network available!");
        }
        return false;
    }

    private class checkInternetStatus extends  AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            if(isInternetWorking())
            {
                status = true;
                Log.i("StatusCheckInternetCo","Status True");
            }
            Log.i("StatusCheckInternetCo","Status False");
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Void result) {
            if(status)
            {
                threadProcess = new getServerData("xinuc.org", 7387);
                threadProcess.execute();
                isRunning = true;
            }else
            {
                myTest.setText("Please connect your smartphone to internet...");
                Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
