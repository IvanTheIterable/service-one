package service.one.repository;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import service.one.model.Bytes;

public interface Redis extends KeyValueRepository<Bytes, String> {

}
