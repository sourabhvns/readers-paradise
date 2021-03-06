package com.project.sharmasir.store;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.sharmasir.store.dummy.DummyContent.DummyItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBookRecyclerViewAdapter extends ArrayAdapter<Book> {

    private List<Book> mValues;
    Context context;

    DownloadManager downloadManager;
    private BroadcastReceiver recieverDownloadComplete;
    long downloadReference;
    //private final OnListFragmentInteractionListener mListener;

    public MyBookRecyclerViewAdapter(List<Book> items, Context context) {
        super(context,R.layout.fragment_book,items);
        mValues = items;
        this.context = context;
    }

    public int getCount() {
        if (mValues != null)
            return mValues.size();
        return 0;
    }

    public Book getItem(int position) {
        if (mValues != null)
            return mValues.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (mValues != null)
            return mValues.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.fragment_book, null);
        }

        Log.e("Size",mValues.size()+"");

        final Book c = mValues.get(position);
        TextView name = (TextView) v.findViewById(R.id.bookname);
        name.setText(c.getName());

        TextView publisher = (TextView) v.findViewById(R.id.publisher);
        publisher.setText(c.getPublisher());

        TextView category = (TextView) v.findViewById(R.id.category);
        category.setText(c.getCategory());

        TextView edition = (TextView) v.findViewById(R.id.edition);
        edition.setText(c.getEdition());

        TextView author = (TextView) v.findViewById(R.id.author);
        author.setText(c.getAuthor());

        TextView size = (TextView) v.findViewById(R.id.size);
        size.setText(c.getSize()+"");

        Button download = (Button) v.findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri file = Uri.parse("http://192.168.0.184/Reader'sParadise/"+c.getUrl());
                DownloadData(file,v,c.getName());
                new BookCheck(c.getIsbn()).execute();

            }
        });



        return v;
    }

    private long DownloadData(Uri uri, View v,String file){


        // Create request for android download manager
        downloadManager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Data Download");

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");
        SharedPreferences myprefs= context.getSharedPreferences("user",MODE_PRIVATE);
        String folder_name = myprefs.getString("username",null)+"_Books";

        //Set the local destination for the downloaded file to a path
        //within the application's external files directory
        request.setDestinationInExternalPublicDir(folder_name,file+".epub" );
        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);


        return downloadReference;
    }
    public List<Book> getItemList() {
        return mValues;
    }

    public void setItemList(List<Book> itemList) {
        this.mValues = itemList;
    }

    class BookCheck extends AsyncTask<String,Void,String> {

        String email,isbn;

        public BookCheck(String isbn) {
            this.isbn=isbn;
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences myprefs= getContext().getSharedPreferences("user", MODE_PRIVATE);
            email= myprefs.getString("user", null);
            String line, response = "";
            String POST_PARAMS ="email="+email ;
            POST_PARAMS+="&isbn="+isbn ;

            Log.e("Parameters",POST_PARAMS);

            try {

                URL url = new URL("http://192.168.0.184/Reader'sParadise/isbnStore.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                    //conn.setRequestProperty("Connection", "close");
                }
                //conn.setRequestProperty("Accept-Encoding", "");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);

                // For POST only - BEGIN
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(POST_PARAMS.getBytes());
                os.flush();
                os.close();
                // For POST only - END
                //conn.connect();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
// read the response
                    System.out.println("Response Code: " + conn.getResponseCode());
                    //InputStream in = new BufferedInputStream(conn.getInputStream());

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                }

                Log.e("RESPONSE",response+"----------");
                //System.out.println(response);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }


        protected void onPostExecute(String result) {
            if(result.equals("1")) {
                Toast.makeText(getContext(), "Book saved!!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Error!!!", Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

}
