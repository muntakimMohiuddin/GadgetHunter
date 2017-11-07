package com.example.mks.gadgethunter.Models;

/**
 * Created by HP on 24-Oct-17.
 * <p>
 * Created by kibria on 24/10/17.
 * <p>
 * Created by kibria on 24/10/17.
 * <p>
 * Created by kibria on 24/10/17.
 */


/**
 * Created by kibria on 24/10/17.
 */


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kibria on 24/10/17.
 */

public class Product implements Parcelable {
    // public String shopId;
    public String productName;
    public String productCategory;
    public String productAvailability;
    public String productPrice;
    public String imageUrl;
    public String productId;
    public String productDescription;
    public String productBrand;


    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }


    public Product(String productName, String productCategory, String productAvailability, String productPrice, String imageUrl, String productId, String productDescription, String productBrand) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productAvailability = productAvailability;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.productId = productId;
        this.productDescription = productDescription;
        this.productBrand = productBrand;

    }

    protected Product(Parcel in) {
        productName = in.readString();
        productCategory = in.readString();
        productAvailability = in.readString();
        productPrice = in.readString();
        imageUrl = in.readString();
        productId = in.readString();
        productDescription = in.readString();
        productBrand = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductAvailability() {
        return productAvailability;
    }

    //muntsa
    public String getProductPrice() {
        return productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductBrand() {
        return productBrand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(productCategory);
        parcel.writeString(productAvailability);
        parcel.writeString(productPrice);
        parcel.writeString(imageUrl);
        parcel.writeString(productId);
        parcel.writeString(productDescription);
        parcel.writeString(productBrand);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productAvailability='" + productAvailability + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", productId='" + productId + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productBrand='" + productBrand + '\'' +
                '}';
    }
}
