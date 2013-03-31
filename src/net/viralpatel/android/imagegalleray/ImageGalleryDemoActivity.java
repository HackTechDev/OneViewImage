package net.viralpatel.android.imagegalleray;

/*
 * http://viralpatel.net/blogs/pick-image-from-galary-android-app/
 * 
 */

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageGalleryDemoActivity extends Activity {
    
	String picturePath = "";
	
	private static int RESULT_LOAD_IMAGE = 1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				// Debug
				Toast.makeText(ImageGalleryDemoActivity.this,
						picturePath, Toast.LENGTH_SHORT).show();
				
				// Delete file
				if(picturePath != null){
					File file = new File(picturePath);
					boolean deleted = file.delete();
					
					/*
					 * http://stackoverflow.com/questions/12130661/deleted-image-still-shows-in-android-gallery-untill-i-restart-the-emaulator
					 */
					// request scan    
					Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					scanIntent.setData(Uri.fromFile(file));
					sendBroadcast(scanIntent);

					/*
					 * http://alensiljak.blogspot.fr/2011/01/clear-gallery-thumbnails-on-android.html
					 */
					
					File fileIndex = new File("/mnt/sdcard/Android/data/com.cooliris.media/cache/local-album-cache/index");
					boolean deletedIndex = fileIndex.delete();
										
					File fileChunk0 = new File("/mnt/sdcard/Android/data/com.cooliris.media/cache/local-album-cache/chunk_0");
					boolean deletedChunk0 = fileChunk0.delete();	
					
				}
				
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			ImageView imageView = (ImageView) findViewById(R.id.imgView);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		
		}
    }
}