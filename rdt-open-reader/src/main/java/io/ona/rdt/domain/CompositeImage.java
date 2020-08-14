package io.ona.rdt.domain;

import android.graphics.Bitmap;

/**
 * Created by Vincent Karuri on 18/09/2019
 */
public class CompositeImage {

    private Bitmap fullImage;

    private Bitmap croppedImage;

    private ParcelableImageMetadata parcelableImageMetadata;

    private UnParcelableImageMetadata unParcelableImageMetadata;

    private String fullImageFilePath;

    private String croppedImageFilePath;

    public Bitmap getFullImage() {
        return fullImage;
    }

    public Bitmap getCroppedImage() {
        return croppedImage;
    }

    public ParcelableImageMetadata getParcelableImageMetadata() {
        return parcelableImageMetadata;
    }

    public UnParcelableImageMetadata getUnParcelableImageMetadata() {
        return unParcelableImageMetadata;
    }

    public String getFullImageFilePath() {
        return fullImageFilePath;
    }

    public void setFullImageFilePath(String fullImageFilePath) {
        this.fullImageFilePath = fullImageFilePath;
    }

    public String getCroppedImageFilePath() {
        return croppedImageFilePath;
    }

    public void setCroppedImageFilePath(String croppedImageFilePath) {
        this.croppedImageFilePath = croppedImageFilePath;
    }

    public void setFullImage(Bitmap fullImage) {
        this.fullImage = fullImage;
    }

    public void setCroppedImage(Bitmap croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void setParcelableImageMetadata(ParcelableImageMetadata parcelableImageMetadata) {
        this.parcelableImageMetadata = parcelableImageMetadata;
    }

    public void setUnParcelableImageMetadata(UnParcelableImageMetadata unParcelableImageMetadata) {
        this.unParcelableImageMetadata = unParcelableImageMetadata;
    }

    public CompositeImage withFullImage(Bitmap fullImage) {
        setFullImage(fullImage);
        return this;
    }

    public CompositeImage withCroppedImage(Bitmap croppedImage) {
        setCroppedImage(croppedImage);
        return this;
    }

    public CompositeImage withParcelableImageMetadata(ParcelableImageMetadata parcelableImageMetadata) {
        setParcelableImageMetadata(parcelableImageMetadata);
        return this;
    }

    public CompositeImage withUnParcelableImageMetadata(UnParcelableImageMetadata unParcelableImageMetadata) {
        setUnParcelableImageMetadata(unParcelableImageMetadata);
        return this;
    }

    public CompositeImage withFullImageFilePath(String fullImageFilePath) {
        setFullImageFilePath(fullImageFilePath);
        return this;
    }

    public CompositeImage withCroppedImageFilePath(String croppedImageFilePath) {
        setCroppedImageFilePath(croppedImageFilePath);
        return this;
    }
}
