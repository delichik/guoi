package moe.imiku.guoi.Model;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageViewLoaderItem {
    private ImageView imageView;
    private Bitmap bitmap;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
