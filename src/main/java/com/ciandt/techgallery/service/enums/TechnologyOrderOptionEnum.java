package com.ciandt.techgallery.service.enums;

import com.ciandt.techgallery.persistence.model.Technology;

import java.util.Arrays;
import java.util.List;

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
      techList.sort((c1, c2) -> Integer.compare(
          c2.getPositiveRecommendationsCounter(), c1.getPositiveRecommendationsCounter()));
    }
  },
  NEGATIVE_RECOMMENDATION_AMOUNT("Quantidade de Recomendações Negativas") {
    @Override
    public void sort(List<Technology> techList) {
      techList.sort((c1, c2) -> Integer.compare(
          c2.getNegativeRecommendationsCounter(), c1.getNegativeRecommendationsCounter()));
    }
  },
  COMMENT_AMOUNT("Quantidade de Comentários") {
    @Override
    public void sort(List<Technology> techList) {
      techList.sort((c1, c2) -> Integer.compare(
          c2.getCommentariesCounter(), c1.getCommentariesCounter()));
    }
  },
  ENDORSEMENT_AMOUNT("Quantidade de Indicações") {
    @Override
    public void sort(List<Technology> techList) {
      techList.sort((c1, c2) -> Integer.compare(
          c2.getEndorsersCounter(), c1.getEndorsersCounter()));
    }
  },
  APHABETIC("Alfabética") {
    @Override
    public void sort(List<Technology> techList) {
      techList.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
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
