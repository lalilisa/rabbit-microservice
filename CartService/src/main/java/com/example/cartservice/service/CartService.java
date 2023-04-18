package com.example.cartservice.service;

import com.example.cartservice.entity.Cart;
import com.example.cartservice.model.CartCommand;
import com.example.cartservice.model.MessageData;
import com.example.cartservice.model.UsernameResponse;
import com.example.cartservice.repository.CartRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    CartRepo cartRepo;

    public CartService(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Query(value = "select c from Cart c where  c.userName =:username")
    public List<Cart> getCartByUserId(String username) {
        return cartRepo.getCartByUserId(username);
    }

    public List<Cart> findAll() {
        return cartRepo.findAll();
    }

    public <S extends Cart> S save(S entity) {
        return cartRepo.save(entity);
    }

    public Cart getSameProduct(String username,Long productId){
        return cartRepo.findByUserNameAndProductId(username,productId);
    }
    @Value("${cosmetics.rabbitmq.exchange}")
    String exchange;

    @Value("${cosmetics.rabbitmq.routingkey-user}")
    private String routingkey;

    @Qualifier("rabbitBeanReq")
    @Autowired
    private RabbitTemplate rabbitTemplateReq;

    public List<Cart> getCart(String username){
        return cartRepo.getCartByUserId(username);
    }

    public void buyProduct(List<Long> ids){
        List<Cart> list = cartRepo.findAll();
        for(Cart cart: list){
            if(ids.contains(cart.getId())){
                cart.setStatus(0);
                cartRepo.save(cart);
            }
        }
    }

    public Cart addtoCart(CartCommand cartCommand) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        Object res=null;
        String username=null;
        if(cartCommand.getUsername()==null || cartCommand.getUsername().isEmpty()) {
            res = rabbitTemplateReq.convertSendAndReceive(exchange, routingkey, new MessageData("", "CREATE_USER", null));
            UsernameResponse usernameResponse=objectMapper.readValue(res.toString(),UsernameResponse.class);
            username=usernameResponse.getUsername();
        }
        else
            username=cartCommand.getUsername();
        Cart checkExistCart = checkExistCart=getSameProduct(username,cartCommand.getProductId());
        if(checkExistCart!=null){
            checkExistCart.setQuantity(checkExistCart.getQuantity()+cartCommand.getQuantity());
            return cartRepo.save(checkExistCart);
        }
        Cart cart= Cart.builder()
                .productId(cartCommand.getProductId())
                .quantity(cartCommand.getQuantity())
                .userName(username)
                .build();
        return cartRepo.save(cart);
    }
}
