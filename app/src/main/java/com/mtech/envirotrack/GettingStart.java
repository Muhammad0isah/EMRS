package com.mtech.envirotrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;


public class GettingStart extends AppCompatActivity {

    Button btnGetStarted;
    private ViewPager2 viewPager;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private int[] images = {R.drawable.s2, R.drawable.s3, R.drawable.s1,R.drawable.s4};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_start);

        btnGetStarted = findViewById(R.id.getStartedButton);


        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GettingStart.this, MainActivity.class);
                startActivity(intent);
            }
        });

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ImageAdapter(images));

        runnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() < images.length - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    viewPager.setCurrentItem(0);
                }
                handler.postDelayed(this, 4000);
            }
        };
        handler.postDelayed(runnable, 4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
        window.getDecorView().setSystemUiVisibility(0);

    }
}
class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private int[] images;

    public ImageAdapter(int[] images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}