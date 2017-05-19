package com.example.csci3310gp28.stalkyourfds;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.example.csci3310gp28.stalkyourfds.multi.MultiListAdapter;
import com.example.csci3310gp28.stalkyourfds.multi.MultiPnCallback;
import com.example.csci3310gp28.stalkyourfds.presence.PresenceListAdapter;
import com.example.csci3310gp28.stalkyourfds.presence.PresencePnCallback;
import com.example.csci3310gp28.stalkyourfds.presence.PresencePojo;
import com.example.csci3310gp28.stalkyourfds.pubsub.PubSubListAdapter;
import com.example.csci3310gp28.stalkyourfds.pubsub.PubSubPnCallback;
import com.example.csci3310gp28.stalkyourfds.util.DateTimeUtil;
import com.example.csci3310gp28.stalkyourfds.util.JsonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatroomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentActivity fa;

    private static final String TAG = MainActivity.class.getName();
    private static final List<String> MULTI_CHANNELS = Arrays.asList(Constants.MULTI_CHANNEL_NAMES.split(","));
    public static final List<String> PUBSUB_CHANNEL = Arrays.asList(Constants.CHANNEL_NAME);

    private ScheduledExecutorService mScheduleTaskExecutor;

    private PubNub mPubnub_DataStream;
    private PubSubListAdapter mPubSub;
    private PubSubPnCallback mPubSubPnCallback;

    private PresenceListAdapter mPresence;
    private PresencePnCallback mPresencePnCallback;

    private PubNub mPubnub_Multi;
    private MultiListAdapter mMulti;
    private MultiPnCallback mMultiPnCallback;

    private SharedPreferences mSharedPrefs;
    private String mUsername;
    private Random random = new Random();

    private EditText mMessage;
    private ViewPager viewPager;

    public ChatroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatroomFragment newInstance(String param1, String param2) {
        ChatroomFragment fragment = new ChatroomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefs = getActivity().getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE);

        //this.mUsername = mSharedPrefs.getString(Constants.DATASTREAM_UUID, "");
        this.mUsername = "testing";
        this.mPubSub = new PubSubListAdapter(getContext());
        this.mPresence = new PresenceListAdapter(getContext());
        this.mMulti = new MultiListAdapter(getContext());

        this.mPubSubPnCallback = new PubSubPnCallback(this.mPubSub);
        this.mPresencePnCallback = new PresencePnCallback(this.mPresence);
        this.mMultiPnCallback = new MultiPnCallback(this.mMulti);

           }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_chatroom, container, false);
        Button button = (Button) rootView.findViewById(R.id.sendButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                publish(rootView);
            }
        });
        fa = super.getActivity();

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        //tabLayout = view.findViewById(R.id.tab_layout);
        mMessage = (EditText) view.findViewById(R.id.new_message);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        final MainActivityTabManager adapter = new MainActivityTabManager
                (getFragmentManager(), 1);

        adapter.setPubSubAdapter(this.mPubSub);
//        adapter.setPresenceAdapter(this.mPresence);
//        adapter.setMultiAdapter(this.mMulti);

//        viewPager.setAdapter(adapter);
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        initPubNub();
        initChannels();
    }

    public void publish(View view) {
        //final EditText mMessage = (EditText) ChatActivity.this.findViewById(R.id.new_message);

        final Map<String, String> message = ImmutableMap.<String, String>of("sender", this.mUsername, "message", mMessage.getText().toString(), "timestamp", DateTimeUtil.getTimeStampUtc());

        this.mPubnub_DataStream.publish().channel(Constants.CHANNEL_NAME).message(message).async(
                new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        try {
                            if (!status.isError()) {
                                mMessage.setText("");
                                Log.v(TAG, "publish(" + JsonUtil.asJson(result) + ")");
                            } else {
                                Log.v(TAG, "publishErr(" + JsonUtil.asJson(status) + ")");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }

    private final void initPubNub() {
        PNConfiguration config = new PNConfiguration();

        config.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        config.setSubscribeKey(Constants.PUBNUB_SUBSCRIBE_KEY);
        config.setUuid(this.mUsername);
        config.setSecure(true);

        this.mPubnub_DataStream = new PubNub(config);
        this.mPubnub_Multi = new PubNub(config);
    }

    private final void initChannels() {
        this.mPubnub_DataStream.addListener(this.mPubSubPnCallback);
        this.mPubnub_DataStream.addListener(this.mPresencePnCallback);

        this.mPubnub_DataStream.subscribe().channels(PUBSUB_CHANNEL).withPresence().execute();
        this.mPubnub_DataStream.hereNow().channels(PUBSUB_CHANNEL).async(new PNCallback<PNHereNowResult>() {
            @Override
            public void onResponse(PNHereNowResult result, PNStatus status) {
                if (status.isError()) {
                    return;
                }

                try {
                    Log.v(TAG, JsonUtil.asJson(result));

                    for (Map.Entry<String, PNHereNowChannelData> entry : result.getChannels().entrySet()) {
                        for (PNHereNowOccupantData occupant : entry.getValue().getOccupants()) {
                            mPresence.add(new PresencePojo(occupant.getUuid(), "join", DateTimeUtil.getTimeStampUtc()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.mPubnub_Multi.addListener(mMultiPnCallback);
        this.mPubnub_Multi.subscribe().channels(MULTI_CHANNELS).execute();


        this.mScheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        this.mScheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int randomIndex = random.nextInt(MULTI_CHANNELS.size());
                        String toChannel = MULTI_CHANNELS.get(randomIndex);

                        final Map<String, String> message = ImmutableMap.<String, String>of("sender", mUsername, "message", "randomly fired on this channel", "timestamp", DateTimeUtil.getTimeStampUtc());

                        mPubnub_Multi.publish().channel(toChannel).message(message).async(
                                new PNCallback<PNPublishResult>() {
                                    @Override
                                    public void onResponse(PNPublishResult result, PNStatus status) {
                                        try {
                                            if (!status.isError()) {
                                                Log.v(TAG, "publish(" + JsonUtil.asJson(result) + ")");
                                            } else {
                                                Log.v(TAG, "publishErr(" + JsonUtil.asJson(status) + ")");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                    }
                });

            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    private void disconnectAndCleanup() {
        getActivity().getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE).edit().clear().commit();

        if (this.mPubnub_DataStream != null) {
            this.mPubnub_DataStream.unsubscribe().channels(PUBSUB_CHANNEL).execute();
            this.mPubnub_DataStream.removeListener(this.mPubSubPnCallback);
            this.mPubnub_DataStream.removeListener(this.mPresencePnCallback);
            this.mPubnub_DataStream.stop();
            this.mPubnub_DataStream = null;
        }

        if (this.mPubnub_Multi != null) {
            this.mPubnub_Multi.unsubscribe().channels(MULTI_CHANNELS).execute();
            this.mPubnub_Multi.removeListener(this.mMultiPnCallback);
            this.mPubnub_Multi.stop();
            this.mPubnub_Multi = null;
        }

        if (this.mScheduleTaskExecutor != null) {
            this.mScheduleTaskExecutor.shutdownNow();
            this.mScheduleTaskExecutor = null;
        }
    }


}
