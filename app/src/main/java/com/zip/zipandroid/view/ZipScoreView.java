package com.zip.zipandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ConvertUtils;
import com.zip.zipandroid.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ZipScoreView extends LinearLayout implements View.OnClickListener {
    /**
     * 两个动画之间的间隔时间
     */
    private final int ANIMATOR_INTERVAL = 150;
    private List<ImageView> viewList = new ArrayList<>();
    private Context mContext;
    private int svgaWidth = ConvertUtils.dp2px(30);
    private int svgaMargin = ConvertUtils.dp2px(6);
    private boolean clickable = true;
    private int score;
    private int selectIv = R.drawable.whole_star_39;
    private int unSelectIv = R.drawable.none_star_40;

    public ZipScoreView(Context context) {
        this(context, null);
    }

    public ZipScoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZipScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.zipScoreView);
        svgaWidth = (int) typedArray.getDimension(R.styleable.zipScoreView_itemScoreWidth, ConvertUtils.dp2px(32));
        selectIv = (int) typedArray.getResourceId(R.styleable.zipScoreView_scoreSelectIv, R.drawable.whole_star_39);
        unSelectIv = (int) typedArray.getResourceId(R.styleable.zipScoreView_scoreUnSelectIv, R.drawable.none_star_40);
        svgaMargin = (int) typedArray.getDimension(R.styleable.zipScoreView_itemStarMargin, ConvertUtils.dp2px(7));
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        viewList.add(addSvgaView());
        viewList.add(addSvgaView());
        viewList.add(addSvgaView());
        viewList.add(addSvgaView());
        viewList.add(addSvgaView());
    }


    private ImageView addSvgaView() {
        ImageView svgaView = new ImageView(mContext);
        LayoutParams params = new LayoutParams(svgaWidth, svgaWidth);
        params.setMargins(svgaMargin, 0, svgaMargin, 0);
        svgaView.setLayoutParams(params);
//        svgaView.setImageResource(R.mipmap.game_star_none_icon);
//        svgaView.setImageResource(R.mipmap.none_star_40);
        svgaView.setImageDrawable(ContextCompat.getDrawable(getContext(), unSelectIv));
        addView(svgaView);
        svgaView.setOnClickListener(this);
        return svgaView;
    }


    public void reset() {
        for (ImageView view : viewList) {
//            ChangeSkinType.IMAGE_SRC.apply(view, unSelectIv);
            view.setImageDrawable(ContextCompat.getDrawable(getContext(), unSelectIv));
//            view.setImageResource(R.mipmap.game_star_none_icon);
        }
    }


    /**
     * 展示分数
     *
     * @param score
     * @param hideMore 隐藏多余灰色分数
     */
    public void justShowScore(int score, boolean hideMore) {
        if (score > 10 || score < 0) {
            return;
        }
        this.score = score;
        reset();
        int starNum = score / 2;
        for (int i = 0; i < viewList.size(); i++) {
            if (i < starNum) {
//                ChangeSkinType.IMAGE_SRC.apply(viewList.get(i), selectIv);
                viewList.get(i).setImageDrawable(ContextCompat.getDrawable(getContext(), selectIv));
//                viewList.get(i).setImageResource(R.mipmap.game_star_whole_icon);
                viewList.get(i).setVisibility(VISIBLE);
            } else {
                if (hideMore) {
                    viewList.get(i).setVisibility(INVISIBLE);
                }
            }
        }
    }

    /**
     * 展示分数
     *
     * @param score
     * @param hideMore 隐藏多余灰色分数
     */
    public void showScore(int score, boolean hideMore) {
        if (score > 10 || score < 0) {
            return;
        }
        this.score = score;
        reset();
        int starNum = score / 2;
        for (int i = 0; i < viewList.size(); i++) {
            if (i < starNum) {
//                ChangeSkinType.IMAGE_SRC.apply(viewList.get(i), selectIv);
                viewList.get(i).setImageDrawable(ContextCompat.getDrawable(getContext(), selectIv));
//                viewList.get(i).setImageResource(R.mipmap.game_star_whole_icon);
                viewList.get(i).setVisibility(VISIBLE);
            } else {
                if (hideMore) {
                    viewList.get(i).setVisibility(INVISIBLE);
                }
            }
        }
        clickable = false;
    }

    int index = 0;
    Disposable disposable;

    @Override
    protected void onDetachedFromWindow() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        if (clickable) {
            int index = viewList.indexOf(v);
            showScore(score = 2 * (index + 1), false);
            clickable = true;
        }
        if (listener != null) {
            listener.scoreFinish(score);
        }
    }

    public int getScore() {
        return score;
    }

    private OnScoreSelectListener listener;

    public void setListener(OnScoreSelectListener listener) {
        this.listener = listener;
    }

    public interface OnScoreSelectListener {
        void scoreFinish(int score);
    }
}
