package me.ykrank.s1next.widget.track;

import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ConcurrentHashMap;

import me.ykrank.s1next.data.User;
import me.ykrank.s1next.util.L;
import me.ykrank.s1next.widget.track.event.PageEndEvent;
import me.ykrank.s1next.widget.track.event.PageStartEvent;
import me.ykrank.s1next.widget.track.event.TrackEvent;
import me.ykrank.s1next.widget.track.talkingdata.TalkingDataAgent;
import me.ykrank.s1next.widget.track.trackhandler.DefaultTrackHandler;
import me.ykrank.s1next.widget.track.trackhandler.PageEndTrackHandler;
import me.ykrank.s1next.widget.track.trackhandler.PageStartTrackHandler;
import me.ykrank.s1next.widget.track.trackhandler.TrackHandler;

/**
 * Created by ykrank on 2016/12/27.
 */

public class DataTrackAgent {
    private static final String TAG = "DataTrackAgent";
    private static final String THREAD_NAME = "EventTrackThread";

    private HandlerThread mThread = new HandlerThread(THREAD_NAME);
    private Looper looper;
    private ConcurrentHashMap<Class, TrackHandler> handlerMapper = new ConcurrentHashMap<>();
    private TrackHandler<TrackEvent> defaultHandler;
    private User mUser;

    public void init() {
        mThread.start();
        looper = mThread.getLooper();

        TalkingDataAgent.init();
        setDefaultHandler(new DefaultTrackHandler());
        regHandler(PageStartEvent.class, new PageStartTrackHandler());
        regHandler(PageEndEvent.class, new PageEndTrackHandler());
    }

    public void setUser(User user) {
        if (mUser == null) {
            mUser = user;
            TalkingDataAgent.setUser(user);
        }
    }

    public <T> void regHandler(Class<T> eventType, TrackHandler<T> handler) {
        handlerMapper.put(eventType, handler);
    }

    public void setDefaultHandler(@Nullable TrackHandler<TrackEvent> handler) {
        if (handler != defaultHandler) {
            defaultHandler = handler;
        }
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull TrackEvent event) {
        TrackHandler handler = handlerMapper.get(event.getEventType());
        if (handler != null) {
            handler.track(looper, event);
        } else if (defaultHandler != null) {
            defaultHandler.track(looper, event);
        } else {
            L.report(new IllegalStateException("TrackEvent not handler:" + event));
        }
    }
}
