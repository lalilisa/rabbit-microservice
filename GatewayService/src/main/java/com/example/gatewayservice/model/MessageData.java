package com.example.gatewayservice.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class MessageData implements Serializable {

    private String token;
    private String target;
    private Object data;
}
