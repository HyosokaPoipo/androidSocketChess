package poipo.hyosoka.com.socketchess;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    getServerData threadProcess;
    TextView myTest ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papan_gridlayout);
        myTest = (TextView) findViewById(R.id.testajjah);
        threadProcess = new getServerData("xinuc.org",7387);
        threadProcess.execute();

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
            //textResponse.setText(response);
            myTest.setText(response);
            getServerData test= new getServerData("xinuc.org",7387);
            test.execute();
            super.onPostExecute(result);
        }





    }//end of getServerData



}
