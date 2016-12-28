package me.ykrank.s1next.widget.track.event;

/**
 * Created by ykrank on 2016/12/28.
 * 页面打开事件
 */

public class PageStartEvent extends TrackEvent {
    private String pageName;

    public PageStartEvent(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
