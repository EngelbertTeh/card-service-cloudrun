package vn.cloud.cardservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.FoodWasteItem;
import vn.cloud.cardservice.repository.FoodWasteItemRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FoodWasteItemService {

    @Autowired
    FoodWasteItemRepository foodWasteItemRepository;

    //Create
    public InternalMessenger<FoodWasteItem> saveFoodWasteItem(FoodWasteItem foodWasteItemOther) {
        try {
            FoodWasteItem foodWasteItemR = foodWasteItemRepository.saveAndFlush(foodWasteItemOther);
            return new InternalMessenger<>(foodWasteItemR, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Retrieve
    public InternalMessenger<FoodWasteItem> getFoodWasteItemById(Long id) {
        try {
            Optional<FoodWasteItem> foodWasteItemOpt = foodWasteItemRepository.findById(id);
            if (foodWasteItemOpt.isPresent()) {
                return new InternalMessenger<>(foodWasteItemOpt.get(), true);
            } else return new InternalMessenger<>(null, false, "element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }
    
    public InternalMessenger<FoodWasteItem> getFoodWasteItemByName(String name) {
        try {
            Optional<FoodWasteItem> foodWasteItemOpt = foodWasteItemRepository.findByName(name.toLowerCase().trim());
            if (foodWasteItemOpt.isPresent()) {
                return new InternalMessenger<>(foodWasteItemOpt.get(), true);
            } else return new InternalMessenger<>(null, false, "element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    public InternalMessenger<List<FoodWasteItem>> getAllFoodWasteItems() {
        try {
            List<FoodWasteItem> foodWasteItems = foodWasteItemRepository.findAll();
            if(!foodWasteItems.isEmpty()) {
                return new InternalMessenger<>(foodWasteItems, true);
            }
            else return new InternalMessenger<>(null, false, "list empty");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }


    public InternalMessenger<List<FoodWasteItem>> getFoodWasteItemsByBizId(Long biz_Id) {
        try {
            List<FoodWasteItem> foodWasteItems = foodWasteItemRepository.findAllFoodWasteItemsByBusinessUserId(biz_Id);
            if(!foodWasteItems.isEmpty()) {
                return new InternalMessenger<>(foodWasteItems,true); // only return cancelled or collected food waste packages
            }
            return new InternalMessenger<>(null,false,"list empty");
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }
    
    //Update
    public InternalMessenger<FoodWasteItem> updateFoodWasteItem(FoodWasteItem foodWasteItemOther) {
        try {
            Optional<FoodWasteItem> foodWasteItemOpt = foodWasteItemRepository.findById(foodWasteItemOther.getId());
            if (foodWasteItemOpt.isPresent()) { // if such user exists
                FoodWasteItem foodWasteItemR = foodWasteItemRepository.saveAndFlush(foodWasteItemOther); // save changes
                return new InternalMessenger<>(foodWasteItemR, true);
            } else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Delete
    public Boolean deleteFoodWasteItemById(Long id) { // hard delete
        try {
            Optional<FoodWasteItem> foodWasteItemOpt = foodWasteItemRepository.findById(id);
            if (foodWasteItemOpt.isPresent()) { // make sure bundle exists
                FoodWasteItem foodWasteItemR = foodWasteItemOpt.get();
                foodWasteItemRepository.delete(foodWasteItemR);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Boolean deleteAllFoodWasteItemByBizId(Long biz_id) { // hard delete
        try {
            List<FoodWasteItem> foodWasteItemsList = foodWasteItemRepository.findAllFoodWasteItemsByBusinessUserId(biz_id);
            if (!foodWasteItemsList.isEmpty()) { // make sure list not empty
                for(FoodWasteItem foodWasteItem : foodWasteItemsList) {
                    foodWasteItemRepository.delete(foodWasteItem);  // one by one delete, all items must be deleted else roll back if something happen in between
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
