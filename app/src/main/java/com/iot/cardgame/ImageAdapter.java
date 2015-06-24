package com.iot.cardgame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by iao on 15. 5. 28.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdaptor";
	private static final int BACK_IMAGE = R.drawable.backimg;
	private static final int CLICKED_IMAGE = R.drawable.clickedbackimg;

    private Context context;
    private DisplayMetrics mMetrics;
    private int row;
    private int col;

    public  boolean isStart = true;
    private boolean ismatching;
    private boolean[] isClicked;
    private boolean[] isOpened;
    private int selectedPos1 = -1;
    private int selectedPos2 = -1;

    private int[] resourceImages = {R.drawable.ddolgie, R.drawable.ddungee, R.drawable.hochi,
            R.drawable.shaechomi, R.drawable.drago, R.drawable.yorongee,
            R.drawable.macho, R.drawable.mimi, R.drawable.mongchi,
            R.drawable.kiki, R.drawable.kangdari, R.drawable.jjingjjingee};
    private int[] images;
    private int[] frontimages;



    private ImageView [] imageViews;
    private ImageView [] frontImageViews;

    private int score;

    public ImageAdapter(Context context, DisplayMetrics mMetrics, int cntCard) {
        Log.d(TAG, "카드 개수 : " + cntCard);
        this.context = context;
        this.mMetrics = mMetrics;
        this.images = new int[cntCard];
        this.frontimages = new int[cntCard];
        this.imageViews = new ImageView[cntCard];
        this.frontImageViews = new ImageView[cntCard];
        initImageViews(cntCard);
        this.isClicked = new boolean[cntCard];
        this.isOpened = new boolean[cntCard];



        for (int i = 0; i != images.length; ++i) {
            this.images[i] = BACK_IMAGE;
            this.isClicked[i] = false;
            this.isOpened[i] = false;
        }

        this.score = 0;

        initRandomImages(cntCard);
    }

    private void initImageViews(int cnt){
        for(int i = 0; i < cnt; ++i) {
            imageViews[i] = new ImageView(context);
            imageViews[i].setScaleType(ImageView.ScaleType.FIT_START);
            imageViews[i].setPadding(3, 3, 3, 3);

            frontImageViews[i] = new ImageView(context);
            frontImageViews[i].setScaleType(ImageView.ScaleType.FIT_START);
            frontImageViews[i].setPadding(3, 3, 3, 3);
            frontImageViews[i].setImageResource(frontimages[i]);
        }
    }

    private void initRandomImages(int cnt){
        int cntImage = cnt / 2;                              // 필요한 카드개수 1단계 3, 2단계 6, 3단계 12
        ArrayList allImages = new ArrayList();

	    for (int i = 0; i < resourceImages.length; ++i)     // array를 arraylist로 변경
		    allImages.add(resourceImages[i]);
	    Collections.shuffle(allImages);                      // 이미지 섞기

        Object [] tmp =  allImages.toArray(new Object[12]);
        for(int i = 0; i < resourceImages.length; ++i)       // 섞인 이미지 다시 배열에 추가
            resourceImages[i] = Integer.parseInt(tmp[i].toString());

        ArrayList imageIndex = new ArrayList();                 // index 랜덤 생성
        for (int i = 0; i < cnt; ++i)
            imageIndex.add(i);
        Collections.shuffle(imageIndex);

        for (int i = 0; i < cnt; ++i)                           // frontimage 생성
            frontimages[(int)imageIndex.get(i)] = resourceImages[i % cntImage];

        setRowCol(cnt);
    }

    private void setRowCol(int cnt){            // 카드 개수에 따라 열과 행을 설정
        if (cnt == 6){
            this.row = 2;
            this.col = 3;
        } else if (cnt == 12){
            this.row = 3;
            this.col = 4;
        } else if (cnt == 24){
            this.row = 4;
            this.col = 6;
        }
    }

        //
        public void itemClicked(int position){      // 카드가 눌렸을때
            Log.d(TAG, "itemClicked()");
            if (!isOpened[position]  && !ismatching && !isStart) {      // 오픈된 카드가 아닐경우
                if (isClicked[position]) {
                    isClicked[position] = false;
                    images[position] = BACK_IMAGE;
                    selectedPos1 = -1;
                } else {
                    isClicked[position] = true;
                    images[position] = CLICKED_IMAGE;

                    if (selectedPos1 == -1)
                        selectedPos1 = position;
                    else {
                        selectedPos2 = position;
                        ismatching = true;
                        openTwoCards();
                    }
                }

                this.notifyDataSetChanged();
            }
        }

        public void openTwoCards(){
            Log.d(TAG, "openTwoCards()");
            if (selectedPos1 != -1 && selectedPos2 != -1) {
                images[selectedPos1] = frontimages[selectedPos1];
                images[selectedPos2] = frontimages[selectedPos2];
                flipCard(selectedPos1, frontimages[selectedPos1]);
                flipCard(selectedPos2, frontimages[selectedPos2]);

                Log.i(TAG, "1 :    " + selectedPos1 + "    " + selectedPos2);
              //  flipCard(selectedPos1);
               // flipCard(selectedPos2);
                Log.i(TAG, "2 :    " + selectedPos1 + "    " + selectedPos2);
                this.notifyDataSetChanged();

                Log.d(TAG, "5cho");

                cardCompare();

            }
        }

    public void flipCard(final int position, final int destination){
        AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViews[position].setImageResource(destination);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.setTarget(imageViews[position]);
        animation.setDuration(500);
        animation.start();
    }

    public void reverseCard(final int position){
        AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViews[position].setImageResource(BACK_IMAGE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.setTarget(imageViews[position]);
        animation.setDuration(500);
        animation.start();
    }

        private void cardCompare(){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "****************** handler : " + selectedPos1 + selectedPos2);
                    if (isCardsMatched(frontimages[selectedPos1], frontimages[selectedPos2])) {
                        score += 10;
                        isOpened[selectedPos1] = true;
                        isOpened[selectedPos2] = true;
                    } else {
                        images[selectedPos1] = images[selectedPos2] = BACK_IMAGE;
                        reverseCard(selectedPos1);
                        reverseCard(selectedPos2);
                    }
                    ismatching = false;
                    clearSelectedCards();
                }
            }, 1000);

        }

        public boolean isCardsMatched(int img1, int img2){
            Log.d(TAG, "isCardsMatched()");
            if (img1 == img2) {
                return true;
            }
            return false;
        }


        public void clearSelectedCards(){
            Log.d(TAG, "clearSelectedCards()");
            for (int i = 0; i < isClicked.length; ++i)
                if (!isOpened[i] && isClicked[i]) {
                    isClicked[i] = false;
                }

            selectedPos1 = -1;
            selectedPos2 = -1;
           this.notifyDataSetChanged();
        }

    public int getScore(){
        Log.d(TAG, "Score : " + score);
        return score;
    }
        public boolean isGameClear(){
            for (int i = 0; i < isOpened.length; ++i){
                if (!isOpened[i])
                    return false;
            }
            return true;
        }

    public void destroy(){
        isClicked = null;
        isOpened = null;
        images = null;
        frontimages = null;
        imageViews = null;
        frontImageViews = null;
    }


    @Override
    public int getCount() {
        Log.d(TAG, "getCount()");
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        Log.d(TAG, "getItem()");
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId()");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int rowWidth = (mMetrics.widthPixels) / row;
        int colWidth = (mMetrics.heightPixels) / col;

       // Log.i(TAG, rowWidth + " " + colWidth);

        if (convertView == null) {
           // Log.d(TAG, "이미지뷰 생성");
            imageViews[position].setLayoutParams(new GridView.LayoutParams(rowWidth - 50, colWidth - 50));
            frontImageViews[position].setLayoutParams(new GridView.LayoutParams(rowWidth - 50, colWidth - 50));
        } else {
            //Log.d(TAG, "이미지뷰 불러오기");
            imageViews[position] = (ImageView) convertView;
        }

        if (isStart) {
           // Log.d(TAG, "start true");
            imageViews[position].setImageResource(frontimages[position]);
        }else {
            //Log.d(TAG, "start false - "+ (++cnt));
            imageViews[position].setImageResource(images[position]);
        }

        //imageViews[position].setImageResource(images[position]);
        return imageViews[position];
    }
}
