package com.v3.furry_friend_product.product.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    private String imgName;
    private String uuid;
    private String path;
    //실제 이미지 경로를 리턴해주는 메서드
    public String getImageURL(){
        try {
            return URLEncoder.encode(path + "/" + uuid + imgName, "UTF-8");
        }catch (UnsupportedEncodingException e){
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return "";
    }

    //Thumbnail 이미지 경로를 리턴하는 메서드
    public String getThumbnailURL(){
        try {
            return URLEncoder.encode(path + "/" + uuid + imgName, "UTF-8");
        }catch (UnsupportedEncodingException e){
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return "";
    }
}
