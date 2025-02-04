package br.com.emersondias.ebd.config;

import br.com.emersondias.ebd.constants.RouteConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("EBD - Escola Bíblica Dominical"))
                .components(new Components()
                        .addSchemas("CredentialsDTO", new Schema<>()
                                .addProperty("username", new Schema<>().type("String"))
                                .addProperty("password", new Schema<>().type("String"))
                        )
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .path(RouteConstants.AUTH_ROUTE + "/login", new PathItem()
                        .post(new Operation()
                                .summary("Login Endpoint")
                                .description("Endpoint para realizar login e gerar o token JWT")
                                .requestBody(new RequestBody()
                                        .required(true)
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new Schema().$ref("#/components/schemas/CredentialsDTO"))
                                                )
                                        )
                                )
                        ));

    }
}
