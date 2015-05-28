package com.iot.cardgame;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by iao on 15. 5. 28.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private DisplayMetrics mMetrics;
    private int row;
    private int col;

    private int[] imges;
    private int[] frontimages;
    private boolean[] isClicked;
    private boolean[] isOpened;
    private int selectedImg1 = 0;
    private int selectedImg2 = 0;


    public ImageAdapter(Context context, DisplayMetrics mMetrics, int cntCard) {
        this.context = context;
        this.mMetrics = mMetrics;
        this.imges = new int[cntCard];
        this.frontimages = new int[cntCard];
        this.isClicked = new boolean[cntCard];
        this.isOpened = new boolean[cntCard];

        for (int i = 0; i != imges.length; ++i) {
            this.imges[i] = R.drawable.backimg;
            this.isClicked[i] = false;
            this.isOpened[i] = false;
        }


        setRowCol(cntCard);
    }

    private void setRowCol(int cnt){
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

    public void itemClicked(int position){
        if (isClicked[position]){
            isClicked[position] = false;
            imges[position] = R.drawable.backimg;
        } else{
            isClicked[position] = true;
            imges[position] = R.drawable.clickedbackimg;
            if (selectedImg1 == 0)
                selectedImg1 = frontimages[position];
            else
                selectedImg2 = frontimages[position];
        }
    }

    public boolean isSelectedTwoCards(){
        int cnt = 0;
        for (int i = 0; i < isClicked.length; ++i)
            if (isClicked[i] == true)
                cnt++;
        if (cnt == 2)
            return true;
        return false;
    }

    public boolean isMatched(int img1, int img2){
        if (img1 == img2)
            return true;
        return false;
    }

    public void decision(){
        for (int i = 0; i != isClicked.length; ++i){

        }
    }

    public void clearSelected(){
        for (int i = 0; i < isClicked.length; ++i)
            if (isClicked[i] == true) {
                isClicked[i] = false;
                imges[i] = R.drawable.backimg;
            }
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

        Log.i("ImageAdaptor", rowWidth + " " + colWidth);
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(rowWidth - 50, colWidth- 50));
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setPadding(3, 3, 3, 3);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(imges[position]);
        return imageView;
    }
}
