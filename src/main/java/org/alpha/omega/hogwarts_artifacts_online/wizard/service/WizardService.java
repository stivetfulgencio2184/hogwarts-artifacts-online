package org.alpha.omega.hogwarts_artifacts_online.wizard.service;

import lombok.RequiredArgsConstructor;
import org.alpha.omega.hogwarts_artifacts_online.common.Constant;
import org.alpha.omega.hogwarts_artifacts_online.common.exception.NotFoundException;
import org.alpha.omega.hogwarts_artifacts_online.entity.Wizard;
import org.alpha.omega.hogwarts_artifacts_online.wizard.repository.WizardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WizardService {

    private final WizardRepository repository;

    public Wizard findById(Long wizardId) {
        return this.repository.findById(wizardId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constant.CustomExMessage.Wizard.NOT_FOUND_WIZARD, wizardId)));
    }
}
