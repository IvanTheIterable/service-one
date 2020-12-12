package service.one.model;

import lombok.Data;

@Data
public class ImportantData {
    private String redisId;
    private byte[] publicKey;
    private byte[] privateKey;
}
