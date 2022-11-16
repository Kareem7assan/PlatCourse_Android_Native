package com.rowaad.app.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressLint("NewApi")
public class FileOperations 

{
	final int PICK_FROM_CAMERA = 1888;
    final int LOAD_FROM_GALLERY = 100;

	public String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */

	public String getDataColumn(Context context, Uri uri, String selection,
								String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int columnIndex = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(columnIndex);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public  boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public  boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public  boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	
   public void selectImage(final Context context )
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(context);
       builder.setTitle("Choose Image Source");
       builder.setItems(new CharSequence[]{"Gallery", "Camera"},
               new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       switch (which) {
                           case 0:
                               Intent galleryIntent = new Intent();
                               galleryIntent.setType("image/*");
                               galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                               ((Activity) context).startActivityForResult(galleryIntent, LOAD_FROM_GALLERY);
                               break;
                           case 1:
                               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                               ((Activity) context).startActivityForResult(intent,
                                       PICK_FROM_CAMERA);
                               break;

                           default:
                               break;
                       }
                   }
               });

       builder.show(); 
   }
   
   
   public void selectFile(final Context context , final String type)
   {
        Intent galleryIntent = new Intent();
        galleryIntent.setType(type+"/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
      ((Activity) context).startActivityForResult(galleryIntent, LOAD_FROM_GALLERY);
   }
   

  
   
   public String getFileAsString(final Context context, final Uri uri)
   {
	 return fileToString(getPath(context,uri));
   }
  
   public String fileToString(String selectedPath)
   {
	   FileInputStream inputStream = null;
       String str_image="", pathToOurFile = selectedPath;
       ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
       try {
           inputStream = new FileInputStream(new File(pathToOurFile));
           
           int bufferSize = 16777216;
           byte[] buffer = new byte[bufferSize];

           
           int len = 0;
           while ((len = inputStream.read(buffer)) != -1) {
             byteBuffer.write(buffer, 0, len);
           }
           try {
        	   inputStream.close();
			}
           catch (IOException e)
           {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           str_image = Base64.encodeToString(byteBuffer.toByteArray(), 0);
               
       } catch (Exception ex) {
       }
       
       return str_image;
   }
	public String BitMapToString(Bitmap bitmap){
		ByteArrayOutputStream baos=new  ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
		byte [] b=baos.toByteArray();
		String temp=Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}
  	
	
}
