// đường dẫn: src/main/java/com/example/carespawbe/DBStartupTest.java
package com.example.carespawbe;

import com.example.carespawbe.repository.shop.CategoryRepository;
import com.example.carespawbe.repository.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBStartupTest implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;

    @Override
    public void run(String... args) {
        System.out.println("📌 DB Test START");
        System.out.println("Category 4 exists? " + categoryRepository.findById(4L).isPresent());
        System.out.println("Shop 4 exists? " + shopRepository.findById(4L).isPresent());
        System.out.println("📌 DB Test END");
    }
}
