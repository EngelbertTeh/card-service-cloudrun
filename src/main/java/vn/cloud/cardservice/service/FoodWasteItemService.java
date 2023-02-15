package vn.cloud.cardservice.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.cloud.cardservice.dto.ImageDTO;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.FoodWasteItem;
import vn.cloud.cardservice.repository.FoodWasteItemRepository;

import java.util.*;

import static vn.cloud.cardservice.utils.Constants.baseURL_static;
import static vn.cloud.cardservice.utils.Constants.bucket_name_static;

@Service
public class FoodWasteItemService {

    @Autowired
    FoodWasteItemRepository foodWasteItemRepository;

    @Autowired
    Storage storage;

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
            else return new InternalMessenger<>(new ArrayList<>(), false, "list empty");
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
            return new InternalMessenger<>(new ArrayList<>(),false,"list empty");
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

    @Transactional // hyperlink url for img needs to be saved in entity and image needs be uploaded in cloud, both conditions needs to be met, else, rollback
    public InternalMessenger<FoodWasteItem> uploadImage(@RequestBody ImageDTO imageDTO) { // adds a link to download the image from gcp bucket in the foodWasteItem entity, so its considered an update
        try {
            Optional<FoodWasteItem> foodWasteItemOpt = foodWasteItemRepository.findById(imageDTO.getId()); // find foodWasteItem
            if (foodWasteItemOpt.isPresent()) { // check if foodWasteItem exists
                FoodWasteItem foodWasteItem = foodWasteItemOpt.get();

                // Prepare the image name and blob info
                String name = String.format("foodWasteItem_%s", foodWasteItem.getId()) + ".jpg";
                BlobId blobId = BlobId.of(bucket_name_static, String.format("images-foodWasteItem/%s", name));
                BlobInfo info = BlobInfo.newBuilder(blobId).setContentType("image/jpg").build();

                // Store the image url (a link that when invoked will download the image from gcp bucket)
                foodWasteItem.setImageUrl(baseURL_static + blobId.getName());
                foodWasteItemRepository.saveAndFlush(foodWasteItem);

                // Decode the base64 string received from client into bytes, then store it in google cloud bucket
                byte[] arr = Base64.getDecoder().decode(imageDTO.getBase64());
                storage.create(info, arr);
                return new InternalMessenger<>(foodWasteItem, true);
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
