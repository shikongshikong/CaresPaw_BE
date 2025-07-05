package com.example.carespawbe.config;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.apache.tomcat.util.http.fileupload.impl.FileCountLimitExceededException;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
//
//    @Override
//    public void customize(TomcatServletWebServerFactory factory) {
//        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
//            connector.setProperty("maxSwallowSize", "-1");
//            connector.setProperty("maxParameterCount", "10000");
//            // 👇 Đây là dòng quan trọng để sửa lỗi FileCountLimitExceededException
//            connector.setProperty("fileCountMax", "50"); // Tăng tùy bạn muốn bao nhiêu file
//        });
//    }
//}
@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            connector.setMaxPartCount(10); // Giới hạn số lượng phần multipart (bao gồm tệp)
        });
    }
}
