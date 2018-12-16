package com.project.YBapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;
import java.util.HashMap;

public class ImageLoadTask extends AsyncTask<Void,Void, Bitmap> {


    private String urlStr;
    private ImageView imageView;

    //메모리 해제를 하기 위해
    private static HashMap<String, Bitmap> bitmapHash = new HashMap<String, Bitmap>();

    //생성자
    public ImageLoadTask(String urlStr, ImageView imageView){
        this.urlStr = urlStr;
        this.imageView =imageView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //요청
    @Override
    protected Bitmap doInBackground(Void... voids) {

        Bitmap bitmap = null;

        try {
            //요청주소가 이미 해쉬맵 안에 들어있다고 하면
            if(bitmapHash.containsKey(urlStr)){

                Bitmap oldBitmap = bitmapHash.remove(urlStr); //꺼낸다 이전에 이미 만들어진 객체
                if(oldBitmap != null){
                    oldBitmap.recycle(); //이전 객체를 해제한다.
                    oldBitmap = null;
                }

            }

                //URL을 먼저 받고
                URL url = new URL(urlStr);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                bitmapHash.put(urlStr, bitmap); // 해쉬에 넣어놔야 이전에 만들어 줬던거를

        } catch (Exception e){

            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        imageView.setImageBitmap(bitmap);
        imageView.invalidate(); //다시 그려주는 혹시나 다시 그리는게 안될 수도 있어서
    }

}
