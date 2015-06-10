package com.iot.cardgame;

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
	private static final int IMAGE_IS_MACHED = 1;
	private static final int IMAGE_IS_NOT_MACHED = 0;

    public static boolean isStart = true;

    private static Context context;
    private DisplayMetrics mMetrics;
    private int row;
    private int col;

    private static int[] imges;
    private int[] resourceImages = {R.drawable.ddolgie, R.drawable.ddungee, R.drawable.hochi,
            R.drawable.shaechomi, R.drawable.drago, R.drawable.yorongee,
            R.drawable.macho, R.drawable.mimi, R.drawable.mongchi,
            R.drawable.kiki, R.drawable.kangdari, R.drawable.jjingjjingee};
    private int[] frontimages;

	private static boolean ismatching;
    private static boolean[] isClicked;
    private static boolean[] isOpened;
    private static int selectedPos1 = -1;
    private static int selectedPos2 = -1;

    private ImageView [] imageViews;
    private ImageView [] frontImageViews;

    private int score;
    int cnt;

    public ImageAdapter(Context context, DisplayMetrics mMetrics, int cntCard) {
        this.context = context;
        this.mMetrics = mMetrics;
        this.imges = new int[cntCard];
        this.frontimages = new int[cntCard];
        this.imageViews = new ImageView[cntCard];
        this.frontImageViews = new ImageView[cntCard];
        initImageViews(cntCard);
        this.isClicked = new boolean[cntCard];
        this.isOpened = new boolean[cntCard];

        for (int i = 0; i != imges.length; ++i) {
            this.imges[i] = BACK_IMAGE;
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
                    imges[position] = BACK_IMAGE;
                    selectedPos1 = -1;
                } else {
                    isClicked[position] = true;
                    imges[position] = CLICKED_IMAGE;

                    if (selectedPos1 == -1)
                        selectedPos1 = position;
                    else {
                        selectedPos2 = position;
                        ismatching = true;
                        openTwoCards();
                    }
                }

               // this.notifyDataSetChanged();
            }
        }

        private void flipCard(int position){
            Log.d(TAG, "flipCard()");
            View cardFace = imageViews[position];
            View cardBack = frontImageViews[position];

            FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

            if (cardFace.getVisibility() == View.GONE)
            {
                Log.d(TAG, "----------------------------------reverse()");
                flipAnimation.reverse();
            }
            //rootLayout.startAnimation(flipAnimation);
            cardFace.startAnimation(flipAnimation);
            //this.notifyDataSetChanged();
        }

    private void flipCard2(int position){
        Log.d(TAG, "flipCard()");
        View cardFace = frontImageViews[position];
        View cardBack = imageViews[position];

        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        try {
            Thread.sleep(500);
            flipAnimation.reverse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cardFace.startAnimation(flipAnimation);
        //this.notifyDataSetChanged();
    }

        public void openTwoCards(){
            Log.d(TAG, "openTwoCards()");
            if (selectedPos1 != -1 && selectedPos2 != -1) {
                imges[selectedPos1] = frontimages[selectedPos1];
                imges[selectedPos2] = frontimages[selectedPos2];
                Log.i(TAG, "1 :    " + selectedPos1 + "    " + selectedPos2);
              //  flipCard(selectedPos1);
               // flipCard(selectedPos2);
                Log.i(TAG, "2 :    " + selectedPos1 + "    " + selectedPos2);
                this.notifyDataSetChanged();

                Log.d(TAG, "5cho");

                cardCompare();

            }
        }

        private void cardCompare(){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "****************** handler : " + selectedPos1 + selectedPos2);
                    if (isCardsMatched(frontimages[selectedPos1], frontimages[selectedPos2])) {
                        score += 10;
                      //  handler.sendEmptyMessage(1);
                        isOpened[selectedPos1] = true;
                        isOpened[selectedPos2] = true;
                    } else {
                        //handler.sendEmptyMessage(0);
                        imges[selectedPos1] = imges[selectedPos2] = BACK_IMAGE;
                    }
                    ismatching = false;
                    clearSelectedCards();
                }
            }, 1000);

        }

        public boolean isCardsMatched(int img1, int img2){
            Log.d(TAG,"isCardsMatched()");
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

        public boolean isGameClear(){
            for (int i = 0; i < isOpened.length; ++i){
                if (!isOpened[i])
                    return false;
            }
            return true;
        }

        public int getScore(){
            Log.d(TAG, "Score : " + score);
            return score;
        }

    @Override
    public int getCount() {
        return imges.length;
    }

    @Override
    public Object getItem(int position) {
        return imges[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int rowWidth = (mMetrics.widthPixels) / row;
        int colWidth = (mMetrics.heightPixels) / col;

       // Log.i(TAG, rowWidth + " " + colWidth);

        if (convertView == null) {
            Log.d(TAG, "이미지뷰 생성");
            imageViews[position].setLayoutParams(new GridView.LayoutParams(rowWidth - 50, colWidth - 50));
            frontImageViews[position].setLayoutParams(new GridView.LayoutParams(rowWidth - 50, colWidth - 50));
        } else {
            Log.d(TAG, "이미지뷰 불러오기");
            imageViews[position] = (ImageView) convertView;
        }

        if (isStart) {
            Log.d(TAG, "start true");
            imageViews[position].setImageResource(frontimages[position]);
        }else {
            Log.d(TAG, "start false - "+ (++cnt));
            imageViews[position].setImageResource(imges[position]);
        }

        //imageViews[position].setImageResource(imges[position]);
        return imageViews[position];
    }
}
