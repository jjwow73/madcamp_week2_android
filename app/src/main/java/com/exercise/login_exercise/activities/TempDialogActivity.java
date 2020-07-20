package com.exercise.login_exercise.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

public class TempDialogActivity extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;

    String title;
    String subtitle;
    int minvalue;
    int maxvalue;
    int step;
    int defvalue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final NumberPicker numberPicker = new NumberPicker(getActivity());

        title = getArguments().getString("title");
        subtitle = getArguments().getString("subtitle");
        minvalue = getArguments().getInt("minvalue");
        maxvalue = getArguments().getInt("maxvalue");
        step = getArguments().getInt("step");
        defvalue = getArguments().getInt("defvalue");

        //최소값과 최대값 사이의 값들 중에서 일정한 step사이즈에 맞는 값들을 배열로 만든다.
        String[] myValues = {"36.0", "36.1", "36.2", "36.3", "36.4", "36.5", "36.6", "36.7", "36.8", "36.9", "37.0", "37.1", "37.2" , "37.3", "37.4", "37.5", "37.6", "37.7", "37.8", "37.9", "38.0"};

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(myValues.length-1);
        numberPicker.setDisplayedValues(myValues);

        // dialog를 실행했을 때 시작지점)\
        numberPicker.setValue(myValues.length / 2);
        // 키보드 입력을 방지
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 제목 설정
        builder.setTitle(title);
        // 부제목 설정
        builder.setMessage(subtitle);

        // Ok 눌렀을 때 동작 설정
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog를 종료하면서 값이 변했다는 함수는 onValuechange함수를 실행시킨다.
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setView(numberPicker);
        //number picker 실행
        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    //최소값부터 최대값가지 일정 간격의 값을 String 배열로 출력
    public String[] getArrayWithSteps(int min, int max, int step) {

        int number_of_array = (max - min) / step + 1;

        String[] result = new String[number_of_array];

        for (int i = 0; i < number_of_array; i++) {
            result[i] = String.valueOf(min + step * i);
        }
        return result;
    }
}
