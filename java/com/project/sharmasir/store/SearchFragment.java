package com.project.sharmasir.store;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String type[] =new String[]{"book","magazine"};
    final String bcategory[]={"fiction","sports","biography","educational","romantic","horror"};
    final String mcategory[]={"sports","educational","technology","fashion","lifestyle","women"};

    ListViewCompat slist;
    ArrayAdapter cardview;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_search, container, false);

        slist = (ListViewCompat)rootview.findViewById(R.id.slist);

        final Spinner stype = (Spinner)rootview.findViewById(R.id.stype);
        final Spinner scategory = (Spinner)rootview.findViewById(R.id.scategory);

        ArrayAdapter<String> astype = new ArrayAdapter<String>(rootview.getContext(),android.R.layout.simple_list_item_1,type);
        final ArrayAdapter<String> abcategory = new ArrayAdapter<String>(rootview.getContext(),android.R.layout.simple_list_item_1,bcategory);
        final ArrayAdapter<String> amcategory = new ArrayAdapter<String>(rootview.getContext(),android.R.layout.simple_list_item_1,mcategory);

        stype.setAdapter(astype);

        stype.setPrompt("Select Type");

        stype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    scategory.setAdapter(abcategory);
                }
                else{
                    scategory.setAdapter(amcategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        scategory.setPrompt("Select Category");
        //cardview = new MyBookRecyclerViewAdapter(new ArrayList<Book>(),getContext());

        scategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("Type",stype.getSelectedItem().toString()+":"+scategory.getSelectedItem().toString());
                if(stype.getSelectedItem().toString().equals("book")){
                    cardview = new MyBookRecyclerViewAdapter(new ArrayList<Book>(),getContext());
                    slist.setAdapter(cardview);

                    new BookViewLoader(stype.getSelectedItem().toString(),scategory.getSelectedItem().toString()).execute();

                }else{
                    cardview = new MyMagazineRecyclerViewAdapter(new ArrayList<Magazine>(),getContext());
                    slist.setAdapter(cardview);

                    new MagazineViewLoader(stype.getSelectedItem().toString(),scategory.getSelectedItem().toString()).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class BookViewLoader extends AsyncTask<Void, Void, List<Book>> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String type,category;
        BookViewLoader(String type, String category){
            this.type = type;
            this.category = category;
        }

        @Override
        protected void onPostExecute(List<Book> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(cardview instanceof MyBookRecyclerViewAdapter)
                ((MyBookRecyclerViewAdapter)cardview).setItemList(result);
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
            String POST_PARAMS ="option="+type;
            POST_PARAMS+="&category="+category;

            try {

                URL url = new URL("http://192.168.0.184/Reader'sParadise/searchCatagory.php");
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

    private class MagazineViewLoader extends AsyncTask<Void, Void, List<Magazine>> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String type,category;
        MagazineViewLoader(String type,String category){
            this.type = type;
            this.category = category;
        }

        @Override
        protected void onPostExecute(List<Magazine> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(cardview instanceof MyMagazineRecyclerViewAdapter)
                ((MyMagazineRecyclerViewAdapter)cardview).setItemList(result);
            cardview.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Retrieving Magazines...");
            dialog.show();
        }

        @Override
        protected List<Magazine> doInBackground(Void... params) {
            List<Magazine> result = new ArrayList<Magazine>();
            String line,response="";
            SharedPreferences myprefs= getActivity().getSharedPreferences("user", MODE_PRIVATE);
            String email= myprefs.getString("user", null);
            Log.e("User",email);
            String POST_PARAMS ="option="+type;
            POST_PARAMS+="&category="+category;

            try {

                URL url = new URL("http://192.168.0.184/Reader'sParadise/searchCatagory.php");
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
                        Magazine b = convertMagazine(object);
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

        private Magazine convertMagazine(JSONObject obj) throws JSONException {
            String name = obj.getString("mname");
            String ismn = obj.getString("msbn");
            String publisher = obj.getString("mpub");
            String edition = obj.getString("medition");
            String category = obj.getString("mcatagory");
            String url = obj.getString("mlink");
            float size = (float) obj.getInt("msize");

            Log.e("Book",ismn);
            return new Magazine(name,ismn,publisher,edition,category,url,size);
        }

    }
}
