package service.one.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import service.one.repository.Redis;
import service.one.service.RemoteService;
import service.one.model.Bytes;
import service.one.model.ImportantData;
import service.one.model.Response;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final RemoteService remoteService;
    private final Redis redis;

    private int len = 200 * 1024;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Response home() {
        Response response = new Response();
        byte[] randomBytes = new byte[len];
        new SecureRandom().nextBytes(randomBytes);
        Bytes bytes = new Bytes();
        bytes.setPayload(randomBytes);
        String redisId = redis.save(bytes).getId();

        try {
            ImportantData data = remoteService.process(redisId);
            PublicKey publicKey = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(data.getPublicKey()));
            PrivateKey privateKey = KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(data.getPrivateKey()));

            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.initVerify(publicKey);
            signature.update(randomBytes);

            response.setVerified(signature.verify(redis.findById(data.getRedisId()).get().getPayload()));

            redis.deleteById(redisId);
            redis.deleteById(data.getRedisId());
        } catch (Exception e) {
            response.setException(e.toString());
            e.printStackTrace();
        }
        return response;
    }
}
