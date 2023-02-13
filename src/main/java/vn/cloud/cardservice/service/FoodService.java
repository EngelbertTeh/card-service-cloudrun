package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.CriteriaDTO;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.Food;
import vn.cloud.cardservice.repository.FoodRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    FoodRepository foodRepository;

    //Create
    public InternalMessenger<Food> saveFood(Food foodOther) {
        try {
            Food foodR = foodRepository.saveAndFlush(foodOther);
            return new InternalMessenger<>(foodR, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Retrieve
    public InternalMessenger<Food> getFoodById(Long id) {
        try {
            Optional<Food> foodOpt = foodRepository.findById(id);
            if (foodOpt.isPresent()) {
                return new InternalMessenger<>(foodOpt.get(), true);
            } else return new InternalMessenger<>(null, false, "element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }


    public InternalMessenger<List<Food>> getAllFoods() {
        try {
            List<Food> foods = foodRepository.findAll();
            if(!foods.isEmpty()) {
                return new InternalMessenger<>(foods, true);
            }
            else return new InternalMessenger<>(null, false, "list empty");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    public InternalMessenger<List<Food>> getFoodsByCriteria(CriteriaDTO criteriaDTO) {
        try {
            List<Food> foods = foodRepository.findAllByCriteria(criteriaDTO.getTitle().toLowerCase().trim(),criteriaDTO.getHalal().toLowerCase().trim());
            if(!foods.isEmpty()) {
                return new InternalMessenger<>(foods, true);
            }
            else return new InternalMessenger<>(null, false, "list empty");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    public InternalMessenger<List<Food>> getFoodsByHalalStatus(CriteriaDTO criteriaDTO) {
        try {
            List<Food> foods = foodRepository.findAllByHalalStatus(criteriaDTO.getHalal().toLowerCase().trim());
            if(!foods.isEmpty()) {
                return new InternalMessenger<>(foods, true);
            }
            else return new InternalMessenger<>(null, false, "list empty");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }




    //Update
    public InternalMessenger<Food> updateFood(Food foodOther) {
        try {
            Optional<Food> foodOpt = foodRepository.findById(foodOther.getId());
            if (foodOpt.isPresent()) { // if such user exists
                Food foodR = foodRepository.saveAndFlush(foodOther); // save changes
                return new InternalMessenger<>(foodR, true);
            } else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    public Boolean togglePickupStatus(Long id) {
        try {
            Optional<Food> foodOpt = foodRepository.findById(id); // find food
            if (foodOpt.isPresent()) { // check if food exists
                Food food = foodOpt.get();
                if(food.getIsPendingPickup()) {
                    food.setIsPendingPickup(false);  // if its true set to false
                }
                else food.setIsPendingPickup(true); // if its false set to true
                foodRepository.saveAndFlush(food);
                return true;
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean toggleListedStatus(Long id) {
        try {
            Optional<Food> foodOpt = foodRepository.findById(id); // find food
            if (foodOpt.isPresent()) { // check if food exists
                Food food = foodOpt.get();
                if (food.getIsListed()) {
                    food.setIsListed(false);  // if its true set to false
                } else food.setIsListed(true); // if its false set to true
                foodRepository.saveAndFlush(food);
                return true;
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

        public Boolean updateCollectedStatus (Long id){
            try {
                Optional<Food> foodOpt = foodRepository.findById(id); // find food
                if (foodOpt.isPresent()) { // check if food exists
                    Food food = foodOpt.get();
                    if (food.getIsCollected() == false) { // if not collected -> false
                        food.setIsCollected(true);  // update to collected -> true
                        foodRepository.saveAndFlush(food);
                        return true;
                    }
                }  return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }



    //Delete
    public Boolean deleteFoodById(Long id) { // hard delete, food waste data is considered not so valuable
        try {
            Optional<Food> foodOpt = foodRepository.findById(id);
            if (foodOpt.isPresent()) { // make sure bundle exists
                Food foodR = foodOpt.get();
                foodRepository.delete(foodR);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
}
