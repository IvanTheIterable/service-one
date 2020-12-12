package service.one.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import service.one.model.ImportantData;
import service.one.service.RemoteService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@FeignClient("service-two")
public interface ServiceTwo extends RemoteService {

    @GetMapping("/")
    ImportantData process(@RequestBody String redisId) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;

}
