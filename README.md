# Flowers
一个直播点赞的效果
# 用法
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            HeartView view = new HeartView(MainActivity.this);
            layout.addView(view);
            handler.postDelayed(this, 500);
        }
    }, 1000);
# 效果
![image](https://github.com/jthou20121212/Flowers/blob/master/git/aa.gif)
