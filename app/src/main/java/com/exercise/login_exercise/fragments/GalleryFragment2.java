package com.exercise.login_exercise.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.adapters.GalleryResultAdapter;
import com.exercise.login_exercise.models.ImagesResponse;
import com.exercise.login_exercise.viewmodels.GalleryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jongwow on 2020-07-18.
 */
public class GalleryFragment2 extends Fragment implements View.OnClickListener {
    private static final String TAG = "GalleryFragment";
    private GalleryViewModel viewModel;
    private GalleryResultAdapter adapter;
    private ProgressBar progressBar;
    FloatingActionButton fabCamera;


    private final static int IMAGE_RESULT = 200;
    Uri picUri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new GalleryResultAdapter();

        viewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        viewModel.init();
        viewModel.getImagesResponseLiveData().observe(this, new Observer<ImagesResponse>() {
            @Override
            public void onChanged(ImagesResponse imagesResponse) {
                if (imagesResponse != null) {
                    adapter.setResults(imagesResponse.getUrls());
                    Log.d(TAG, "onChanged: result: " + imagesResponse.getUrls());
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);

        fabCamera = view.findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(this);

        return view;
    }

    private void getImages() {
        viewModel.getImages();
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up progress before call
        progressBar = view.findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);
        getImages();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabCamera) {
            startActivityForResult(getPickImageChooserIntent(getContext()), IMAGE_RESULT);
            Toast.makeText(getContext(), "fabCamera", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(Context mContext, String path) {
        try {
            // 해당 경로에 이미지가 존재하는지 확인
            File oldImage = new File(path);
            if (!oldImage.exists()) {
                Toast.makeText(mContext, " 파일이 존재하지 않습니다. ", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), oldImage);
            MultipartBody.Part parts = MultipartBody.Part.createFormData("img", oldImage.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "img");
            viewModel.postImage(parts, name);
        } catch (Exception e) {
            Log.d(TAG, "uploadImage: Error");
            e.printStackTrace();
        }
    }

    private Uri getCaptureImageOutputUri(Context mContext) {
        Uri outputFileUri = null;
        File getImage = mContext.getExternalFilesDir("");
        if (getImage != null) {
            Log.d(TAG, "getCaptureImageOutputUri:  is NULL");
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
            Log.d(TAG, "getCaptureImageOutputUri: from null to" + outputFileUri.toString());
        }
        return outputFileUri;
    }

    public Intent getPickImageChooserIntent(Context mContext) {

        Uri outputFileUri = getCaptureImageOutputUri(mContext);

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = mContext.getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    private String getImageFromFilePath(Context mContext, Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri(mContext).getPath();
        else return getPathFromURI(mContext, data.getData());

    }

    public String getImageFilePath(Context mContext, Intent data) {
        return getImageFromFilePath(mContext, data);
    }

    private String getPathFromURI(Context mContext, Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            picUri = savedInstanceState.getParcelable("pic_uri");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_RESULT) {
                String filePath = getImageFilePath(getContext(), data);
                if (filePath != null) {
                    Toast.makeText(getContext(), "새로운 데이따~: " + filePath, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onActivityResult: FileCreated:" + filePath);
                }
                uploadImage(getContext(), filePath);
            }

        }
    }

    private Bitmap rotateFromFilePath(String filePath) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        int rot = 0;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rot = 1;
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rot = 2;
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rot = 3;
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
                rot = 4;
            default:
                rot = 4;
                rotatedBitmap = bitmap;
        }
        Log.d(TAG, "rotateFromFilePath: Rotated![" + rot + "]");
        return rotatedBitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
