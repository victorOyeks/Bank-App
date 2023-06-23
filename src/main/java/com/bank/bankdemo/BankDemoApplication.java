package com.bank.bankdemo;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "This is a Bank App",
                description = "A Bank App that makes all banking transactions",
                version = "v1.0",
                contact = @Contact(
                        name = "Victor Oyeks",
                        email = "bolaoyeks@gmail.com"
                ),
                license = @License(
                        name = "Oyeks Code"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Oyeks Banking App"
        )
)
public class BankDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankDemoApplication.class, args);
    }
}
