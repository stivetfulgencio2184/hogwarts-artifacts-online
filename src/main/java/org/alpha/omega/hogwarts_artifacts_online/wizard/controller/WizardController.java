package org.alpha.omega.hogwarts_artifacts_online.wizard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.response.Result;
import org.alpha.omega.hogwarts_artifacts_online.wizard.mapper.WizardMapper;
import org.alpha.omega.hogwarts_artifacts_online.wizard.request.WizardRequest;
import org.alpha.omega.hogwarts_artifacts_online.wizard.service.WizardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> createNewWizard(@RequestBody @Valid WizardRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Result.builder()
                        .flag(Boolean.TRUE)
                        .code(HttpStatus.CREATED.value())
                        .message("New wizard created successfully.")
                        .data(WizardMapper.INSTANCE.toWizardDTO(
                                this.service.createWizard(
                                        WizardMapper.INSTANCE.toWizard(request))))
                        .build());
    }

    @PutMapping(path = "/{wizardId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> updateWizardById(@RequestBody @Valid WizardRequest request, @PathVariable(name = "wizardId") Long id) {
        Wizard updateWizard = WizardMapper.INSTANCE.toWizard(request);
        updateWizard.setId(id);
        return ResponseEntity.ok(Result.builder()
                        .flag(Boolean.TRUE)
                        .code(HttpStatus.OK.value())
                        .message("Wizard updated successfully.")
                        .data(WizardMapper.INSTANCE.toWizardDTO(this.service.updateWizard(updateWizard)))
                .build());
    }
}
