package org.alpha.omega.hogwarts_artifacts_online.wizard.controller;

import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.response.Result;
import org.alpha.omega.hogwarts_artifacts_online.wizard.mapper.WizardMapper;
import org.alpha.omega.hogwarts_artifacts_online.wizard.service.WizardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/wizards")
@RequiredArgsConstructor
public class WizardController {

    private final WizardService service;

    @GetMapping(path = "/{wizardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> findWizardById(@PathVariable(name = "wizardId") Long id) {
        return ResponseEntity.ok(Result.builder()
                        .flag(Boolean.TRUE)
                        .code(HttpStatus.OK.value())
                        .message("Find Success")
                        .data(WizardMapper.INSTANCE.toWizardDTO(this.service.findById(id)))
                .build());
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> findAllWizards() {
        return ResponseEntity.ok(Result.builder()
                        .flag(Boolean.TRUE)
                        .code(HttpStatus.OK.value())
                        .message("Find all success")
                        .data(WizardMapper.INSTANCE.toWizardsDTOs(this.service.findAll()))
                .build());
    }
}
