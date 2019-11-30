package com.example.androidscene;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArFragment arFragment;
    private ModelRenderable androidRenderable;

    ImageView android;

    View arrayView[];
    //ViewRenderable name_android;

    int selected = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);

        android = (ImageView)findViewById(R.id.android);

        setArrayView();
        setClickListener();
        setupModel();

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    createModel(anchorNode,selected);

            }
        });

    }

    private void setupModel() {



        ModelRenderable.builder()
                .setSource(this, R.raw.android)
                .build()
                .thenAccept(renderable -> androidRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                            Toast.makeText(this, "Model not loaded", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                            return null;
                        });
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if(selected == 1)
        {
            TransformableNode android = new TransformableNode(arFragment.getTransformationSystem());
            android.setParent(anchorNode);
            android.setRenderable(androidRenderable);
            android.select();

            addName(anchorNode,android,"Android");
        }
    }

    private void addName(AnchorNode anchorNode, TransformableNode model, String name) {

        ViewRenderable.builder()
                        .setView(this,R.layout.name_object)
                        .build()
                        .thenAccept(ViewRenderable -> {
                            TransformableNode nameView = new TransformableNode(arFragment.getTransformationSystem());
                            nameView.setLocalPosition(new Vector3(0f, model.getLocalPosition() .y+0.5f, 0 ));
                            nameView.setParent(anchorNode);
                            nameView.setRenderable(ViewRenderable);
                            nameView.select();

                            TextView txt_name = (TextView)ViewRenderable.getView();
                            txt_name.setText(name);

                            txt_name.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    anchorNode.setParent(null);
                                }
                            });
                        });

    }

    private void setClickListener() {
        for(int i=0; i<arrayView.length;i++)
            arrayView[i].setOnClickListener(this);
    }

    private void setArrayView() {
        arrayView = new View[]{
                android
        };
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.android){
            selected = 1;
            setBackgroud(v.getId());
        }
    }

    private void setBackgroud(int id) {
        for(int i=0; i<arrayView.length;i++)
        {
            if(arrayView[i].getId() == id)
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"));
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
