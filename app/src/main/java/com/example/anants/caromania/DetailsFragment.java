package com.example.anants.caromania;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;



public class DetailsFragment extends Fragment {

    private ImageView mCarImage;
    private EditText mCarTitle;
    private EditText mAboutCar;
    private EditText mEngineDisplacement;
    private EditText mMaximumPower;
    private EditText mMaximumTorque;
    private EditText mTopSpeed;
    private EditText mAcceleration;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button mEditButton;
    private Button mSaveButton;
    private RelativeLayout main;
    private ImageButton mImgButton;
    private static final int SELECT_PICTURE = 100;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_details, container, false);
        mCarImage = (ImageView) v.findViewById(R.id.imageView2);
        mCarTitle = (EditText) v.findViewById(R.id.carTitle);
        mAboutCar = (EditText) v.findViewById(R.id.aboutCar);
        mEngineDisplacement = (EditText) v.findViewById(R.id.enginedisplacement);
        mMaximumPower = (EditText) v.findViewById(R.id.maximumpower);
        mMaximumTorque = (EditText) v.findViewById(R.id.maximumtorque);
        mTopSpeed = (EditText) v.findViewById(R.id.topspeed);
        mAcceleration = (EditText) v.findViewById(R.id.acceleration);
        mEditButton = (Button) v.findViewById(R.id.editButton);
        mSaveButton = (Button) v.findViewById(R.id.saveButton);
        main = (RelativeLayout) v.findViewById(R.id.mainLayout);
        mImgButton=(ImageButton) v.findViewById(R.id.imageEditButton);
        //defining shared preferences
        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        unedit();
        try {
            getPref();
        }catch (Exception e){

        }
        //edit button onClick listener
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
                mSaveButton.setVisibility(View.VISIBLE);
                mEditButton.setVisibility(View.GONE);
            }
        });

        //save button onClick listener
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPref();
                getPref();
                unedit();
                mSaveButton.setVisibility(View.GONE);
                mEditButton.setVisibility(View.VISIBLE);
                Snackbar.make(main,getResources().getString(R.string.saved),
                        Snackbar.LENGTH_LONG).show();
            }
        });
        mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void unedit(){
        mCarTitle.setEnabled(false);
        mAboutCar.setEnabled(false);
        mEngineDisplacement.setEnabled(false);
        mMaximumPower.setEnabled(false);
        mMaximumTorque.setEnabled(false);
        mAcceleration.setEnabled(false);
        mTopSpeed.setEnabled(false);
    }
    public void edit(){
        mCarTitle.setEnabled(true);
        mAboutCar.setEnabled(true);
        mEngineDisplacement.setEnabled(true);
        mMaximumPower.setEnabled(true);
        mMaximumTorque.setEnabled(true);
        mAcceleration.setEnabled(true);
        mTopSpeed.setEnabled(true);
    }

    /*function to get data from the view and save it in the shared preference*/
    public void setPref(){
        editor.putString("carTitle",mCarTitle.getText().toString());
        editor.putString("aboutCar",mAboutCar.getText().toString());
        editor.putString("engineDisplacement",
                mEngineDisplacement.getText().toString());
        editor.putString("maximumPower",mMaximumPower.getText().toString());
        editor.putString("maximumTorque",mMaximumTorque.getText().toString());
        editor.putString("topspeed",mTopSpeed.getText().toString());
        editor.putString("acceleration",mAcceleration.getText().toString());
        editor.commit();
    }

    public void getPref(){
        mCarTitle.setText(pref.getString("carTitle",""));
        mAboutCar.setText(pref.getString("aboutCar",""));
        mEngineDisplacement.setText(pref.getString("engineDisplacement",""));
        mMaximumTorque.setText(pref.getString("maximumTorque",""));
        mMaximumPower.setText(pref.getString("maximumPower",""));
        mAcceleration.setText(pref.getString("acceleration",""));
        mTopSpeed.setText(pref.getString("topspeed",""));
        mCarImage.setImageBitmap(decodeBase64(
                pref.getString("imagePreferance","")));
    }
    public String encodeTobase64() {
        Bitmap immage = ((BitmapDrawable) mCarImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    /*method to convert base64 string to bitmap*/
    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                /*Get the url from data*/
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    /*setting the image in imageView and saving it in base64 coding in shared Preference*/
                    mCarImage.setImageURI(selectedImageUri);
                    editor.putString("imagePreferance", encodeTobase64());
                    editor.commit();
                    Snackbar.make(main,getResources().getString(R.string.image_saved),Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
