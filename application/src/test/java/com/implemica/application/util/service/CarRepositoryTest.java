package com.implemica.application.util.service;

import com.implemica.application.CarCatalogApplication;
import com.implemica.config.WebConfiguration;
import com.implemica.model.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static com.implemica.application.util.helpers.TestValues.EXAMPLE_CAR;
import static com.implemica.application.util.helpers.TestValues.EXAMPLE_CAR_WITHOUT_ID;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class CarRepositoryTest {
    @Test
    public void oneTest() {
        while (true) {

        }
        //carRepository.save(EXAMPLE_CAR_WITHOUT_ID);
        //assertEquals(EXAMPLE_CAR, carRepository.findById(1l).orElseThrow());

    }
}

