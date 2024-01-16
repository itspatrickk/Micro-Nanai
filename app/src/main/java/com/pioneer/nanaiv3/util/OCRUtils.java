package com.pioneer.nanaiv3.util;

//import com.googlecode.tesseract.android.TessBaseAPI;

public class OCRUtils {

//    private static final String TESS_DATA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
//    private static final String TESS_LANG = "eng"; // Language code for the trained data
//
//    public static String performOCR(Context context, Bitmap bitmap) {
//
//        String path = context.getFilesDir().getAbsolutePath();
//
//        Toast.makeText(context, path + " path.", Toast.LENGTH_SHORT).show();
//
//        TessBaseAPI tessBaseAPI = new TessBaseAPI();
//        tessBaseAPI.init(path, TESS_LANG);
//        tessBaseAPI.setImage(bitmap);
//
//        String recognizedText = tessBaseAPI.getUTF8Text();
//        tessBaseAPI.end();
//
//        return recognizedText;
//    }
//
//    public static Bitmap loadBitmapFromPath(String imagePath) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        return BitmapFactory.decodeFile(imagePath, options);
//    }
}
