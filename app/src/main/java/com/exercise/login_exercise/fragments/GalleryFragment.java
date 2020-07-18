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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.login_exercise.R;
import com.exercise.login_exercise.adapters.GalleryAdapter;
import com.exercise.login_exercise.api.RetrofitClient;
import com.exercise.login_exercise.models.DefaultResponse;
import com.exercise.login_exercise.models.ImagesResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jongwow on 2020-07-18.
 */
public class GalleryFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "GalleryFragment";

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private ArrayList<String> urls;
    private ProgressBar progressBar;
    private Context mContext;
    FloatingActionButton fabCamera, fabUpload;


    private final static int IMAGE_RESULT = 200;
    Uri picUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        fabCamera = view.findViewById(R.id.fabCamera);
        fabUpload = view.findViewById(R.id.fabUpload);
        fabCamera.setOnClickListener(this);
        fabUpload.setOnClickListener(this);

        Call<ImagesResponse> call = RetrofitClient.getInstance().getApi().getImages();

        // Set up progress before call
        progressBar = view.findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ImagesResponse>() {
            @Override
            public void onResponse(Call<ImagesResponse> call, Response<ImagesResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    urls = response.body().getUrls();
                    Log.d(TAG, "onResponse: getUrls" + urls);
                    adapter = new GalleryAdapter(getActivity(), urls);
                    recyclerView.setAdapter(adapter);
                } else {

                    Toast.makeText(mContext, "잘못된 요청[" + response.code() + "]", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImagesResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "요청 실패!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabCamera:
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                Toast.makeText(mContext, "fabCamera", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: RandomGeneratedString:" + generateRandomString());
                break;

            case R.id.fabUpload:
//                    Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "fabUpload", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void uploadImage(String path) {

        try {

            // 해당 경로에 이미지가 존재하는지 확인
            File oldImage = new File(path);
            if (!oldImage.exists()) {
                Toast.makeText(mContext, " 파일이 존재하지 않습니다. ", Toast.LENGTH_SHORT).show();
                return;
            }

            // 카메라로 찍었을 경우 90도 회전이 되는데 그걸 다시 돌려주는 작업
//            File f = new File(mContext.getCacheDir(), oldImage.getName());
//            f.createNewFile();
//
//            Bitmap bitmap = rotateFromFilePath(path);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
//            byte[] bitmapdata = bos.toByteArray();
//
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), oldImage);
            MultipartBody.Part parts = MultipartBody.Part.createFormData("img", oldImage.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "img");


            Call<DefaultResponse> call = RetrofitClient.getInstance()
                    .getApi()
                    .postImage(parts, name);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    if (response.code() == 200) {
                        Toast.makeText(mContext, "성공:" + response.code() + " ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "실패!:" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Toast.makeText(mContext, "요청 실패!", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d(TAG, "uploadImage: Error");
            e.printStackTrace();
        }
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = mContext.getExternalFilesDir("");
        if (getImage != null) {
            Log.d(TAG, "getCaptureImageOutputUri:  is NULL");
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
            Log.d(TAG, "getCaptureImageOutputUri: from null to" + outputFileUri.toString());
        }
        return outputFileUri;
    }

    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

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

    private String generateRandomString() {
        StringBuilder temp = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }


    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
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
                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    Toast.makeText(mContext, "오예ㅖ~~ 데이터가 생성됐어요!" + filePath, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onActivityResult: FileCreated:" + filePath);
                }
                uploadImage(filePath);
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
                rot =1;
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rot =2;
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rot =3;
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
                rot =4;
            default:
                rot =4;
                rotatedBitmap = bitmap;
        }
        Log.d(TAG, "rotateFromFilePath: Rotated!["+rot+"]");
        return rotatedBitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
