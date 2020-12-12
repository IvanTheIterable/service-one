package service.one.model;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Bytes {
    @Id
    private String id;
    private byte[] payload;
}
