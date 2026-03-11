package com.ciandt.techgallery.utils;

import com.googlecode.objectify.Ref;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for dereferencing Objectify Ref objects.
 * Updated to use Java 8 streams instead of Guava's Function and Lists.transform.
 */
public class Dereferencer {

  public static <T> T deref(Ref<T> ref) {
    return ref == null ? null : ref.get();
  }

  public static <T> List<T> deref(List<Ref<T>> reflist) {
    return reflist.stream()
        .map(Dereferencer::deref)
        .collect(Collectors.toList());
  }
}
