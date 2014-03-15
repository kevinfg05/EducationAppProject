package com.example.educationapp;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created by kevin on 14-3-15.
 */
public class CutImageActivity extends SherlockActivity {
    public static final String EXTRA_IMAGE_PATH = "image_path";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutimage);
        imageView = (ImageView)findViewById(R.id.image);
        imageView.setOnTouchListener(new TounchListener());
        String imagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
    }

    private class TounchListener implements View.OnTouchListener{

        private PointF startPoint = new PointF();
        private Matrix matrix = new Matrix();
        private Matrix currentMaritx = new Matrix();

        private float startDis = 0;
        private PointF midPoint;

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    currentMaritx.set(imageView.getImageMatrix());
                    startPoint.set(event.getX(),event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:
                    float dx = event.getX() - startPoint.x;
                    float dy = event.getY() - startPoint.y;
                    matrix.set(currentMaritx);
                    matrix.postTranslate(dx, dy);

                    break;

                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    startDis = distance(event);

                    if(startDis > 10f){
                        midPoint = mid(event);
                        currentMaritx.set(imageView.getImageMatrix());
                    }

                    break;


            }
            imageView.setImageMatrix(matrix);
            return true;
        }

    }

    /**
     * Á½µãÖ®¼äµÄ¾àÀë
     * @param event
     * @return
     */
    private static float distance(MotionEvent event){
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return FloatMath.sqrt(dx * dx + dy * dy);
    }
    /**
     * ¼ÆËãÁ½µãÖ®¼äÖÐÐÄµãµÄ¾àÀë
     * @param event
     * @return
     */
    private static PointF mid(MotionEvent event){
        float midx = event.getX(1) + event.getX(0);
        float midy = event.getY(1) - event.getY(0);

        return new PointF(midx/2, midy/2);
    }

}
