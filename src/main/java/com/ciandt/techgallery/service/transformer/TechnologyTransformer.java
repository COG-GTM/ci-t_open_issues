package com.ciandt.techgallery.service.transformer;

import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.model.TechnologyTO;
import com.google.api.server.spi.config.Transformer;
import com.googlecode.objectify.Ref;

import java.util.ArrayList;
import java.util.List;

public class TechnologyTransformer implements Transformer<Technology, TechnologyTO> {

  @Override
  public Technology transformFrom(TechnologyTO baseObject) {
    Technology product = new Technology();
    product.setAuthor(baseObject.getAuthor());
    product.setDescription(baseObject.getDescription());
    product.setId(baseObject.getId());
    product.setImage(baseObject.getImage());
    product.setName(baseObject.getName());
    product.setRecommendation(baseObject.getRecommendation());
    product.setRecommendationJustification(baseObject.getRecommendationJustification());
    product.setShortDescription(baseObject.getShortDescription());
    product.setWebsite(baseObject.getWebsite());
    product.setPositiveRecommendationsCounter(baseObject.getPositiveRecommendationsCounter());
    product.setNegativeRecommendationsCounter(baseObject.getNegativeRecommendationsCounter());
    product.setCommentariesCounter(baseObject.getCommentariesCounter());
    product.setEndorsersCounter(baseObject.getEndorsersCounter());
    product.setFollowedByUser(baseObject.isFollowedByUser());
    product.setLastActivity(baseObject.getLastActivity());
    product.setImageContent(baseObject.getImageContent());
    
    if (baseObject.getParentTechnologyId() != null && !baseObject.getParentTechnologyId().isEmpty()) {
      Technology parent = new Technology();
      parent.setId(baseObject.getParentTechnologyId());
      product.setParentTechnology(Ref.create(parent));
    }
    
    return product;
  }

  @Override
  public TechnologyTO transformTo(Technology baseObject) {
    if (baseObject.getInactivatedDate() == null) {
      TechnologyTO product = new TechnologyTO();
      product.setAuthor(baseObject.getAuthor());
      product.setDescription(baseObject.getDescription());
      product.setId(baseObject.getId());
      product.setImage(baseObject.getImage());
      product.setName(baseObject.getName());
      product.setRecommendation(baseObject.getRecommendation());
      product.setRecommendationJustification(baseObject.getRecommendationJustification());
      product.setShortDescription(baseObject.getShortDescription());
      product.setWebsite(baseObject.getWebsite());
      product.setPositiveRecommendationsCounter(baseObject.getPositiveRecommendationsCounter());
      product.setNegativeRecommendationsCounter(baseObject.getNegativeRecommendationsCounter());
      product.setCommentariesCounter(baseObject.getCommentariesCounter());
      product.setEndorsersCounter(baseObject.getEndorsersCounter());
      product.setFollowedByUser(baseObject.isFollowedByUser());
      product.setLastActivity(baseObject.getLastActivity());
      product.setImageContent(baseObject.getImageContent());
      
      if (baseObject.getParentTechnology() != null) {
        product.setParentTechnologyId(baseObject.getParentTechnology().get().getId());
      }
      
      if (baseObject.getChildTechnologies() != null && !baseObject.getChildTechnologies().isEmpty()) {
        List<TechnologyTO> childTOs = new ArrayList<>();
        for (Technology child : baseObject.getChildTechnologies()) {
          TechnologyTO childTO = transformTo(child);
          if (childTO != null) {
            childTOs.add(childTO);
          }
        }
        product.setChildTechnologies(childTOs);
      }
      
      return product;
    } else {
      return null;
    }
  }

}
