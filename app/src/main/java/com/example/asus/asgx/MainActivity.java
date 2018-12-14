package com.example.asus.asgx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager ViewPage_Detail;
    private LinearLayout point_detail;
    private Context context;
    private View view;
    //轮播图图片资源
    private final int[] viewpage_images = {R.drawable.g1, R.drawable.g2, R.drawable.g3, R.drawable.g4,R.drawable.g5,R.drawable.g6};
    private ArrayList<ImageView> viewpage_imageList;
    //判断是否自动滚动ViewPage
    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Instantiation();

    }

    public void Instantiation() {

        ViewPage_Detail = (ViewPager)findViewById(R.id.ViewPage_Detail);
        point_detail = (LinearLayout)findViewById(R.id.point_detail);

        //初始化图片资源
        viewpage_imageList = new ArrayList<ImageView>();
        for (int i : viewpage_images) {
            // 初始化图片资源
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(i);
            viewpage_imageList.add(imageView);

            // 添加指示小点
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,
                    15);
            params.rightMargin = 10;
            params.bottomMargin = 15;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.ic_light);
            if (i == R.drawable.g1) {
                //默认聚焦在第一张
                point.setBackgroundResource(R.drawable.ic_brightness_1_black_24dp);
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }

            point_detail.addView(point);

        }

        //首页轮播
        CarouselShow carouselShow = new CarouselShow(context, viewpage_imageList);
        carouselShow.CarouselShow_Info_Detail(this);
        handler.sendEmptyMessageDelayed(0, 3000);

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 执行滑动到下一个页面
            ViewPage_Detail.setCurrentItem(ViewPage_Detail.getCurrentItem() + 1);
            if (isRunning) {
                // 在发一个handler延时
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    };


    @Override
    public void onDestroy() {
        // 停止滚动
        isRunning = false;
        super.onDestroy();
    }

}

class CarouselShow {

    private final Context context;
    private LinearLayout point_group;
    private ArrayList<ImageView> viewpage_imageList;
    protected int lastPosition = 0;
    private ViewPager ViewPage_Detail;
    private LinearLayout point_detail;

    public CarouselShow(Context context, ArrayList<ImageView> viewpage_imageList) {
        this.context = context;
        this.viewpage_imageList = viewpage_imageList;
    }

    /**
     * 当需要多个轮播功能的时候 建立一个类来调用 并实现此方法
     */
    public void CarouselShow_Info_Detail(MainActivity view){

        ViewPage_Detail = (ViewPager)view.findViewById(R.id.ViewPage_Detail);
        point_detail = (LinearLayout) view.findViewById(R.id.point_detail);
        ViewPage_Detail.setAdapter(new TeaNetPageAdapter(viewpage_imageList));

        // 设置当前viewPager的位置
        ViewPage_Detail.setCurrentItem(Integer.MAX_VALUE / 2
                - (Integer.MAX_VALUE / 2 % viewpage_imageList.size()));

        ViewPage_Detail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 页面切换后调用， position是新的页面位置
                // 实现无限制循环播放
                position %= viewpage_imageList.size();

                // 把当前点设置为true,将上一个点设为false；并设置point_group图标
                point_detail.getChildAt(position).setEnabled(true);
                point_detail.getChildAt(position).setBackgroundResource(R.drawable.ic_brightness_1_black_24dp);//设置聚焦时的图标样式
                point_detail.getChildAt(lastPosition).setEnabled(false);
                point_detail.getChildAt(lastPosition).setBackgroundResource(R.drawable.ic_light);//上一张恢复原有图标
                lastPosition = position;
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}

class TeaNetPageAdapter extends PagerAdapter {

    private ArrayList<ImageView> viewpage_imageList;

    public TeaNetPageAdapter(ArrayList<ImageView> viewpage_imageList) {

        this.viewpage_imageList = viewpage_imageList;

    }
    // 需要实现以下四个方法

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // 判断view和Object对应是否有关联关系
        if (view == object) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 获得相应位置上的view； container view的容器，其实就是viewpage自身,
        // position: viewpager上的位置
        // 给container添加内容
        container.addView(viewpage_imageList.get(position % viewpage_imageList.size()));

        return viewpage_imageList.get(position % viewpage_imageList.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 销毁对应位置上的Object
        // super.destroyItem(container, position, object);
        container.removeView((View) object);
        object = null;
    }
}