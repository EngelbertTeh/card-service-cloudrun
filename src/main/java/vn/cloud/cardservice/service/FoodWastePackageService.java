package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.FoodWastePackage;
import vn.cloud.cardservice.repository.FoodWastePackageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FoodWastePackageService {

    @Autowired
    FoodWastePackageRepository foodWastePackageRepository;

    //Create
    public InternalMessenger<FoodWastePackage> saveFoodWastePackage(FoodWastePackage foodWastePackageOther) {
        try {
            FoodWastePackage foodWastePackageR = foodWastePackageRepository.save(foodWastePackageOther);
            return new InternalMessenger<>(foodWastePackageR, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Retrieve
    public InternalMessenger<FoodWastePackage> getFoodWastePackageById(Long id) {
        try {
            Optional<FoodWastePackage> foodWastePackageOpt = foodWastePackageRepository.findById(id);
            if (foodWastePackageOpt.isPresent()) {
                return new InternalMessenger<>(foodWastePackageOpt.get(), true);
            } else return new InternalMessenger<>(null, false, "element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }


    public InternalMessenger<List<FoodWastePackage>> getAllFoodWastePackages() {
        try {
            List<FoodWastePackage> foodWastePackages = foodWastePackageRepository.findAll();
            if(!foodWastePackages.isEmpty()) {
                List<FoodWastePackage> activeFoodWastePackages = new ArrayList<>();
                for(FoodWastePackage foodWastePackage : foodWastePackages) {
                    if(foodWastePackage.getIsDeactivated() == false) {
                        activeFoodWastePackages.add(foodWastePackage);
                    }
                }
                return new InternalMessenger<>(activeFoodWastePackages,true); // only return non-cancelled food waste packages
            }
            else return new InternalMessenger<>(null,false,"list empty");
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    public InternalMessenger<List<FoodWastePackage>> getAllNotCollectedFoodWastePackages() {
        try {
            List<FoodWastePackage> foodWastePackages = foodWastePackageRepository.findAll();
            if(!foodWastePackages.isEmpty()) {
                List<FoodWastePackage> notCollectedFoodWastePackages = new ArrayList<>();
                for(FoodWastePackage foodWastePackage : foodWastePackages) {
                    if(foodWastePackage.getIsCollected() == false && foodWastePackage.getIsDeactivated() == false) {
                        notCollectedFoodWastePackages.add(foodWastePackage);
                    }
                }
                return new InternalMessenger<>(notCollectedFoodWastePackages,true); // only return non-cancelled and not collected food waste packages
            }
            else return new InternalMessenger<>(null,false,"list empty");
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }


    public InternalMessenger<List<FoodWastePackage>> getAllFoodWastePackagesHistory() {
        try {
            List<FoodWastePackage> foodWastePackages = foodWastePackageRepository.findAll();
            if(!foodWastePackages.isEmpty()) {
                List<FoodWastePackage> foodWastePackagesHistory = new ArrayList<>();
                for(FoodWastePackage foodWastePackage : foodWastePackages) {
                    if(foodWastePackage.getIsCollected() == true || foodWastePackage.getIsDeactivated() == true) {
                        foodWastePackagesHistory.add(foodWastePackage);
                    }
                }
                return new InternalMessenger<>(foodWastePackagesHistory,true); // only return non-cancelled and not collected food waste packages
            }
            else return new InternalMessenger<>(null,false,"list empty");
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

//    public InternalMessenger<List<FoodWastePackage>> getAllPendingCollection() {
//        try {
//            List<FoodWastePackage> foodWastePackages = foodWastePackageRepository.findAll();
//            if(!foodWastePackages.isEmpty()) {
//                List<FoodWastePackage> foodWastePackagesPendingCollection = new ArrayList<>();
//                for(FoodWastePackage foodWastePackage : foodWastePackages) {
//                    if(foodWastePackage.getIsCollected() == false) {
//                        foodWastePackagesPendingCollection.add(foodWastePackage);
//                    }
//                }
//                return new InternalMessenger<>(foodWastePackagesPendingCollection, true);
//            }
//            else return new InternalMessenger<>(null, false, "pending list empty");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new InternalMessenger<>(null, false, e.toString());
//        }
//    }

    //Update
    public InternalMessenger<FoodWastePackage> updateFoodWastePackage(FoodWastePackage foodWastePackageOther) {
        try {
            Optional<FoodWastePackage> foodWastePackageOpt = foodWastePackageRepository.findById(foodWastePackageOther.getId());
            if (foodWastePackageOpt.isPresent()) { // if such user exists
                FoodWastePackage foodWastePackageR = foodWastePackageRepository.saveAndFlush(foodWastePackageOther); // save changes
                return new InternalMessenger<>(foodWastePackageR, true);
            } else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }
//
//    public Boolean togglePickupStatus(Long id) {
//        try {
//            Optional<FoodWastePackage> foodWastePackageOpt = foodWastePackageRepository.findById(id); // find foodWastePackage
//            if (foodWastePackageOpt.isPresent()) { // check if foodWastePackage exists
//                FoodWastePackage foodWastePackage = foodWastePackageOpt.get();
//                if(foodWastePackage.getIsPendingPickup()) {
//                    foodWastePackage.setIsPendingPickup(false);  // if its true set to false
//                }
//                else foodWastePackage.setIsPendingPickup(true); // if its false set to true
//                foodWastePackageRepository.saveAndFlush(foodWastePackage);
//                return true;
//            } else return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public Boolean toggleListedStatus(Long id) {
//        try {
//            Optional<FoodWastePackage> foodWastePackageOpt = foodWastePackageRepository.findById(id); // find foodWastePackage
//            if (foodWastePackageOpt.isPresent()) { // check if foodWastePackage exists
//                FoodWastePackage foodWastePackage = foodWastePackageOpt.get();
//                if (foodWastePackage.getIsListed()) {
//                    foodWastePackage.setIsListed(false);  // if its true set to false
//                } else foodWastePackage.setIsListed(true); // if its false set to true
//                foodWastePackageRepository.saveAndFlush(foodWastePackage);
//                return true;
//            } else return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
public Boolean updateCollectedStatus (Long id){
    try {
        Optional<FoodWastePackage> foodWastePackageOpt = foodWastePackageRepository.findById(id); // find foodWastePackage
        if (foodWastePackageOpt.isPresent()) { // check if foodWastePackage exists
            FoodWastePackage foodWastePackage = foodWastePackageOpt.get();
            if (foodWastePackage.getIsCollected() == false) { // if not collected -> false
                foodWastePackage.setIsCollected(true);  // update to collected -> true
                foodWastePackageRepository.saveAndFlush(foodWastePackage);
                return true;
            }
        }  return false;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    //Delete
    public Boolean deleteFoodWastePackageById(Long id) { // soft delete
        try {
            Optional<FoodWastePackage> foodWastePackageOpt = foodWastePackageRepository.findById(id);
            if(foodWastePackageOpt.isPresent()) { // make sure such food waste package exists
                FoodWastePackage foodWastePackageR = foodWastePackageOpt.get();
                if(foodWastePackageR.getIsDeactivated()==false) { // make sure fwp was not previously "soft deleted", if it was previously "deleted", the `isDeactivated` returns true
                    foodWastePackageR.setIsDeactivated(true); // "soft delete" the fwp by changing `isDeactivated` to true
                    foodWastePackageRepository.saveAndFlush(foodWastePackageR); // update the entity
                    return true;
                }
            }
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
