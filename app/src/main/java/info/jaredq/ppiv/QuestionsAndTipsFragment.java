package info.jaredq.ppiv;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import info.jaredq.ppiv.dummy.DummyContent;
import info.jaredq.ppiv.models.Forum;
import info.jaredq.ppiv.models.ForumHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class QuestionsAndTipsFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    /**
     * The AsyncTask for getting forum list
     */
    private AsyncTask mGetForumListAT;

    /**
     * The list of forum
     */
    private List<Forum> mForumList;

    /**
     * @return
     */
    public static QuestionsAndTipsFragment newInstance() {
        QuestionsAndTipsFragment fragment = new QuestionsAndTipsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static QuestionsAndTipsFragment newInstance(int sectionNumber) {
        QuestionsAndTipsFragment fragment = new QuestionsAndTipsFragment();
        Bundle args = new Bundle();
        args.putInt(MainFragmentHelper.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionsAndTipsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // TODO reed arguments

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions_and_tips, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mGetForumListAT = new GetForumListAT().execute(Forum.CATEGORY_ID_QUESTIONS);

//        List<Forum> items = ForumHelper.getForumList(Forum.CATEGORY_ID_QUESTIONS);
//        mAdapter = new ArrayAdapter<Forum>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, items);
//        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;

            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(MainFragmentHelper.ARG_SECTION_NUMBER));
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onForumClick(mForumList.get(position).getFid());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    private class GetForumListAT extends AsyncTask<Integer, Integer, List<Forum>> {

        @Override
        protected List<Forum> doInBackground(Integer... params) {
            mForumList = ForumHelper.getForumList(params[0]);

            return mForumList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<Forum> o) {
            super.onPostExecute(o);

            List<Forum> items = (List<Forum>) o;
            mAdapter = new ArrayAdapter<Forum>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, items);

            ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        }
    }
}
