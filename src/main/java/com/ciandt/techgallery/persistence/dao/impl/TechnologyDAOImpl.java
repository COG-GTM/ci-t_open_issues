package com.ciandt.techgallery.persistence.dao.impl;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Ref;

import com.ciandt.techgallery.ofy.OfyService;
import com.ciandt.techgallery.persistence.dao.TechnologyDAO;
import com.ciandt.techgallery.persistence.model.Technology;

import java.util.ArrayList;
import java.util.List;

/**
 * TechnologyDAOImpl methods implementation.
 *
 * @author Felipe Goncalves de Castro
 *
 */
public class TechnologyDAOImpl extends GenericDAOImpl<Technology, String> implements TechnologyDAO {

  /*
   * Attributes --------------------------------------------
   */
  private static TechnologyDAOImpl instance;

  /*
   * Constructor --------------------------------------------
   */
  private TechnologyDAOImpl() {}

  /**
   * Singleton method for the DAO.
   *
   * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros Moreira </a>
   * @since 08/10/2015
   *
   * @return TechnologyDAOImpl instance.
   */
  public static TechnologyDAOImpl getInstance() {
    if (instance == null) {
      instance = new TechnologyDAOImpl();
    }
    return instance;
  }

  @Override
  public Technology findByName(String name) {
    final Objectify objectify = OfyService.ofy();
    Technology entity =
        objectify.load().type(Technology.class).filter(Technology.NAME, name).first().now();

    return entity;
  }

  @Override
  public Technology findByIdActive(String id) {
    Technology technology = super.findById(id);
    if (technology.getActive()) {
      return technology;
    }
    return null;
  }

  @Override
  public List<Technology> findChildrenByParentId(String parentId) {
    final Objectify objectify = OfyService.ofy();
    Technology parent = findById(parentId);
    if (parent == null) {
      return new ArrayList<>();
    }
    
    Ref<Technology> parentRef = Ref.create(parent);
    return objectify.load().type(Technology.class)
        .filter(Technology.PARENT_TECHNOLOGY, parentRef)
        .filter(Technology.ACTIVE, true)
        .list();
  }

  @Override
  public List<Technology> findRootTechnologies() {
    final Objectify objectify = OfyService.ofy();
    return objectify.load().type(Technology.class)
        .filter(Technology.PARENT_TECHNOLOGY, null)
        .filter(Technology.ACTIVE, true)
        .list();
  }

  @Override
  public List<Technology> findTechnologyPath(String techId) {
    List<Technology> path = new ArrayList<>();
    Technology current = findByIdActive(techId);
    
    while (current != null) {
      path.add(0, current);
      if (current.getParentTechnology() != null) {
        current = current.getParentTechnology().get();
      } else {
        current = null;
      }
    }
    
    return path;
  }
}
