package com.example.com.wisdomcommunity.util.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PhotosClient {

    private Activity activity;
    private Fragment fragment;

    private final int cropWidth;
    private final int cropHeight;

    private final File cropGalleryDir;

    private final int requestCodeTakeAPicture;
    private final int requestCodeSelectFromAlbum;
    private final int requestCodeCropPicture;

    private Callback callback;

    // 兼容旧版相机和相册（像小米手机之属，依然用旧版相机和相册，而不用新版相机和相册）
    private Uri takeAPictureUri;
    private Uri cropPictureUri;

    private PhotosClient(Activity activity, int cropWidth, int cropHeight, File cropGalleryDir, int requestCodeTakeAPicture, int requestCodeSelectFromAlbum, int requestCodeCropPicture, Callback callback) {
        super();
        this.activity = activity;
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
        this.cropGalleryDir = cropGalleryDir;
        this.requestCodeTakeAPicture = requestCodeTakeAPicture;
        this.requestCodeSelectFromAlbum = requestCodeSelectFromAlbum;
        this.requestCodeCropPicture = requestCodeCropPicture;
        this.callback = callback;
    }

    private PhotosClient(Fragment fragment, int cropWidth, int cropHeight, File cropGalleryDir, int requestCodeTakeAPicture, int requestCodeSelectFromAlbum, int requestCodeCropPicture, Callback callback) {
        super();
        this.fragment = fragment;
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
        this.cropGalleryDir = cropGalleryDir;
        this.requestCodeTakeAPicture = requestCodeTakeAPicture;
        this.requestCodeSelectFromAlbum = requestCodeSelectFromAlbum;
        this.requestCodeCropPicture = requestCodeCropPicture;
        this.callback = callback;
    }

    private Context getContext() {
        return activity != null ? activity : fragment.getContext();
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    public void takeAPicture() {
        try {
            File picture = new File(cropGalleryDir, randomString() + ".jpg");

            if (Build.VERSION.SDK_INT >= 24) {
                takeAPictureUri = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getApplicationContext().getPackageName() + ".provider", picture);
            } else {
                takeAPictureUri = Uri.fromFile(picture);
            }
            Intent intent = CameraUtils.buildOpenSysCameraIntent(takeAPictureUri);
            startActivityForResult(intent, requestCodeTakeAPicture);
        } catch (Exception ignored) {
            Log.d("Camera", "$$$ No Camera.");
        }
    }

    public void selectFromAlbum() {
        try {
            Intent intent = CameraUtils.buildOpenSysGalleryIntent();
            startActivityForResult(intent, requestCodeSelectFromAlbum);
        } catch (Exception ignored) {
            Log.d("Gallery", "$$$ No Gallery.");
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCodeTakeAPicture) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        String imageAbsolutePath = CameraUtils.getImageAbsolutePath(getContext(), data.getData());
                        if (!TextUtils.isEmpty(imageAbsolutePath)) {
                            File imageFile = new File(imageAbsolutePath);
                            cropPicture(Uri.fromFile(imageFile));
                        } else {
                            cropPicture(uri);
                        }
                    } else {
                        cropPicture(uri);
                    }
                } else {
                    cropPicture(takeAPictureUri);
                }
            }
            return true;
        } else if (requestCode == requestCodeSelectFromAlbum) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        String imageAbsolutePath = CameraUtils.getImageAbsolutePath(getContext(), data.getData());
                        if (!TextUtils.isEmpty(imageAbsolutePath)) {
                            File imageFile = new File(imageAbsolutePath);
                            cropPicture(Uri.fromFile(imageFile));
                        } else {
                            cropPicture(uri);
                        }
                    } else {
                        cropPicture(uri);
                    }
                }
            }
            return true;
        } else if (requestCode == requestCodeCropPicture) {
            if (resultCode == Activity.RESULT_OK) {
                // delay after onResume
                if (callback != null) {
                    callback.onCropPicture(data != null && data.getData() != null ? data.getData() : cropPictureUri);
                }
            }
        }
        return false;
    }


    private void cropPicture(Uri uri) {
        try {
            // 创建File对象，用于存储裁剪后的图片，避免更改原图
            File file = new File(cropGalleryDir, "crop_image.jpg");
            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            cropPictureUri = Uri.fromFile(file);
            Intent intent = CameraUtils.buildImageCropIntent(uri, cropPictureUri, cropWidth, cropHeight, false);
            startActivityForResult(intent, requestCodeCropPicture);
        } catch (Exception ignored) {
            Log.d("Camera", "$$$ No Gallery.");
        }
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    public interface Callback {
        public void onCropPicture(Uri pictureUri);
    }

    public static class Builder {

        private static final int REQUEST_CODE_TAKE_A_PICTURE = 10086;
        private static final int REQUEST_CODE_SELECT_FROM_ALBUM = 10087;
        private static final int REQUEST_CODE_CROP_PICTURE = 10088;

        private static final int CROP_SIZE = 200;

        private static final String GALLERY_DIR = "gallery";

        private Activity activity;
        private Fragment fragment;

        private int cropWidth = CROP_SIZE;
        private int cropHeight = CROP_SIZE;

        private File cropGalleryDir;

        private int requestCodeTakeAPicture = REQUEST_CODE_TAKE_A_PICTURE;
        private int requestCodeSelectFromAlbum = REQUEST_CODE_SELECT_FROM_ALBUM;
        private int requestCodeCropPicture = REQUEST_CODE_CROP_PICTURE;

        private Callback callback;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder(Fragment fragment) {
            this.fragment = fragment;
        }

        public Builder cropWidth(int cropWidth) {
            this.cropWidth = cropWidth;
            return this;
        }

        public Builder cropHeight(int cropHeight) {
            this.cropHeight = cropHeight;
            return this;
        }

        public Builder cropSize(int cropSize) {
            this.cropWidth = cropSize;
            this.cropHeight = cropSize;
            return this;
        }

        public Builder cropGalleryDir(File cropGalleryDir) {
            this.cropGalleryDir = cropGalleryDir;
            return this;
        }

        public Builder requestCodeTakeAPicture(int requestCodeTakeAPicture) {
            this.requestCodeTakeAPicture = requestCodeTakeAPicture;
            return this;
        }

        public Builder requestCodeSelectFromAlbum(int requestCodeSelectFromAlbum) {
            this.requestCodeSelectFromAlbum = requestCodeSelectFromAlbum;
            return this;
        }

        public Builder requestCodeCropPicture(int requestCodeCropPicture) {
            this.requestCodeCropPicture = requestCodeCropPicture;
            return this;
        }

        public Builder callback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public PhotosClient build() {
            if (activity != null) {
                if (cropGalleryDir == null) {
                    cropGalleryDir = new File(activity.getCacheDir(), GALLERY_DIR);
                }
                if (!cropGalleryDir.exists()) {
                    cropGalleryDir.mkdirs();
                }
                return new PhotosClient(activity, cropWidth, cropHeight, cropGalleryDir, requestCodeTakeAPicture, requestCodeSelectFromAlbum, requestCodeCropPicture, callback);
            }
            if (fragment != null) {
                if (cropGalleryDir == null) {
                    cropGalleryDir = new File(fragment.getContext().getCacheDir(), GALLERY_DIR);
                }
                if (!cropGalleryDir.exists()) {
                    cropGalleryDir.mkdirs();
                }
                return new PhotosClient(fragment, cropWidth, cropHeight, cropGalleryDir, requestCodeTakeAPicture, requestCodeSelectFromAlbum, requestCodeCropPicture, callback);
            }
            throw new IllegalArgumentException("Both activity and fragment is null.");
        }
    }
}
