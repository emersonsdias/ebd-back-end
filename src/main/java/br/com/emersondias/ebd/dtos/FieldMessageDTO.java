package br.com.emersondias.ebd.dtos;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FieldMessageDTO implements Serializable {

    private String fieldName;
    private Object fieldValue;
    private String message;

    @Override
    public String toString() {
        return "{" +
                "\"fieldName\":\"" + fieldName + "\"" +
                ", \"message\":\"" + message + "\"" +
                ", \"fieldValue\":\"" + fieldValue + "\"" +
                "}";
    }
}
