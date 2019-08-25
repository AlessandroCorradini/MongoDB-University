package mflix.api.controllers;

import mflix.api.services.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Configuration
@ComponentScan({"config", "entity", "security"})
public abstract class ApiController {

  @Autowired protected TokenAuthenticationService tokenProvider;

  @ResponseBody
  @GetMapping(value = "info")
  public ResponseEntity<Map> info() {

    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("message", "hello from MFlix API");

    return ResponseEntity.ok(responseMap);
  }

  protected String getEmailFromRequest(String request) {
    String jwt = request.substring(7);
    return tokenProvider.getAuthenticationUser(jwt);
  }

  @GetMapping(value = "/")
  abstract ResponseEntity<Map> index();
}
