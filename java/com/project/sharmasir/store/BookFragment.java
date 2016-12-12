package com.project.sharmasir.store;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.sharmasir.store.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    MyBookRecyclerViewAdapter cardview;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookFragment newInstance(int columnCount) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootview = inflater.inflate(R.layout.fragment_book_list, container, false);

        cardview = new MyBookRecyclerViewAdapter(new ArrayList<Book>(),getActivity());
        ListViewCompat list = (ListViewCompat) rootview.findViewById(R.id.list);
        list.setAdapter(cardview);

        new AsyncListViewLoader().execute();

        return rootview;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    private class AsyncListViewLoader extends AsyncTask<Void, Void, List<Book>> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPostExecute(List<Book> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            cardview.setItemList(result);
            cardview.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Retrieving Books...");
            dialog.show();
        }

        @Override
        protected List<Book> doInBackground(Void... params) {
            List<Book> result = new ArrayList<Book>();
            String line,response="";
            SharedPreferences myprefs= getActivity().getSharedPreferences("user", MODE_PRIVATE);
            String email= myprefs.getString("user", null);
            Log.e("User",email);
            String POST_PARAMS ="email="+email;

            try {

                URL url = new URL("http://192.168.0.184/Reader'sParadise/prefFetchBook.php");
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
                    Log.e("RESPONSE", response);

                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object=arr.getJSONObject(i);
                        Log.e("Object",object.toString());
                        Book b = convertBook(object);
                        Log.e("Output",b.toString());
                        result.add(b);

                    }
                }
                //System.out.println(response);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        private Book convertBook(JSONObject obj) throws JSONException {
            String name = obj.getString("bname");
            String isbn = obj.getString("isbn");
            String author = obj.getString("bauth");
            String publisher = obj.getString("bpub");
            String edition = obj.getString("bedition");
            String category = obj.getString("bcatagory");
            String url = obj.getString("blink");
            float size = (float) obj.getInt("bsize");

            Log.e("Book",isbn);
            return new Book(name,isbn,author,publisher,edition,category,url,size);
        }

    }
}
