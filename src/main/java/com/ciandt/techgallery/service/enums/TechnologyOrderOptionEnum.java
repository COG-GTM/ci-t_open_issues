package com.ciandt.techgallery.service.enums;

import com.ciandt.techgallery.persistence.model.Technology;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;

/**
 * Enum for mapping Order Options.
 * 
 * @author Felipe Ibrahim
 *
 */
public enum TechnologyOrderOptionEnum {

  POSITIVE_RECOMMENDATION_AMOUNT("Quantidade de Recomendações Positivas") {
    @Override
    public void sort(List<Technology> techList) {
      Collections.sort(techList, (counter1, counter2) -> Integer.compare(
          counter2.getPositiveRecommendationsCounter(),
          counter1.getPositiveRecommendationsCounter()));
    }
  },
  NEGATIVE_RECOMMENDATION_AMOUNT("Quantidade de Recomendações Negativas") {
    @Override
    public void sort(List<Technology> techList) {
      Collections.sort(techList, (counter1, counter2) -> Integer.compare(
          counter2.getNegativeRecommendationsCounter(),
          counter1.getNegativeRecommendationsCounter()));
    }
  },
  COMMENT_AMOUNT("Quantidade de Comentários") {
    @Override
    public void sort(List<Technology> techList) {
      Collections.sort(techList, (counter1, counter2) -> Integer.compare(
          counter2.getCommentariesCounter(), counter1.getCommentariesCounter()));
    }
  },
  ENDORSEMENT_AMOUNT("Quantidade de Indicações") {
    @Override
    public void sort(List<Technology> techList) {
      Collections.sort(techList, (counter1, counter2) -> Integer.compare(
          counter2.getEndorsersCounter(), counter1.getEndorsersCounter()));
    }
  },
  APHABETIC("Alfabética") {
    @Override
    public void sort(List<Technology> techList) {
      Collections.sort(techList,
          (counter1, counter2) -> counter1.getName().compareTo(counter2.getName()));
    }
  };

  private String option;

  TechnologyOrderOptionEnum(String option) {
    this.option = option;
  }

  public String option() {
    return option;
  }

  /**
   * Convert the text informed to Enum.
   *
   * @param text to be converted.
   * 
   * @return the enum value.
   */
  public static TechnologyOrderOptionEnum fromString(String text) {
    if (text != null) {
      return Arrays.stream(TechnologyOrderOptionEnum.values())
          .filter(item -> text.equalsIgnoreCase(item.option()))
          .findFirst()
          .orElse(null);
    }
    return null;
  }

  public abstract void sort(List<Technology> techList);
}
