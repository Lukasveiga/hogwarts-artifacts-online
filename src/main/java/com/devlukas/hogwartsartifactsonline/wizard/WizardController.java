package com.devlukas.hogwartsartifactsonline.wizard;

import com.devlukas.hogwartsartifactsonline.system.Result;
import com.devlukas.hogwartsartifactsonline.system.StatusCode;
import com.devlukas.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import com.devlukas.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import com.devlukas.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    private final WizardService wizardService;

    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        var wizard = this.wizardService.findById(wizardId);
        var wizardDto = wizardToWizardDtoConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS,"Find one success", wizardDto);
    }

    @GetMapping
    public Result findAllWizards() {
        var wizardsList = this.wizardService.findAll();
        var wizardsListDto = wizardsList.stream().map(wizardToWizardDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find all success", wizardsListDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        var newWizard = wizardDtoToWizardConverter.convert(wizardDto);
        var savedWizard = this.wizardService.save(newWizard);
        var savedWizardDto = wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Add success", savedWizardDto);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto update) {
        var updatedWizard = this.wizardService.update(wizardId, wizardDtoToWizardConverter.convert(update));
        var updatedWizardDto = wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "Update success", updatedWizardDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId) {
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete success");
    }
}
