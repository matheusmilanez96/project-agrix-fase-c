package com.betrybe.agrix.ebytr.staff.controllers;

import com.betrybe.agrix.ebytr.staff.controllers.dto.CropCreationDto;
import com.betrybe.agrix.ebytr.staff.controllers.dto.FarmDto;
import com.betrybe.agrix.ebytr.staff.controllers.dto.FertilizerDto;
import com.betrybe.agrix.ebytr.staff.controllers.dto.PersonDto;
import com.betrybe.agrix.ebytr.staff.entity.Crop;
import com.betrybe.agrix.ebytr.staff.entity.Farm;
import com.betrybe.agrix.ebytr.staff.entity.Fertilizer;
import com.betrybe.agrix.ebytr.staff.entity.Person;
import com.betrybe.agrix.ebytr.staff.service.FarmService;
import com.betrybe.agrix.ebytr.staff.service.FertilizerService;
import com.betrybe.agrix.ebytr.staff.service.PersonService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * FarmController.
 */
@RestController
@RequestMapping(value = "")
public class FarmController {

  private final FarmService farmService;

  private final FertilizerService fertilizerService;

  private final PersonService personService;

  @Autowired
  public FarmController(FarmService farmService, FertilizerService fertilizerService,
      PersonService personService) {
    this.farmService = farmService;
    this.fertilizerService = fertilizerService;
    this.personService = personService;
  }

  @PostMapping("/farms")
  public ResponseEntity<Farm> createFarm(@RequestBody FarmDto farmDto) {
    Farm newFarm = farmService.insertFarm(farmDto.toFarm());
    return ResponseEntity.status(HttpStatus.CREATED).body(newFarm);
  }

  @PostMapping("/persons")
  public ResponseEntity<?> createPerson(@RequestBody PersonDto personDto) {
    Person newPerson = personService.create(personDto.toPerson());
    Map<String, Object> finalMap = new HashMap<>();
    finalMap.put("id", newPerson.getId());
    finalMap.put("username", newPerson.getUsername());
    finalMap.put("role", newPerson.getRole());
    return ResponseEntity.status(HttpStatus.CREATED).body(finalMap);
  }

  @PostMapping("/fertilizers")
  public ResponseEntity<Fertilizer> createFertilizer(@RequestBody FertilizerDto fertilizerDto) {
    Fertilizer newFertilizer = fertilizerService.insertFertilizer(fertilizerDto.toFertilizer());
    return ResponseEntity.status(HttpStatus.CREATED).body(newFertilizer);
  }

  /**
   * Método insertCrop.
   */
  @PostMapping("/farms/{farmId}/crops")
  public ResponseEntity<?> insertCrop(@RequestBody CropCreationDto cropDto,
      @PathVariable Long farmId) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);

    if (optionalFarm.isEmpty()) {
      String message = "Fazenda não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    Farm farm = optionalFarm.get();
    Crop crop = cropDto.toCrop();
    crop.setFarm(farm);
    Crop newCrop = farmService.insertCrop(crop);
    Map<String, Object> finalMap = new HashMap<>();
    finalMap.put("id", newCrop.getId());
    finalMap.put("name", newCrop.getName());
    finalMap.put("plantedArea", newCrop.getPlantedArea());
    finalMap.put("farmId", newCrop.getFarm().getId());
    finalMap.put("plantedDate", newCrop.getPlantedDate());
    finalMap.put("harvestDate", newCrop.getHarvestDate());


    return ResponseEntity.status(HttpStatus.CREATED).body(finalMap);
  }

  /**
   * Método associateCropAndFertilizer.
   */
  @PostMapping("/crops/{cropId}/fertilizers/{fertilizerId}")
  public ResponseEntity<?> associateCropAndFertilizer(@PathVariable Long cropId,
      @PathVariable Long fertilizerId) {
    Optional<Crop> optionalCrop = farmService.getCropById(cropId);

    if (optionalCrop.isEmpty()) {
      String message = "Plantação não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    Optional<Fertilizer> optionalFertilizer = fertilizerService.getFertilizerById(fertilizerId);

    if (optionalFertilizer.isEmpty()) {
      String message = "Fertilizante não encontrado!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    Crop crop = optionalCrop.get();
    Fertilizer fertilizer = optionalFertilizer.get();
    farmService.associateCropAndFertilizer(crop, fertilizer);
    String message = "Fertilizante e plantação associados com sucesso!";
    return ResponseEntity.status(HttpStatus.CREATED).body(message);
  }

  /**
   * Método getFertilizerById.
   */
  @GetMapping("/fertilizers/{id}")
  public ResponseEntity<?> getFertilizerById(@PathVariable Long id) {
    Optional<Fertilizer> optionalFertilizer = fertilizerService.getFertilizerById(id);

    if (optionalFertilizer.isEmpty()) {
      String message = "Fertilizante não encontrado!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    return ResponseEntity.ok(optionalFertilizer.get());
  }

  /**
   * Método getFarmById.
   */
  @GetMapping("/farms/{farmId}")
  public ResponseEntity<?> getFarmById(@PathVariable Long farmId) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);

    if (optionalFarm.isEmpty()) {
      String message = "Fazenda não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    return ResponseEntity.ok(optionalFarm.get());
  }

  /**
   * Método getCropById.
   */
  @GetMapping("/crops/{id}")
  public ResponseEntity<?> getCropById(@PathVariable Long id) {
    Optional<Crop> optionalCrop = farmService.getCropById(id);

    if (optionalCrop.isEmpty()) {
      String message = "Plantação não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    Crop crop = optionalCrop.get();

    CropCreationDto responseCrop = new CropCreationDto(crop.getId(), crop.getName(),
        crop.getPlantedArea(), crop.getFarm().getId(), crop.getPlantedDate(),
        crop.getHarvestDate());

    return ResponseEntity.ok(responseCrop);
  }

  /**
   * Método getCropById.
   */
  @GetMapping("/farms/{farmId}/crops")
  public ResponseEntity<?> getCropsById(@PathVariable Long farmId) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);

    if (optionalFarm.isEmpty()) {
      String message = "Fazenda não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    List<Crop> crops = farmService.getCropsById(optionalFarm.get());
    crops.forEach(crop -> crop.setFarm(optionalFarm.get()));

    List<CropCreationDto> allCrops = crops.stream()
        .map((crop) -> new CropCreationDto(crop.getId(), crop.getName(), crop.getPlantedArea(),
            crop.getFarm().getId(), crop.getPlantedDate(), crop.getHarvestDate()))
        .toList();

    return ResponseEntity.ok(allCrops);
  }

  /**
   * Método searchCrops.
   */
  @GetMapping("/crops/search")
  public List<CropCreationDto> searchCrops(@RequestParam("start") LocalDate start,
      @RequestParam("end") LocalDate end) {
    List<Crop> allCrops = farmService.searchCrops(start, end);
    return allCrops.stream()
        .map((crop) -> new CropCreationDto(crop.getId(), crop.getName(), crop.getPlantedArea(),
            crop.getFarm().getId(), crop.getPlantedDate(), crop.getHarvestDate()))
        .collect(Collectors.toList());
  }

  /**
   * Método getAllCrops.
   */
  @GetMapping("/crops")
  public List<?> getAllCrops() {
    List<Crop> allCrops = farmService.getAllCrops();
    return allCrops.stream()
        .map((crop) -> new CropCreationDto(crop.getId(), crop.getName(), crop.getPlantedArea(),
            crop.getFarm().getId(), crop.getPlantedDate(), crop.getHarvestDate()))
        .collect(Collectors.toList());
  }

  /**
   * Método getAllFarms.
   */
  @GetMapping("/farms")
  public List<FarmDto> getAllFarms() {
    List<Farm> allFarms = farmService.getAllFarms();
    return allFarms.stream()
        .map((farm) -> new FarmDto(farm.getId(), farm.getName(), farm.getSize()))
        .collect(Collectors.toList());
  }

  /**
   * Método getAllFertilizers.
   */
  @GetMapping("/fertilizers")
  public List<FertilizerDto> getAllFertilizers() {
    List<Fertilizer> allFertilizers = fertilizerService.getAllFertilizers();
    return allFertilizers.stream()
        .map((fertilizer) -> new FertilizerDto(fertilizer.getId(), fertilizer.getName(),
            fertilizer.getBrand(), fertilizer.getComposition()))
        .collect(Collectors.toList());
  }
}
