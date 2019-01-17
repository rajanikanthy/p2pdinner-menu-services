/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p2pdinner.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author rajani
 */
@JsonSerialize
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer id;

    private Integer profileId;

    private String title;

    private String description;

    private Boolean isActive;

    private String addressLine1;

    private String addressLine2;

    private String zipCode;

    private String state;

    private String city;

    private Integer availableQuantity;

    private Float costPerItem;

    private String imageUri;

    private Long startDate;

    private Long endDate;

    private Long closeDate;

    private Collection<DinnerCategory> categories;

    private Collection<DinnerSpecialNeeds> specialNeeds;

    private Collection<DinnerDelivery> deliveries;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MenuItem other = (MenuItem) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    public String getAddressLine1() {
        return addressLine1;
    }
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }
    public String getAddressLine2() {
        return addressLine2;
    }
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
    public Float getCostPerItem() {
        return costPerItem;
    }
    public void setCostPerItem(Float costPerItem) {
        this.costPerItem = costPerItem;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageFileName) {
        this.imageUri = imageFileName;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Collection<DinnerCategory> getCategories() {
        return categories;
    }

    public void setCategories(Collection<DinnerCategory> categories) {
        this.categories = categories;
    }

    public Collection<DinnerSpecialNeeds> getSpecialNeeds() {
        return specialNeeds;
    }

    public void setSpecialNeeds(Collection<DinnerSpecialNeeds> specialNeeds) {
        this.specialNeeds = specialNeeds;
    }

    public Collection<DinnerDelivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(Collection<DinnerDelivery> deliveries) {
        this.deliveries = deliveries;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Long closeDate) {
        this.closeDate = closeDate;
    }
}
