package vn.cloud.cardservice.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.cloud.cardservice.dto.CriteriaDTO;
import vn.cloud.cardservice.dto.ImageDTO;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.Food;
import vn.cloud.cardservice.repository.FoodRepository;

import java.util.*;

import static vn.cloud.cardservice.utils.Constants.baseURL_static;
import static vn.cloud.cardservice.utils.Constants.bucket_name_static;

@Service
public class FoodService {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    private Storage storage;

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
            else return new InternalMessenger<>(new ArrayList<>(), false, "list empty");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    public InternalMessenger<List<Food>> getFoodsByCriteria(CriteriaDTO criteriaDTO) {
        try {
            List<Food> foods;
            String halalStatus = criteriaDTO.getHalalStatus().toLowerCase().trim(); // certain that halalStatus won't be null because client side uses radio button for halal status
            String title = criteriaDTO.getTitle();
            if(halalStatus.equals("all")) {
                if(title != null && title.trim().length() > 0) {
                    foods = foodRepository.findAllByTitle(title.toLowerCase().trim());
                }
               else foods = foodRepository.findAll();
            }
            else {
                if(title != null && title.trim().length() > 0) {
                    foods = foodRepository.findFoodsByCriteria(title.toLowerCase().trim(),halalStatus);
                }
                else foods = foodRepository.findAllByHalalStatus(halalStatus);
            }
            if(!foods.isEmpty()) {
                return new InternalMessenger<>(foods, true);
            }
            return new InternalMessenger<>(new ArrayList<>(), false, "list empty");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }
    
    public InternalMessenger<List<Food>> getFoodsByIndIdAndCollectedStatus(Long ind_Id, Boolean isCollected) {
        try {
            List<Food> foods = foodRepository.findFoodsByIndividualUserIdAndCollectedStatus(ind_Id,isCollected);
            if(!foods.isEmpty()) {
                return new InternalMessenger<>(foods,true);
            }
            return new InternalMessenger<>(new ArrayList<>(),false,"list empty");
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
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

    @Transactional // hyperlink url for img needs to be saved in entity and image needs be uploaded in cloud, both conditions needs to be met, else, rollback
    public InternalMessenger<Food> uploadImage(@RequestBody ImageDTO imageDTO) { // adds a link to download the image from gcp bucket in the food entity, so its considered an update
        try {
            Optional<Food> foodOpt = foodRepository.findById(imageDTO.getId()); // find food
            if (foodOpt.isPresent()) { // check if food exists
                Food food = foodOpt.get();

                // Prepare the image name and blob info
                String name = String.format("food_%s", food.getId()) + ".jpg";
                BlobId blobId = BlobId.of(bucket_name_static, String.format("images-food/%s", name));
                BlobInfo info = BlobInfo.newBuilder(blobId).setContentType("image/jpg").build();

                // Store the image url (a link that when invoked will download the image from gcp bucket)
                food.setImageUrl(baseURL_static + blobId.getName());
                foodRepository.saveAndFlush(food);

                // Decode the base64 string received from client into bytes, then store it in google cloud bucket
                byte[] arr = Base64.getDecoder().decode(imageDTO.getBase64());
                storage.create(info, arr);
                return new InternalMessenger<>(food,true);
            }
            return new InternalMessenger<>(null,false,"not found");
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,"invalid format");
    } catch(Exception e){
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
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
