package com.dekar.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

//    private void closeOnError() {
//        finish();
//        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
//    }
//
//    private void populateUI(Sandwich sandwich) {
//
//        if(sandwich.getPlaceOfOrigin().length() == 0) {
//            mBinding.originTv.setText(R.string.data_unavailable);
//
//        } else {
//            mBinding.originTv.setText(sandwich.getPlaceOfOrigin());
//        }
//
//        if(sandwich.getDescription().length() == 0) {
//            mBinding.descriptionTv.setText(R.string.data_unavailable);
//
//        } else {
//            mBinding.descriptionTv.setText(sandwich.getDescription());
//        }
//
//        if(sandwich.getAlsoKnownAs().size() == 0) {
//            mBinding.alsoKnownTv.setText(R.string.data_unavailable);
//
//        } else {
//            mBinding.alsoKnownTv.setText(JsonUtils.implode(", ", sandwich.getAlsoKnownAs()));
//        }
//
//
//        if(sandwich.getIngredients().size() == 0) {
//            mBinding.ingredientsTv.setText(R.string.data_unavailable);
//        } else {
//            mBinding.ingredientsTv.setText(JsonUtils.implode(", ", sandwich.getIngredients()));
//
//        }
//
//
//    }
}
