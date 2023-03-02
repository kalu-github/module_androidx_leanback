package lib.kalu.leanback.presenter.bean;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class TvEpisodesItemBean implements Serializable {

    public TvEpisodesItemBean() {
    }

    private int start;
    private int end;
    private boolean checked = false; // 是否正在选中
    private boolean playing = false; // 是否正在播放

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
