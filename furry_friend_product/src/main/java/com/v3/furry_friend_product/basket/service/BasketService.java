package com.v3.furry_friend_product.basket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v3.furry_friend_product.basket.dto.BasketRequestDTO;
import com.v3.furry_friend_product.basket.dto.BasketResponseDTO;
import com.v3.furry_friend_product.basket.entity.Basket;
import com.v3.furry_friend_product.basket.repository.BasketRepository;
import com.v3.furry_friend_product.product.dto.ProductDTO;
import com.v3.furry_friend_product.product.dto.ProductImageDTO;
import com.v3.furry_friend_product.product.entity.Product;
import com.v3.furry_friend_product.product.entity.ProductImage;
import com.v3.furry_friend_product.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class BasketService {

    private final BasketRepository basketRepository;

    private final ProductRepository productRepository;

    // 장바구니 읽어오기
    public BasketResponseDTO findBasketList(Long memberId){

        try {
            List<Object []> result = basketRepository.basketByMember(memberId);

            Product product = (Product) result.get(0)[1];

            List<ProductImage> productImageList = new ArrayList<>();
            result.forEach(arr -> {
                ProductImage productImage = (ProductImage) arr[2];
                productImageList.add(productImage);
            });
            return entityToDTO((long) result.get(0)[0], product, productImageList, memberId);
        }
        catch (Exception e){
            log.error(e);
            return null;
        }

    }

    // 장바구니 삭제하기
    @Transactional
    public void deleteBasketItem(BasketRequestDTO basketRequestDTO, Long memberId){
        basketRepository.deleteBasketByBasket_id(basketRequestDTO.getBid(), memberId);
    }

    public void saveBasket(String pname, Long memberId){
        Product product = productRepository.findProductByPname(pname);
        Basket basket = Basket.builder()
            .product(product)
            .memberid(memberId).build();
        basketRepository.save(basket);
    }

    // Entity를 DTO로 변경해주는 메서드
    public BasketResponseDTO entityToDTO(Long bid, Product product, List<ProductImage> productImageList, Long mid){

        List<ProductImageDTO> productImageDTOList = new ArrayList<>();
        productImageList.forEach(arr -> {
            ProductImageDTO productImageDTO = ProductImageDTO.builder()
                .imgName(arr.getImgName())
                .path(arr.getPath())
                .uuid(arr.getUuid())
                .build();
            productImageDTOList.add(productImageDTO);
        });

        ProductDTO productDTO = ProductDTO.builder()
            .pid(product.getPid())
            .pname(product.getPname())
            .imageDTOList(productImageDTOList)
            .build();

        return BasketResponseDTO.builder()
            .bid(bid)
            .productDTO(productDTO)
            .mid(mid)
            .build();
    }
}
