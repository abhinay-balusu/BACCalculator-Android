//HomeWork 1
//MainActivity.java
//Abhinay Balusu, Swarna Vallurupalli

package com.abhinaybalusu.baccalculator;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    //Global Variables
    private EditText weightEditText;
    private Switch genderSwitch;
    private Button saveButton;
    private RadioGroup ozRadioGroup;
    private SeekBar seekBar;
    private TextView seekBarPercentageTextView;
    private Button addDrinkButton;
    private Button resetButton;
    private TextView bacLevelTextView;
    private ProgressBar progressBar;
    private TextView statusTextView;

    private Double weightValue;
    private Double genderValue;
    private int alcoholPercentage;
    private int drinkSize;
    private double bacLevel;
    private double new_baclevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting ActionBar Icon
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setLogo(R.mipmap.ic_launcher);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00BFFF")));

        //Creating objects for UI elements
        weightEditText = (EditText)findViewById(R.id.weightEditText);
        genderSwitch = (Switch)findViewById(R.id.genderSwitch);
        saveButton = (Button)findViewById(R.id.buttonSave);
        ozRadioGroup = (RadioGroup)findViewById(R.id.ozRadioGroup);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        //seekBar.incrementProgressBy(5);

        seekBarPercentageTextView = (TextView)findViewById(R.id.seekBarPercentageTextView);
        addDrinkButton = (Button)findViewById(R.id.buttonAddDrink);
        resetButton = (Button)findViewById(R.id.buttonReset);
        bacLevelTextView = (TextView)findViewById(R.id.bacLevelTextView);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(25);

        statusTextView = (TextView)findViewById(R.id.statusTextView);
        statusTextView.setBackgroundResource(R.drawable.rounded_corners_green);

        //Default Values
        weightValue = 0.00;
        genderSwitch.setChecked(false);
        alcoholPercentage = 5;
        drinkSize = 1;
        bacLevel = 0.00;
        new_baclevel = 0.00;

        //Listener for Radio Group to know which radio button is checked and to get its value
        ozRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.oneOZRadioButton)
                {
                    drinkSize = 1;
                }
                else if(checkedId == R.id.fiveOZRadioButton)
                {
                    drinkSize = 5;
                }
                else if(checkedId == R.id.twelveOZRadioButton)
                {
                    drinkSize = 12;
                }
                else
                {
                    drinkSize = 1;
                }
            }
        });

        //Listener for seek Bar to get its current value
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



                alcoholPercentage = progress;

                progress = ((int)Math.round(progress/5))*5;
                if(progress<5)
                {
                    alcoholPercentage = 5;
                    progress = 5;
                }
                seekBar.setProgress(progress);
                seekBarPercentageTextView.setText(String.valueOf(progress)+"%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Save Button functionality
        assert saveButton != null;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bacLevel = 0.00;
                new_baclevel = 0.00;
                if(drinkSize == 1 && alcoholPercentage == 5)
                {
                    calculateBACLevel(1,5);
                }
                else
                {
                    calculateBACLevel(drinkSize,alcoholPercentage);
                }
                bacLevelTextView.setText("BAC Level: "+bacLevel);
                progressBar.setProgress((int)(bacLevel*100));

                setStatusMessage(bacLevel);

            }
        });

        //Add Drink Button Functionality
        addDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new_baclevel = bacLevel;
                calculateBACLevel(drinkSize,alcoholPercentage);
                new_baclevel = new_baclevel + bacLevel;
                bacLevelTextView.setText("BAC Level: "+new_baclevel);
                progressBar.setProgress((int)(new_baclevel*100));

                setStatusMessage(new_baclevel);
            }
        });

        //Reset Button Functionality where all the variables are set to its default values
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weightEditText.setText("");
                weightEditText.setEnabled(true);
                genderSwitch.setChecked(false);
                RadioButton oneOZButton = (RadioButton) findViewById(R.id.oneOZRadioButton);
                oneOZButton.setChecked(true);
                seekBar.setProgress(5);
                progressBar.setProgress(0);
                bacLevelTextView.setText("BAC Level: 0.00");
                statusTextView.setText("You're Safe");
                statusTextView.setBackgroundResource(R.drawable.rounded_corners_green);
                saveButton.setEnabled(true);
                addDrinkButton.setEnabled(true);
                new_baclevel = 0.00;
                bacLevel = 0.00;
            }
        });
    }

    //Calculate BAC Level
    public void calculateBACLevel(int drinkSize, int alcoholPercentage)
    {
        if(!weightEditText.getText().toString().matches(""))
        {
            weightValue = Double.parseDouble(weightEditText.getText().toString());


            if (genderSwitch.isChecked())
                genderValue = 0.68;
            else
                genderValue = 0.55;

            bacLevel = (drinkSize * (alcoholPercentage * 0.01) * 6.24)/(weightValue * genderValue);

            DecimalFormat df = new DecimalFormat("#.##");
            bacLevel = Double.valueOf(df.format(bacLevel));

            //Toast.makeText(getApplicationContext(),String.valueOf(bacLevel),Toast.LENGTH_SHORT).show();
        }
        else
        {
            weightEditText.setError("Enter Weight in lbs");
        }
    }

    //Function to check BAC level to set status message and to disable save and addDrink buttons if BAC level exceeds the limit
    public void setStatusMessage(Double bacLevel)
    {
        if(bacLevel <= 0.08)
        {
            statusTextView.setText("You're Safe");
            statusTextView.setBackgroundResource(R.drawable.rounded_corners_green);

        }else if(bacLevel>0.08 && bacLevel<0.20)
        {
            statusTextView.setText("Be Careful...");
            statusTextView.setBackgroundResource(R.drawable.rounded_corners_orange);
        }
        else if(bacLevel>=0.20)
        {
            statusTextView.setText("Over the Limit!");
            statusTextView.setBackgroundResource(R.drawable.rounded_corners_red);
        }

        if(bacLevel>=0.25)
        {
            saveButton.setEnabled(false);
            addDrinkButton.setEnabled(false);
            weightEditText.setEnabled(false);

            Toast.makeText(getApplicationContext(),"No more drinks for you.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            saveButton.setEnabled(true);
            addDrinkButton.setEnabled(true);
            weightEditText.setEnabled(true);
        }
    }
}
