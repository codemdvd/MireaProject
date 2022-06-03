package ru.mirea.apasov.mireaproject;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import ru.mirea.apasov.mireaproject.databinding.FragmentCalculateBinding;


public class CalculateFragment extends Fragment {
    private Float firstDigit;
    private Float secondDigit;
    private String operation = null;
    private String actualOperation = null;
    private boolean flag = false;

    private FragmentCalculateBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCalculateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.button0.setOnClickListener(this::onNumberButtonClick);
        binding.button1.setOnClickListener(this::onNumberButtonClick);
        binding.button2.setOnClickListener(this::onNumberButtonClick);
        binding.button3.setOnClickListener(this::onNumberButtonClick);
        binding.button4.setOnClickListener(this::onNumberButtonClick);
        binding.button5.setOnClickListener(this::onNumberButtonClick);
        binding.button6.setOnClickListener(this::onNumberButtonClick);
        binding.button7.setOnClickListener(this::onNumberButtonClick);
        binding.button8.setOnClickListener(this::onNumberButtonClick);
        binding.button9.setOnClickListener(this::onNumberButtonClick);
        binding.buttonPoint.setOnClickListener(this::onNumberButtonClick);

        binding.buttonDiv.setOnClickListener(this::onOperationButtonClick);
        binding.buttonMin.setOnClickListener(this::onOperationButtonClick);
        binding.buttonMult.setOnClickListener(this::onOperationButtonClick);
        binding.buttonPlus.setOnClickListener(this::onOperationButtonClick);
        binding.buttonRes.setOnClickListener(this::onOperationButtonClick);

        binding.buttonClear.setOnClickListener(this::onClearButtonClick);

        return root;
    }

    private void onNumberButtonClick(View v) {
        Button b = (Button) v;
        String text = binding.textCalc.getText().toString();
        if (text.equals("0")) {
            binding.textCalc.setText(b.getText());
        } else {
            binding.textCalc.setText(text + b.getText());
        }
    }

    //clear button
    private void onClearButtonClick(View v) {
        binding.textHistory.setText("");
        binding.textCalc.setText("0");
        firstDigit = null;
        secondDigit = null;
        operation = null;
        actualOperation = null;
    }

    private void onOperationButtonClick(View view) {
        if (flag){
            binding.textHistory.setText(null);
            flag = false;
        }
        String result = binding.textHistory.getText().toString();
        String text = binding.textCalc.getText().toString();
        if (operation == null) {
            binding.textHistory.setText(null);
            firstDigit = Float.parseFloat(text);
        } else {
            secondDigit = Float.parseFloat(text);
        }
        result += text;
        binding.textCalc.setText(null);
        Button button = (Button) view;
        operation = button.getText().toString();
        result += " " + operation + " ";

        if (operation.equals("=")) {
            Float resultText = calculate();
            binding.textCalc.setText(resultText.toString());
            firstDigit = null;
            secondDigit = null;
            operation = null;
            flag = true;
        } else {
            actualOperation = operation;
        }

        binding.textHistory.setText(result);
    }

    private Float calculate() {
        Float result = 0f;

        if (actualOperation.equals("+")) {
            result = firstDigit + secondDigit;
        } else if (actualOperation.equals("-")) {
            result = firstDigit - secondDigit;
        } else if (actualOperation.equals("*")) {
            result = firstDigit * secondDigit;
        } else if (actualOperation.equals("/")) {
            result = firstDigit / secondDigit;
        }

        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}