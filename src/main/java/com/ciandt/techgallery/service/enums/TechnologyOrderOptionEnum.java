package com.ciandt.techgallery.service.enums;

import com.ciandt.techgallery.persistence.model.Technology;

import java.util.Comparator;
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
      techList.sort(Comparator.comparingInt(
          (Technology t) -> t.getPositiveRecommendationsCounter()).reversed());
    }
  },
  NEGATIVE_RECOMMENDATION_AMOUNT("Quantidade de Recomendações Negativas") {
    @Override
    public void sort(List<Technology> techList) {
      techList.sort(Comparator.comparingInt(
          (Technology t) -> t.getNegativeRecommendationsCounter()).reversed());
    }
  },
  COMMENT_AMOUNT("Quantidade de Comentários") {
    @Override
    public void sort(List<Technology> techList) {
      techList.sort(Comparator.comparingInt(
          (Technology t) -> t.getCommentariesCounter()).reversed());
    }
  },
  ENDORSEMENT_AMOUNT("Quantidade de Indicações") {
    @Override
    public void sort(List<Technology> techList) {
      techList.sort(Comparator.comparingInt(
          (Technology t) -> t.getEndorsersCounter()).reversed());
    }
  },
  APHABETIC("Alfabética") {
    @Override
    public void sort(List<Technology> techList) {
      techList.sort(Comparator.comparing(Technology::getName));
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
      for (TechnologyOrderOptionEnum item : TechnologyOrderOptionEnum.values()) {
        if (text.equalsIgnoreCase(item.option())) {
          return item;
        }
      }
    }
    return null;
  }

  public abstract void sort(List<Technology> techList);
}
