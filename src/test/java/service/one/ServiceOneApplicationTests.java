package service.one;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import service.one.api.Controller;
import service.one.model.Bytes;
import service.one.model.Response;
import service.one.repository.Redis;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class ServiceOneApplicationTests {
    @Autowired
    private Redis redis;

    @Autowired
    private ServiceTwoMock serviceTwoMock;

    @Autowired
    private Controller controller;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(controller, "remoteService", serviceTwoMock);
        ReflectionTestUtils.setField(controller, "len", 200);
    }

    @Test
    void testLocalRedis() {
        Bytes bytes = new Bytes();
        bytes.setPayload("123".getBytes());
        String id = redis.save(bytes).getId();
        Optional<Bytes> opt = redis.findById(id);
        assertArrayEquals(bytes.getPayload(), opt.get().getPayload());
    }

    @Test
    void testApplication() throws Exception {
        Response response = om.readValue(
                mvc.perform(MockMvcRequestBuilders.get("/"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                Response.class);
        assertTrue(response.isVerified());
        assertNull(response.getException());
    }

}
