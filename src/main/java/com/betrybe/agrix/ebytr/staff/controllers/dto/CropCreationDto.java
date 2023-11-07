package com.betrybe.agrix.ebytr.staff.controllers.dto;

import com.betrybe.agrix.ebytr.staff.entity.Crop;
import java.time.LocalDate;

/**
 * CropCreationDto.
 */
public record CropCreationDto(
    Long id,
    String name,
    Double plantedArea,
    Long farmId,

    LocalDate plantedDate,

    LocalDate harvestDate
) {
  /**
   * Método fromCrop.
   */
  public CropCreationDto fromCrop(Crop crop) {
    return new CropCreationDto(
        crop.getId(),
        crop.getName(),
        crop.getPlantedArea(),
        crop.getFarm().getId(),
        crop.getPlantedDate(),
        crop.getHarvestDate()
    );
  }

  /**
   * Método toCrop.
   */
  public Crop toCrop() {
    Crop crop = new Crop();
    crop.setName(name);
    crop.setPlantedArea(plantedArea);
    crop.setPlantedDate(plantedDate);
    crop.setHarvestDate(harvestDate);
    return crop;
  }
}
