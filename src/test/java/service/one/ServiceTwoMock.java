package service.one;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import service.one.feign.ServiceTwo;
import service.one.model.Bytes;
import service.one.model.ImportantData;
import service.one.repository.Redis;

import java.security.*;

@Component
@RequiredArgsConstructor
public class ServiceTwoMock implements ServiceTwo {
    private final Redis redis;

    public ImportantData process(String redisId) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        KeyPair pair = KeyPairGenerator.getInstance("EC").generateKeyPair();
        signature.initSign(pair.getPrivate());
        signature.update(redis.findById(redisId).get().getPayload());

        Bytes bytes = new Bytes();
        bytes.setPayload(signature.sign());

        ImportantData data = new ImportantData();
        data.setRedisId(redis.save(bytes).getId());
        data.setPublicKey(pair.getPublic().getEncoded());
        data.setPrivateKey(pair.getPrivate().getEncoded());
        return data;
    }

}
