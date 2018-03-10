package com.example.com.wisdomcommunity.util.photo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

public class CameraUtils {

    private CameraUtils() {
        super();
    }

    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            // this device has a front camera
            return true;
        } else if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has any camera
            return true;
        }
        return false;
    }

    public static Bitmap createThumb(File file, int maxWidth, int maxHeight) {
        // 读取参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        // 防止宽高取值错误
        if (options.outHeight == -1 || options.outWidth == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
                int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                options.outWidth = width;
                options.outHeight = height;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 计算压缩比
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inSampleSize = 1;
        if (height > maxHeight || width > maxWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) maxHeight);
            final int widthRatio = Math.round((float) width / (float) maxWidth);
            // Choose the smallest ratio as inSampleSize value, this will guaranteea final image with both dimensions larger than or equal to the requested height and width.
            options.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        // 压缩
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        // 读取图片旋转角度
        int degrees = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90: {
                    degrees = 90;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_180: {
                    degrees = 180;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_270: {
                    degrees = 270;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 设置图片旋转角度
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    public static String saveToPhotoAlbum(Context context, Bitmap bitmap, String srcPath, String title, String description) {
        String desPath = null;
        Cursor cursor = null;
        try {
            ContentResolver cr = context.getContentResolver();
            String uri;
            if (bitmap != null) {
                uri = MediaStore.Images.Media.insertImage(cr, bitmap, title, description);
            } else {
                uri = MediaStore.Images.Media.insertImage(cr, srcPath, !TextUtils.isEmpty(title) ? title : new File(srcPath).getName(), description);
            }
            cursor = MediaStore.Images.Media.query(cr, Uri.parse(uri), new String[]{MediaStore.Images.Media.DATA});
            if (cursor.moveToNext()) {
                desPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (desPath != null) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(desPath))));
        }
        return desPath;
    }

    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int outputX, int outputY) {
        return buildImageCropIntent(uriFrom, uriTo, outputX, outputY, false);
    }

    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int outputX, int outputY, boolean returnData) {
        return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData);
    }

    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int aspectX, int aspectY, int outputX, int outputY, boolean returnData) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uriFrom, "image/*");
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//		}
//		intent.putExtra("crop", "true");
//		if (uriTo != null) {
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTo);
//		}
//		intent.putExtra("aspectX", aspectX);
//		intent.putExtra("aspectY", aspectY);
//		intent.putExtra("outputX", outputX);
//		intent.putExtra("outputY", outputY);
//		intent.putExtra("scale", true);
//		intent.putExtra("return-data", returnData);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTo);
////		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
//
//		intent.putExtra("output", uriFrom); // 输出的图片路径
////		intent.setType("image/*");

        // 创建File对象，用于存储裁剪后的图片，避免更改原图
//		File file = new File(getExternalCacheDir(), "crop_image.jpg");
//		try {
//			if (file.exists()) {
//				file.delete();
//			}
//			file.createNewFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		uriTo = Uri.fromFile(file);

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uriFrom, "image/*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        //intent.putExtra("outputX", 400);
        //intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTo);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        return intent;
    }

    public static Intent buildOpenSysCameraIntent(Uri uriTo) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (uriTo != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTo);
        }
        return intent;
    }

    public static Intent buildOpenSysGalleryIntent() {
        return buildOpenSysGalleryIntent(false);
    }

    public static Intent buildOpenSysGalleryIntent(boolean returnData) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data", returnData); //true会把图像bitmap对象的数据传回来，太大，忌用
        return intent;
    }

//	public static boolean hasCamera(Context context) {
//		PackageManager packageManager = context.getPackageManager();
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//		return list.size() > 0;
//	}

//	public static void openSysCamera(Activity activity, int requestCode, String outputFilePath) {
//		try {
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputFilePath)));
//			activity.startActivityForResult(intent, requestCode);
//		} catch (Exception e) {
//			Log.d("Camera", "$$$ No Camera.");
//		}
//	}

//	public static boolean hasGallery(Context context) {
//		PackageManager packageManager = context.getPackageManager();
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//		return list.size() > 0;
//	}

//	public static void openSysGallery(Activity activity, int requestCode) {
//		try {
//			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//			intent.addCategory(Intent.CATEGORY_OPENABLE);
//			intent.setType("image/*");
//			intent.putExtra("return-data", false); //true会把图像bitmap对象的数据传回来，太大，忌用
//			activity.startActivityForResult(intent, requestCode);
//		} catch (Exception e) {
//			Log.d("Gallery", "$$$ No Gallery.");
//		}
//	}

    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {// MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) {
                return imageUri.getLastPathSegment();
            }
            return getDataColumn(context, imageUri, null, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {// File
            return imageUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
