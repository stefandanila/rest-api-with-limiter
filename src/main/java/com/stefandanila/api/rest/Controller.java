package com.stefandanila.api.rest;

import com.stefandanila.api.response.ResponseSuccess;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping(path = "/foo")
    public ResponseEntity<ResponseSuccess> getFoo() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ResponseSuccess("ok"));
    }

    @GetMapping(path = "/bar")
    public ResponseEntity<ResponseSuccess> getBar() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ResponseSuccess("ok"));
    }
}
