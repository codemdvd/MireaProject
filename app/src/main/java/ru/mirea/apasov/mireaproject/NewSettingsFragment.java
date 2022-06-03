package ru.mirea.apasov.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import ru.mirea.apasov.mireaproject.databinding.FragmentNewSettingsBinding;

public class NewSettingsFragment extends Fragment {

    private FragmentNewSettingsBinding binding;
    private ViewGroup back;
    private boolean colorCheck = false;
    private boolean moodCheck = false;
    private boolean notificationCheck = false;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView sad;
    private TextView happy;
    private SharedPreferences preferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        back = binding.ConstraintLayout;
        textView1 = binding.textView4;
        textView2 = binding.textView5;
        textView3 = binding.textView6;
        textView4 = binding.textView7;
        sad = binding.Sad;
        happy = binding.Happy;



        binding.switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                colorCheck = true;
                back.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                textView1.setTextColor(getResources().getColor(R.color.white));
                textView2.setTextColor(getResources().getColor(R.color.white));
                textView3.setTextColor(getResources().getColor(R.color.white));
                textView4.setTextColor(getResources().getColor(R.color.white));
                sad.setTextColor(getResources().getColor(R.color.white));
                happy.setTextColor(getResources().getColor(R.color.white));

            } else {
                colorCheck = false;
                back.setBackgroundColor(getResources().getColor(R.color.white));
                textView1.setTextColor(getResources().getColor(R.color.grey));
                textView2.setTextColor(getResources().getColor(R.color.grey));
                textView3.setTextColor(getResources().getColor(R.color.grey));
                textView4.setTextColor(getResources().getColor(R.color.grey));
                sad.setTextColor(getResources().getColor(R.color.grey));
                happy.setTextColor(getResources().getColor(R.color.grey));
            }
            preferences.edit().putBoolean("color", colorCheck).apply();
        });

        binding.switchMood.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                moodCheck = true;
                sad.setVisibility(View.GONE);
                happy.setVisibility(View.VISIBLE);
            } else{
                moodCheck = false;
                happy.setVisibility(View.GONE);
                sad.setVisibility(View.VISIBLE);
            }
            preferences.edit().putBoolean("mood",moodCheck).apply();
        });

        binding.switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked){
            notificationCheck = true;
            Toast toast = Toast.makeText(getContext(),
                    "WASSSSUUP", Toast.LENGTH_SHORT);
            toast.show();
        } else{
            notificationCheck = false;
        }
            preferences.edit().putBoolean("notification",notificationCheck).apply();
        });

        binding.LoadButton.setOnClickListener(this::onLoadText);

        return root;
    }

    public void onLoadText(View view) {
        Boolean savedColor = preferences.getBoolean("color", false);
        Boolean savedMood = preferences.getBoolean("mood", false);
        Boolean savedNotification = preferences.getBoolean("notification", false);
        binding.switchNotification.setChecked(savedNotification);
        binding.switchDark.setChecked(savedColor);
        binding.switchMood.setChecked(savedMood);

    }
}