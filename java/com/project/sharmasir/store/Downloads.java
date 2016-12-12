package com.project.sharmasir.store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.sharmasir.store.dummy.DummyContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Downloads.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Downloads#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Downloads extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static List<File> epubs;
    static List<String> names;
    ArrayAdapter<String> adapter;
    static String selected;

    String folder_name;

    final private String TAG = "EPub";

    private OnFragmentInteractionListener mListener;

    public Downloads() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Downloads.
     */
    // TODO: Rename and change types and number of parameters
    public static Downloads newInstance(String param1, String param2) {
        Downloads fragment = new Downloads();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_downloads, container, false);

        SharedPreferences myprefs= getActivity().getSharedPreferences("user",MODE_PRIVATE);
        folder_name = myprefs.getString("username",null)+"_Books";

        if (!this.makeBooksDirectory()) {
            debug("faild to make books directory");
        }

        //installSamples();

        if ((epubs == null) || (epubs.size() == 0)) {
            epubs = epubList(new File(Environment.getExternalStorageDirectory()+ File.separator + folder_name));
        }

        ListView list = (ListView) rootView.findViewById(R.id.fileListView);
        names = fileNames(epubs);
        Log.e("++++++++++",names+"");
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, names);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView,
                                    int position, long itemId) {
                selected = names.get(position);
                Intent resultIntent = new Intent(getActivity(), BookViewActivity.class);
                // TODO: hardcoded string
                resultIntent.putExtra("name", selected);
                resultIntent.putExtra("folder", folder_name);
                Log.e("Name",selected);
                //setResult(Activity.RESULT_OK, resultIntent);
                startActivity(resultIntent);
            }
        });

        list.setAdapter(adapter);

        final SwipeRefreshLayout refresh = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                refreshList();
                refresh.setRefreshing(false);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //refreshList();
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

    private List<String> fileNames(List<File> files) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < files.size(); i++) {
            res.add(files.get(i).getName().replace(".epub", ""));
			/*
			 * NOTE: future
			res.add(files.get(i).getName().replace(".epub", "").replace(".e0", ""));
			*/
        }
        return res;
    }

    private List<File> epubList(File dir) {
        List<File> res = new ArrayList<File>();
        Log.e("True",dir.toString());
        if (dir.isDirectory()) {
            File[] f = dir.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    if (f[i].isDirectory()) {
                        res.addAll(epubList(f[i]));
                    } else {
                        String lowerCasedName = f[i].getName().toLowerCase();
                        if (lowerCasedName.endsWith(".epub")) {
                            res.add(f[i]);
                        }

						/*
						 * NOTE: future
						if ((lowerCasedName.endsWith(".epub"))
								|| (lowerCasedName.endsWith(".e0"))) {
							res.add(f[i]);
						}
						*/
                    }
                }
            }
        }
        return res;
    }

    private void refreshList() {
        epubs = epubList(new File(Environment.getExternalStorageDirectory()+ File.separator + folder_name));
        names.clear();
        names.addAll(fileNames(epubs));
        this.adapter.notifyDataSetChanged();
    }

    public void debug(String msg) {
        Log.d("EPub", msg);
    }

    public boolean makeBooksDirectory() {
//        boolean res;
//        String filePath = new String(getFilesDir().getAbsolutePath() + "/books");
//        File file = new File(filePath);
//        if (!file.exists()) {
//            res = file.mkdirs();
//        }else {
//            res = false;
//        }
//        return res;
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + folder_name);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        return success;

    }

    public boolean fileExists(String fileName) {
        boolean res;

        String pureName = this.removeExtention(fileName);
//        String targetDirectory = getFilesDir().getAbsolutePath() + "/books/"+pureName;
        String targetDirectory = Environment.getExternalStorageDirectory() + File.separator + folder_name + File.separator + pureName;
        String targetPath = targetDirectory+"/"+ fileName;

        File file = new File(targetPath);
        debug(file.getAbsolutePath());

        if (file.exists()) res = true;
        else  res = false;
        return res;
    }

    public boolean  deleteFile(String fileName) {
        boolean res;

        String pureName = this.removeExtention(fileName);
//        String targetDirectory = getFilesDir().getAbsolutePath() + "/books/"+pureName;
        String targetDirectory = Environment.getExternalStorageDirectory() + File.separator + folder_name;
        String targetPath = targetDirectory+"/"+ fileName;

        File file = new File(targetPath);
        res = file.delete();
        return res;
    }

    public String removeExtention(String filePath) {
        // These first few lines the same as Justin's
        File f = new File(filePath);

        // if it's a directory, don't remove the extention
        if (f.isDirectory()) return filePath;

        String name = f.getName();

        // Now we know it's a file - don't need to do any special hidden
        // checking or contains() checking because of:
        final int lastPeriodPos = name.lastIndexOf('.');
        if (lastPeriodPos <= 0)
        {
            // No period after first character - return name as it was passed in
            return filePath;
        }
        else
        {
            // Remove the last period and everything after it
            File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
            return renamed.getPath();
        }
    }

    public void copyToDevice(String fileName) {
        if (!this.fileExists(fileName)){
            try
            {
                String pureName = this.removeExtention(fileName);
//                String targetDirectory = getFilesDir().getAbsolutePath() + "/books/"+pureName;
                String targetDirectory = Environment.getExternalStorageDirectory() + File.separator + folder_name + File.separator + pureName;
                File dir = new File(targetDirectory);
                dir.mkdirs();
                String targetPath = targetDirectory+"/"+ fileName;

                InputStream localInputStream = getActivity().getAssets().open(fileName);
                FileOutputStream localFileOutputStream = new FileOutputStream(targetPath);

                byte[] arrayOfByte = new byte[1024];
                int offset;
                while ((offset = localInputStream.read(arrayOfByte))>0)
                {
                    localFileOutputStream.write(arrayOfByte, 0, offset);
                }
                localFileOutputStream.close();
                localInputStream.close();
                Log.d(TAG, fileName+" copied to phone");
            }
            catch (IOException localIOException)
            {
                localIOException.printStackTrace();
                Log.d(TAG, "failed to copy");
                return;
            }
        }
        else {
            Log.d(TAG, fileName+" already exist");
        }
    }

    private void installBook(String fileName) {
        if (this.fileExists(fileName)){
            Log.d(TAG, fileName+ " already exist. try to delete old file.");
            this.deleteFile(fileName);
        }
        this.copyToDevice(fileName);

//        File from = new File(fileName);
//        File to = new File(Environment.getExternalStorageDirectory() + File.separator + "Books" + File.separator + fileName);
//        from.renameTo(to);
    }

    private void installSamples() {
        this.installBook("Alice.epub");
    }

    private void startBookViewActivity() {
        Intent intent = new Intent(getActivity(),BookViewActivity.class);
        startActivity(intent);
    }


    private View.OnClickListener listener=new View.OnClickListener(){
        @Override
        public void onClick(View arg) {
            if (arg.getId()==new Integer(8080)) {
                startBookViewActivity();
            }else if (arg.getId()==new Integer(8082)) {
                installSamples();
            }else if (arg.getId()==new Integer(8083)) {

            }

        }
    };
}
