package ru.mirea.apasov.mireaproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import ru.mirea.apasov.mireaproject.databinding.FragmentCalculateBinding;
import ru.mirea.apasov.mireaproject.databinding.FragmentMusicPlayerBinding;

public class MusicPlayerFragment extends Fragment {
    private ImageButton buttonPlayMusic;
    private ImageButton buttonStopMusic;
    private ImageView imageView2;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        buttonPlayMusic = (ImageButton) view.findViewById(R.id.ButtonPlayMusic);
        buttonPlayMusic.setOnClickListener(this::onClickPlayMusic);
        buttonStopMusic = (ImageButton) view.findViewById(R.id.ButtonStopMusic);
        buttonStopMusic.setOnClickListener(this::onClickStopMusic);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        return view;

    }

    final Runnable animation = new Runnable() {
        public void run() {
            Animation animation = AnimationUtils.loadAnimation(
                    getContext(), R.anim.scale);
            buttonPlayMusic.startAnimation(animation);
            Animation animation_cover = AnimationUtils.loadAnimation(
                    getContext(), R.anim.little);
            imageView2.startAnimation(animation_cover);
        }
    };

    final Runnable animation2 = new Runnable() {
        public void run() {
            Animation animation = AnimationUtils.loadAnimation(
                    getContext(), R.anim.big);
            buttonStopMusic.startAnimation(animation);
            Animation animation_cover = AnimationUtils.loadAnimation(
                    getContext(), R.anim.little);
            imageView2.startAnimation(animation_cover);
        }
    };

    final Runnable change = new Runnable() {
        public void run() {
            getActivity().startService(
                    new Intent(getContext(), MusicService.class));
            buttonPlayMusic.setVisibility(View.GONE);
            buttonStopMusic.setVisibility(View.VISIBLE);

        }
    };
    final Runnable change_back = new Runnable() {
        public void run() {
            buttonStopMusic.setVisibility(View.GONE);
            buttonPlayMusic.setVisibility(View.VISIBLE);
        }
    };

    final Runnable stop_thread = new Runnable() {
        public void run() {
            getActivity().stopService(
                    new Intent(getContext(), MusicService.class));
        }
    };

    public void onClickPlayMusic(View view) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(animation);
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(change);
                }
            });

            thread.start();

                }

        private void onClickStopMusic(View view){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(animation2);
                    getActivity().runOnUiThread(stop_thread);

                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(change_back);
                }
            });

            thread.start();

        }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}