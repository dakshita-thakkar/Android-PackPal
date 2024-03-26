package com.example.packpal.Data;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.packpal.Constants.Constants;
import com.example.packpal.Database.RoomDB;
import com.example.packpal.Models.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppData extends Application {
    RoomDB database;
    String category;
    Context context;

    public static final String LAST_VERSION = "LAST_VERSION";
    public static final int NEW_VERSION = 3;

    public AppData(RoomDB database) {
        this.database = database;
    }

    public AppData(RoomDB database, Context context) {
        this.database = database;
        this.context = context;
    }

    public List<Items> getBasicData(){
        category = "Basic Needs";
        List<Items> basicItem = new ArrayList<>();
        basicItem.add(new Items("Passport", category,false));
        basicItem.add(new Items("Visa", category,false));
        basicItem.add(new Items("Tickets", category,false));
        basicItem.add(new Items("Wallet", category,false));
        basicItem.add(new Items("ID Proof", category,false));
        basicItem.add(new Items("Neck Pillow", category,false));
        basicItem.add(new Items("Books", category,false));
        basicItem.add(new Items("House Keys", category,false));
        basicItem.add(new Items("Umbrella", category,false));


        return basicItem;

    }

    public List<Items> getPersonalCareData(){
        String [] data = {"Tooth Brush", "Tooth Paste", "Soap", "Mouth wash", "Shampoo and Conditioner", "Face Wash", "Moisturizer", "Sunscreen", "Makeup Kit", "Makeup Cleanser", "Lip Balm", "Shaving Kit", "Feminine Hygiene Products", "Contacts/Glasses"};
        return prepareItemsList(Constants.PERSONAL_CARE_CAMEL_CASE, data);

    }

    public List<Items> getFoodData(){
        String [] data = {"Tea Bags", "Snacks", "Water", "Instant Coffee Powder"};
        return prepareItemsList(Constants.FOOD_CAMEL_CASE, data);

    }

    public List<Items> getCarSuppliesData(){
        String [] data = {"Car Jack", "Spare Car Keys", "Car Shades", "Car Cover", "Car Charger"};
        return prepareItemsList(Constants.CAR_SUPPLIES_CAMEL_CASE, data);

    }


    public List<Items> getNeedsData(){
        String [] data = {"BagPack", "Disposable bags", "Travel Lock", "Luggage Tags", "Stickers"};
        return prepareItemsList(Constants.NEEDS_CAMEL_CASE, data);

    }

    public List<Items> getBabyNeedsData(){
        String [] data = {"Diapers", "Wipes", "Rash Cream", "Clothes", "Milk Bottle", "Burp Clothes", "Pacifier", "Stroller", "Toys" };
        return prepareItemsList(Constants.BABY_NEEDS_CAMEL_CASE, data);

    }

    public List<Items> getBeachSuppliesData(){
        String [] data = {"Swimming Costume", "Sunscreen", "Lotion", "Beach Towels", "Beach Toys", "Sunglasses", "Hats"};
        return prepareItemsList(Constants.BEACH_SUPPLIES_CAMEL_CASE, data);

    }

    public List<Items> getTechnologyData(){
        String [] data = {"Phone", "Phone Charger", "Laptop", "Laptop Charger", "Headphones", "Earphones", "Kindle", "Power Bank"};
        return prepareItemsList(Constants.TECHNOLOGY_CAMEL_CASE, data);

    }

    public List<Items> getClothingData(){
        String [] data = {"T-shirts", "Formal Shirts", "Blazers", "Shoes", "Formal Shoes", "Pyjamas", "Undergarments"};
        return prepareItemsList(Constants.CLOTHING_CAMEL_CASE, data);

    }

    public List<Items> getHealthData(){
        String [] data = {"Medicines", "Asthma Inhaler", "Sugar Checking Machine", "Blood Pressure Machine", "First Aid Kit"};
        return prepareItemsList(Constants.HEALTH_CAMEL_CASE, data);

    }

    public List<Items> prepareItemsList(String category, String [] data){
        List<String> list = Arrays.asList(data);
        List<Items> dataList = new ArrayList<>();
        dataList.clear();
        for(int i=0; i<list.size(); i++){
            dataList.add(new Items(list.get(i), category, false));
        }

        return dataList;
    }

    public List<List<Items>> getAllData(){

        List<List<Items>> listOfAllItems = new ArrayList<>();
        listOfAllItems.clear();
        listOfAllItems.add(getBasicData());
        listOfAllItems.add(getClothingData());
        listOfAllItems.add(getPersonalCareData());
        listOfAllItems.add(getBabyNeedsData());
        listOfAllItems.add(getHealthData());
        listOfAllItems.add(getTechnologyData());
        listOfAllItems.add(getFoodData());
        listOfAllItems.add(getBeachSuppliesData());
        listOfAllItems.add(getCarSuppliesData());
        listOfAllItems.add(getNeedsData());

        return listOfAllItems;



    }


    public void persistAllData(){
        List<List<Items>> listOfAllItems = getAllData();
        for(List<Items> list:listOfAllItems){

            for(Items items:list){
                database.mainDao().saveItem(items);
            }

        }
        System.out.println("Data Added");

    }


    public void persistDataByCategory(String category, Boolean onlyDelete){
        try{

            List<Items> list = deleteAndGetListByCategory(category, onlyDelete);
            if(!onlyDelete){

                for(Items item:list){
                    database.mainDao().saveItem(item);
                }

            }
            else{
                Toast.makeText(this, category+ " Reset Successfully", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

//    deleteAndGetListByCategory():
//    --> if the user selects "Delete Default data" from menu, it will only delete all the data added by the system which we hardcoded.
//    --> If the user selects "Reset To Default", the data added y the user is deleted and the system data is restored.
    private List<Items> deleteAndGetListByCategory(String category, Boolean onlyDelete){
        if(onlyDelete){
            database.mainDao().deleteAllByCategoryAndAddedBy(category, Constants.SYSTEM_SMALL);
        }else{

            database.mainDao().deleteAllByCategory(category);

        }
        switch(category){
            case Constants.BASIC_NEEDS_CAMEL_CASE:
                return getBasicData();

            case Constants.CLOTHING_CAMEL_CASE:
                return getClothingData();

            case Constants.NEEDS_CAMEL_CASE:
                return getNeedsData();

            case Constants.BABY_NEEDS_CAMEL_CASE:
                return getBabyNeedsData();

            case Constants.HEALTH_CAMEL_CASE:
                return getHealthData();

            case Constants.FOOD_CAMEL_CASE:
                return getFoodData();

            case Constants.TECHNOLOGY_CAMEL_CASE:
                return getTechnologyData();

            case Constants.PERSONAL_CARE_CAMEL_CASE:
                return getPersonalCareData();

            case Constants.CAR_SUPPLIES_CAMEL_CASE:
                return getCarSuppliesData();

            case Constants.BEACH_SUPPLIES_CAMEL_CASE:
                return getBeachSuppliesData();

            default:
                return new ArrayList<>();

        }
    }



}
