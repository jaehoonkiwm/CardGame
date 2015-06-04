package com.iot.cardgame;

import android.content.Context;
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

/**
 * Created by iao on 15. 5. 28.
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdaptor";
	private static final int BACK_IMAGE = R.drawable.backimg;
	private static final int CLICKED_IMAGE = R.drawable.clickedbackimg;

	private static final int IMAGE_IS_MACHED = 1;
	private static final int IMAGE_IS_NOT_MACHED = 0;

    private boolean isStart = true;

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

    private TextView tvScore;
    private ImageView imageView;

    private int score;

    public ImageAdapter(Context context, DisplayMetrics mMetrics, int cntCard) {
        this.context = context;
        this.mMetrics = mMetrics;
        this.imges = new int[cntCard];
        //this.frontimages = new int[cntCard];
        this.isClicked = new boolean[cntCard];
        this.isOpened = new boolean[cntCard];

        for (int i = 0; i != imges.length; ++i) {
            this.imges[i] = BACK_IMAGE;
            this.isClicked[i] = false;
            this.isOpened[i] = false;
        }

        this.score = 0;

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
	    if (!isOpened[position] && !isStart) {      // 오픈된 카드가 아닐경우
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
				    openTwoCards();
			    }
		    }

            this.notifyDataSetChanged();
	    }
    }

    public void openTwoCards(){
		imges[selectedPos1] = frontimages[selectedPos1];
	    imges[selectedPos2] = frontimages[selectedPos2];

	    this.notifyDataSetChanged();

	    new Handler().postDelayed(new Runnable() {
		    @Override
		    public void run() {
			    if (isCardsMatched(frontimages[selectedPos1], frontimages[selectedPos2])) {

                    score += 10;
				    handler.sendEmptyMessage(1);

			    } else{
				    handler.sendEmptyMessage(0);
			    }
		    }
	    }, 1000);

    }

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == -1){
                Log.d(TAG, "10초");
                isStart = false;
            } else if (msg.what == IMAGE_IS_MACHED){
				isOpened[selectedPos1] = true;
				isOpened[selectedPos2] = true;
				Toast.makeText(context, "일치", Toast.LENGTH_SHORT).show();
			} else if (msg.what == IMAGE_IS_NOT_MACHED){
				Toast.makeText(context, "불일치", Toast.LENGTH_SHORT).show();
				imges[selectedPos1] = imges[selectedPos2] = BACK_IMAGE;
			}
			clearSelectedCards();
		}
	};

    public boolean isCardsMatched(int img1, int img2){
        if (img1 == img2) {
            return true;
        }
        return false;
    }

    public void decision(){
        for (int i = 0; i != isClicked.length; ++i){
;
        }
    }

    public void clearSelectedCards(){
        for (int i = 0; i < isClicked.length; ++i)
            if (!isOpened[i] && isClicked[i]) {
                isClicked[i] = false;
            }

	    selectedPos1 = -1;
	    selectedPos2 = -1;
	    this.notifyDataSetChanged();
    }

    public int getScore(){
        Log.d(TAG, score + "");
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

        Log.i(TAG, rowWidth + " " + colWidth);

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(rowWidth - 50, colWidth- 50));
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setPadding(3, 3, 3, 3);
        } else {
            imageView = (ImageView) convertView;
        }

        if (isStart) {
            Log.d(TAG, "start true");
            imageView.setImageResource(frontimages[position]);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(-1);
                }
            }, 1000);
        }else {
            Log.d(TAG, "start false");
            imageView.setImageResource(imges[position]);
        }
        return imageView;
    }
}
