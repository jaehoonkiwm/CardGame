package com.iot.cardgame;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by iao on 15. 5. 28.
 */
public class ImageAdapter extends BaseAdapter {
	private static final int BACK_IMG = R.drawable.backimg;
	private static final int CLICKED_IMG = R.drawable.clickedbackimg;
    private Context context;
    private DisplayMetrics mMetrics;
    private int row;
    private int col;

    private int[] imges;
    private int[] frontimages = {R.drawable.junghwa, R.drawable.junghwa2, R.drawable.junghwa3,
                                R.drawable.junghwa, R.drawable.junghwa2, R.drawable.junghwa3};
    private boolean[] isClicked;
    private boolean[] isOpened;
    private int selectedPos1 = -1;
    private int selectedPos2 = -1;

    public ImageAdapter(Context context, DisplayMetrics mMetrics, int cntCard) {
        this.context = context;
        this.mMetrics = mMetrics;
        this.imges = new int[cntCard];
        //this.frontimages = new int[cntCard];
        this.isClicked = new boolean[cntCard];
        this.isOpened = new boolean[cntCard];

        for (int i = 0; i != imges.length; ++i) {
            this.imges[i] = BACK_IMG;
            this.isClicked[i] = false;
            this.isOpened[i] = false;
        }


        setRowCol(cntCard);
    }

    private void setRowCol(int cnt){            // 카드 개수에 따라 열과 행을 설
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
	    if (isOpened[position] == false) {
		    if (isClicked[position]) {
			    isClicked[position] = false;
			    imges[position] = BACK_IMG;
			    selectedPos1 = -1;
		    } else {
			    isClicked[position] = true;
			    imges[position] = CLICKED_IMG;

			    if (selectedPos1 == -1)
				    selectedPos1 = position;
			    else {
				    selectedPos2 = position;
				    openTwoCards();
			    }
		    }
	    }
    }

    public void openTwoCards(){
		imges[selectedPos1] = frontimages[selectedPos1];
	    imges[selectedPos2] = frontimages[selectedPos2];

	    this.notifyDataSetChanged();

	    if (isMatched(frontimages[selectedPos1], frontimages[selectedPos2]))
		    Toast.makeText(context, "일치", Toast.LENGTH_SHORT).show();
	    else{
		    Toast.makeText(context, "불일치", Toast.LENGTH_SHORT).show();
	    }
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
            if (isOpened[i] == false && isClicked[i] == true) {
                isClicked[i] = false;
                imges[i] = BACK_IMG;
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
