package com.implemica.application.util.application;

import com.implemica.application.CarCatalogApplication;
import org.junit.jupiter.api.Test;

// Test class added ONLY to cover main() invocation not covered by application tests.
public class ApplicationTest {
    @Test
    public void main() {
        CarCatalogApplication.main(new String[] {});
    }
}
