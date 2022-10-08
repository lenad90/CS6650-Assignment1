package com.Controller;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @RequestMapping(value = "/skiers/", method = RequestMethod.GET)
  @ResponseStatus(code = HttpStatus.OK)
  public HttpStatus Get(){
    return HttpStatus.OK;
  }

  @RequestMapping(value="/skiers/{resortID:\\d+}/seasons/{seasonID:\\d+}/days/{dayID:\\d+}/skiers"
      + "/{skierID:\\d+}", method= RequestMethod.POST,
      consumes = {"application/json"})
  @ResponseStatus(code = HttpStatus.CREATED)
  public HttpStatus Post() {
    return HttpStatus.CREATED;
  }

}
