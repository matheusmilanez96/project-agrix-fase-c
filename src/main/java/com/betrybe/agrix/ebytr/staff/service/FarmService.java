package com.betrybe.agrix.ebytr.staff.service;

import com.betrybe.agrix.ebytr.staff.entity.Crop;
import com.betrybe.agrix.ebytr.staff.entity.Farm;
import com.betrybe.agrix.ebytr.staff.entity.Fertilizer;
import com.betrybe.agrix.ebytr.staff.repository.CropRepository;
import com.betrybe.agrix.ebytr.staff.repository.FarmRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FarmService.
 */
@Service
public class FarmService {

  private final FarmRepository farmRepository;

  private final CropRepository cropRepository;

  @Autowired
  public FarmService(FarmRepository farmRepository, CropRepository cropRepository) {
    this.farmRepository = farmRepository;
    this.cropRepository = cropRepository;
  }

  public Farm insertFarm(Farm farm) {
    return farmRepository.save(farm);
  }

  public Optional<Farm> getFarmById(Long id) {
    return farmRepository.findById(id);
  }

  public Optional<Crop> getCropById(Long id) {
    return cropRepository.findById(id);
  }

  public List<Crop> getAllCrops() {
    return cropRepository.findAll();
  }

  public List<Farm> getAllFarms() {
    return farmRepository.findAll();
  }

  /**
   * Método insertCrop.
   */
  public Crop insertCrop(Crop crop) {
    return cropRepository.save(crop);
  }

  /**
   * Método getCropsById.
   */
  public List<Crop> getCropsById(Farm farm) {
    List<Crop> allCrops = farm.getCrops();
    String name = farm.getName();
    Long farmId = farm.getId();
    //List<Crop> crops = cropRepository.findByFarmId(farmId);
    return allCrops;
  }

  public List<Crop> searchCrops(LocalDate start, LocalDate end) {
    return cropRepository.findByHarvestDateBetween(start, end);
  }

  public void associateCropAndFertilizer(Crop crop, Fertilizer fertilizer) {
    crop.setFertilizers(fertilizer);
    cropRepository.save(crop);
  }
}
