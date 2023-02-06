package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.FoodWasteBundle;
import vn.cloud.cardservice.repository.FoodWasteBundleRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FoodWasteBundleService {

        @Autowired
        FoodWasteBundleRepository foodWasteBundleRepository;

        //Create
        public InternalMessenger<FoodWasteBundle> saveFoodWasteBundle(FoodWasteBundle foodWasteBundleOther) {
            try {
                FoodWasteBundle foodWasteBundleR = foodWasteBundleRepository.save(foodWasteBundleOther);
                return new InternalMessenger<>(foodWasteBundleR, true);
            } catch (Exception e) {
                e.printStackTrace();
                return new InternalMessenger<>(null, false, e.toString());
            }
        }

        //Retrieve
        public InternalMessenger<FoodWasteBundle> getUserById(Long id) {
            try {
                Optional<FoodWasteBundle> foodWasteBundleOpt = foodWasteBundleRepository.findById(id);
                if (foodWasteBundleOpt.isPresent()) {
                    return new InternalMessenger<>(foodWasteBundleOpt.get(), true);
                } else return new InternalMessenger<>(null, false, "element not found");
            } catch (Exception e) {
                e.printStackTrace();
                return new InternalMessenger<>(null, false, e.toString());
            }
        }


        public InternalMessenger<List<FoodWasteBundle>> getAllFoodWasteBundles() {
            try {
                List<FoodWasteBundle> foodWasteBundles = foodWasteBundleRepository.findAll();
                return new InternalMessenger<>(foodWasteBundles, true);
            } catch (Exception e) {
                e.printStackTrace();
                return new InternalMessenger<>(null, false, e.toString());
            }
        }

        //Update
        public InternalMessenger<FoodWasteBundle> updateFoodWasteBundle(FoodWasteBundle foodWasteBundleOther) {
            try {
                Optional<FoodWasteBundle> foodWasteBundleOpt = foodWasteBundleRepository.findById(foodWasteBundleOther.getId());
                if (foodWasteBundleOpt.isPresent()) { // if such user exists
                    FoodWasteBundle foodWasteBundleR = foodWasteBundleRepository.saveAndFlush(foodWasteBundleOther); // save changes
                    return new InternalMessenger<>(foodWasteBundleR, true);
                } else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
            } catch (Exception e) {
                e.printStackTrace();
                return new InternalMessenger<>(null, false, e.toString());
            }
        }

        //Delete
        public Boolean deleteFoodWasteBundleById(Long id) { // hard delete, food waste data is considered not so valuable
            try {
                Optional<FoodWasteBundle> foodWasteBundleOpt = foodWasteBundleRepository.findById(id);
                if (foodWasteBundleOpt.isPresent()) { // make sure bundle exists
                    FoodWasteBundle foodWasteBundleR = foodWasteBundleOpt.get();
                    foodWasteBundleRepository.delete(foodWasteBundleR);
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
}
